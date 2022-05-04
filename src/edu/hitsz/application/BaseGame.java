package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.enemycreator.EliteCreator;
import edu.hitsz.enemycreator.EnemyCreator;
import edu.hitsz.enemycreator.MobCreator;
import edu.hitsz.items.AbstractItem;
import edu.hitsz.items.ItemBomb;
import edu.hitsz.items.ItemFire;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public abstract class BaseGame extends JPanel {

    protected int backGroundTop = 0;

    /**
     * 线程相关参数
     * 1. Scheduled 线程池，用于任务调度
     * 2. 计时线程，用于计算火力道具生效时间
     * 3. boss敌机背景音乐线程
     */
    private final ScheduledExecutorService executorService;
    private Thread calTimeThread = null;
    private MusicThread bossMusic;

    /**
     * 时间相关参数
     * 1. 时间间隔(ms)，控制刷新频率
     * 2. time耗时
     * 3. 指示距上次敌机产生的时间
     */
    private final int timeInterval = 40;
    private int enemyCycleTime = 0;
    private int heroCycleTime = 0;
    protected int time = 0;


    /**
     * 游戏过程所需参数
     */
    private final HeroAircraft heroAircraft;
    protected final List<AbstractEnemy> enemyAircrafts;
    protected final List<BaseBullet> heroBullets;
    protected final List<BaseBullet> enemyBullets;
    protected final List<AbstractItem> items;
    protected final List<AbstractItem> itemBombs;
    private int score = 0;
    protected int bossAppearFlag = 0;
    private boolean gameOverFlag = false;
    protected Boolean playMusic;

    /**
     * 游戏难度相关参数
     * 1. boss产生阈值(每bossAppearThreshold分产生一架boss)
     * 2. 敌机最大数量
     * 3. 精英机产生的阈值
     * 4. 子弹发射、产生敌机频率(ms)
     */
    protected static int bossAppearThreshold;
    protected static int enemyMaxNumber;
    protected static double eliteAppearThreshold;
    protected static double[] dropItemThresh;
    protected static int enemyCycleDuration;
    protected static int heroCycleDuration;
    protected static int difficultyUpdateCycle = 15000;
    protected static HashMap<String, Integer> mobParam = new HashMap<>();
    protected static HashMap<String, Integer> eliteParam = new HashMap<>();
    protected static HashMap<String, Integer> bossParam = new HashMap<>();

    public BaseGame() {

        heroAircraft = HeroAircraft.getHeroAircraft();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        items = new LinkedList<>();
        itemBombs = new LinkedList<>();

        //Scheduled 线程池，用于定时任务调度
        ThreadFactory gameThread = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t =new Thread(r);
                t.setName("game thread");
                return t;
            }
        };
        executorService = new ScheduledThreadPoolExecutor(1, gameThread);

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action(String difficulty) {

        if(playMusic){
            MusicThread backgroundMusic = new MusicThread(System.getProperty("user.dir") + "\\src\\videos\\bgm.wav", true);
            backgroundMusic.start();
        }

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge(enemyCycleTime, enemyCycleDuration)) {
                // 新敌机产生
                if (enemyAircrafts.size() < enemyMaxNumber) {
                    generateEnemy();
                }
                // 敌机射出子弹
                enemyShootAction();
            }

            if (timeCountAndNewCycleJudge(heroCycleTime, heroCycleDuration)) {
                // 英雄机射出子弹
                heroShootAction();
            }

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            // 道具移动
            itemsMoveAction();

            // 撞击检测
            crashCheckAction();

            // 后处理
            postProcessAction();

            // 每个时刻重绘界面
            repaint();

            // 难度增强检测
            difficultyUpdateCheck();

            // 游戏结束检查
            checkGameOver();
        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);
    }


    //***********************
    //      产生敌机各部分
    //***********************

    protected abstract void generateEnemy();

    protected abstract void generateBoss();

    protected void generateBossMusic(Boolean playMusic){
        if(playMusic){
            bossMusic = new MusicThread(System.getProperty("user.dir") + "\\src\\videos\\bgm_boss.wav", true);
            bossMusic.start();
        }
    }

    protected void generateElite(){
        EnemyCreator enemyCreator = new EliteCreator();
        AbstractEnemy elite = enemyCreator.createEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2),
                eliteParam.get("speedX"),
                eliteParam.get("speedY"),
                eliteParam.get("hp")
        );
        enemyAircrafts.add(elite);
        for(AbstractItem bomb : itemBombs){
            ((ItemBomb)bomb).addSubscriber(elite);
        }
    }

    protected void generateMob(){
        EnemyCreator enemyCreator = new MobCreator();
        AbstractEnemy mob = enemyCreator.createEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2),
                mobParam.get("speedX"),
                mobParam.get("speedY"),
                mobParam.get("hp")
        );
        enemyAircrafts.add(mob);
        for(AbstractItem bomb : itemBombs){
            ((ItemBomb)bomb).addSubscriber(mob);
        }
    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudge(int cycleTime, int cycleDuration) {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            if(cycleDuration == enemyCycleDuration){
                this.enemyCycleTime = cycleTime;
            }
            else{
                this.heroCycleTime = cycleTime;
            }
            return true;
        } else {
            if(cycleDuration == enemyCycleDuration){
                this.enemyCycleTime = cycleTime;
            }
            else{
                this.heroCycleTime = cycleTime;
            }
            return false;
        }
    }

    private void enemyShootAction() {
        // 敌机射击
        List<BaseBullet> bullets;
        for(AbstractAircraft enemyAircraft : enemyAircrafts ){
            if(!(enemyAircraft instanceof MobEnemy)) {
                bullets = enemyAircraft.shoot();
                enemyBullets.addAll(bullets);
                for(AbstractItem bomb : itemBombs){
                    ((ItemBomb)bomb).addAllSubscriber(bullets);
                }
            }
        }
    }

    private void heroShootAction(){
        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void itemsMoveAction() {
        for (AbstractItem item : items) {
            item.forward();
        }
    }

    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // TODO 敌机子弹攻击英雄
        for(BaseBullet bullet: enemyBullets){
            if(bullet.notValid()) {
                continue;
            }
            else if(bullet.crash(heroAircraft)){
                // 英雄机损失生命值
                heroAircraft.decreaseHp(bullet.getPower());
                for(AbstractItem bomb : itemBombs){
                    ((ItemBomb)bomb).deleteSubscriber(bullet);
                }
                bullet.vanish();
            }
        }
        // 英雄子弹攻击敌机
        AbstractItem newItem = null;
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractEnemy enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet) || bullet.crash(enemyAircraft)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    // 一次只能有一架boss机
                    // 控制子弹击中敌机音效
                    if(playMusic){
                        MusicThread hitMusic = new MusicThread(System.getProperty("user.dir") + "\\src\\videos\\bullet_hit.wav", false);
                        hitMusic.start();
                    }

                    bullet.vanish();
                    enemyAircraft.decreaseHp(bullet.getPower());
                    if(enemyAircraft instanceof BossEnemy && enemyAircraft.getHp()==0){
                        BossEnemy.exist = false;
                        if(playMusic){
                            bossMusic.setStopFlag(true);
                        }
                    }
                    if (enemyAircraft.notValid()) {
                        // TODO 获得分数，产生道具补给
                        if(!(enemyAircraft instanceof MobEnemy)){
                            newItem = enemyAircraft.dropItems(dropItemThresh);
                            if(newItem != null){
                                items.add(newItem);
                            }
                            if(newItem instanceof ItemBomb){
                                itemBombs.add(newItem);
                            }
                        }
                        if(!(enemyAircraft instanceof BossEnemy)){
                            for(AbstractItem bomb : itemBombs){
                                ((ItemBomb)bomb).deleteSubscriber(enemyAircraft);
                            }
                        }
                        if(enemyAircraft instanceof MobEnemy){
                            score += 10;
                            bossAppearFlag += 10;
                        }
                        else if(enemyAircraft instanceof EliteEnemy){
                            score += 20;
                            bossAppearFlag += 20;
                        }
                        else if(enemyAircraft instanceof BossEnemy){
                            score += 50;
                            bossAppearFlag += 50;
                        }
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 若新增道具为炸弹道具，需要将当前精英机，敌机子弹加入炸弹的订阅者列表
        if(newItem instanceof ItemBomb){
            for(AbstractEnemy aircraft: enemyAircrafts){
                if(!aircraft.notValid() && !(aircraft instanceof BossEnemy)){
                    ((ItemBomb) newItem).addSubscriber(aircraft);
                }
            }
            for(BaseBullet bullet1: enemyBullets){
                if(!bullet1.notValid()){
                    ((ItemBomb) newItem).addSubscriber(bullet1);
                }
            }
        }

        // 道具生效+火力道具计时
        for(AbstractItem item : items){
            if(item.notValid()) {
                continue;
            } else if(item.crash(heroAircraft) || heroAircraft.crash(item)){
                MusicThread supplyMusic;
                if(item instanceof ItemBomb){
                    if(playMusic){
                        MusicThread bombMusic = new MusicThread(System.getProperty("user.dir") + "\\src\\videos\\bomb_explosion.wav", false);
                        bombMusic.start();
                    }
                    for(AbstractFlyingObject obj : ((ItemBomb)item).getSubscriber()){
                        if(obj instanceof MobEnemy){
                            score += 10;
                            bossAppearFlag += 10;
                        }
                        else if(obj instanceof EliteEnemy){
                            score += 20;
                            bossAppearFlag += 20;
                        }
                    }
                }
                else if(item instanceof ItemFire){
                    if(heroAircraft.getFireActivated()){
                        calTimeThread.interrupt();
                    }
                    calTimeThread = new CalTimeThread();
                    calTimeThread.start();
                    if(playMusic){
                        supplyMusic = new MusicThread(System.getProperty("user.dir") + "\\src\\videos\\get_supply.wav", false);
                        supplyMusic.start();
                    }
                }
                else if(playMusic){
                    supplyMusic = new MusicThread(System.getProperty("user.dir") + "\\src\\videos\\get_supply.wav", false);
                    supplyMusic.start();
                }
                item.activateItem(heroAircraft);
                item.vanish();
            }
        }
    }

    /**
     * 检查难度是否提升
     */
    protected abstract void difficultyUpdateCheck();

    private void checkGameOver(){
        if (heroAircraft.getHp() <= 0) {

            executorService.shutdown();
            gameOverFlag = true;

            // 释放主线程的锁
            synchronized (Main.MAIN_LOCK){
                Main.MAIN_LOCK.notify();
            }

            if(playMusic){
                bossMusic.setStopFlag(true);
                MusicThread gameOverMusic = new MusicThread(System.getProperty("user.dir") + "\\src\\videos\\game_over.wav", false);
                gameOverMusic.start();
            }
            System.out.println("Game Over!");
        }
    }

    public void setPlayMusic(Boolean playMusic) {
        this.playMusic = playMusic;
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 检查英雄机生存
     * 3. 删除无效的敌机
     * 4. 删除无效道具
     * 5. 删除无效炸弹
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        items.removeIf(AbstractFlyingObject::notValid);
        itemBombs.removeIf(AbstractItem::notValid);
    }


    public boolean isGameOverFlag(){
        return gameOverFlag;
    }

    public int getScore() {
        return score;
    }

    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param  g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        paintBackground(g);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, items);

        paintImageWithPositionRevised(g, enemyAircrafts);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);
    }

    protected abstract void paintBackground(Graphics g);

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }


}

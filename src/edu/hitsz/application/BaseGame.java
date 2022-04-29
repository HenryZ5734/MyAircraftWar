package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.enemycreator.BossCreator;
import edu.hitsz.enemycreator.EliteCreator;
import edu.hitsz.enemycreator.EnemyCreator;
import edu.hitsz.enemycreator.MobCreator;
import edu.hitsz.items.AbstractItems;
import edu.hitsz.items.ItemFire;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
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
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;
    private Thread calTimeThread = null;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

    private final HeroAircraft heroAircraft;
    private final List<AbstractEnemy> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<AbstractItems> items;
    private int bossAppearFlag = 0;
    private int score = 0;

    /**
     * 游戏难度相关参数
     */
    protected static int bossAppearThreshold = 100;
    protected static int enemyMaxNumber = 5;
    protected static double eliteAppearThreshold = 0.6;

    private boolean gameOverFlag = false;
    private int time = 0;

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;

    public BaseGame() {
        heroAircraft = HeroAircraft.getHeroAircraft();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        items = new LinkedList<>();

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
    public void action() {

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);
                // 新敌机产生
                if (enemyAircrafts.size() < enemyMaxNumber) {
                    double i = Math.random();
                    if(bossAppearFlag >= bossAppearThreshold){
                        bossAppearFlag -= 100;
                        EnemyCreator enemyCreator = new BossCreator();
                        enemyAircrafts.add(enemyCreator.createEnemy(
                                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2),
                                1,
                                0,
                                200
                        ));
                    }
                    else if(i<eliteAppearThreshold){
                        EnemyCreator enemyCreator = new MobCreator();
                        enemyAircrafts.add(enemyCreator.createEnemy(
                                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2),
                                0,
                                10,
                                30
                        ));
                    }
                    else{
                        EnemyCreator enemyCreator = new EliteCreator();
                        enemyAircrafts.add(enemyCreator.createEnemy(
                                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())),
                                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2),
                                2,
                                10,
                                30
                        ));
                    }

                }
                // 飞机射出子弹
                shootAction();
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

            //每个时刻重绘界面
            repaint();

            // 游戏结束检查
            if (heroAircraft.getHp() <= 0) {

                executorService.shutdown();
                gameOverFlag = true;

                // 释放主线程的锁
                synchronized (Main.MAIN_LOCK){
                    Main.MAIN_LOCK.notify();
                }

                System.out.println("Game Over!");
            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void shootAction() {
        // TODO 敌机射击
        for(AbstractAircraft enemyAircraft : enemyAircrafts ){
            if(!(enemyAircraft instanceof MobEnemy)) {
                enemyBullets.addAll(enemyAircraft.shoot());
            }
        }
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
        for (AbstractItems item : items) {
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
            } else{
                if(bullet.crash(heroAircraft)){
                    // 英雄机损失生命值
                    heroAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                }
            }
        }
        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet) || bullet.crash(enemyAircraft)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    // 一次只能有一架boss机
                    bullet.vanish();
                    enemyAircraft.decreaseHp(bullet.getPower());
                    if(enemyAircraft instanceof BossEnemy && enemyAircraft.getHp()==0){
                        BossEnemy.exist = false;
                    }
                    if (enemyAircraft.notValid()) {
                        // TODO 获得分数，产生道具补给
                        if(!(enemyAircraft instanceof MobEnemy)){
                            AbstractItems itemTemp = ((AbstractEnemy)enemyAircraft).dropItems();
                            if(itemTemp != null){
                                items.add(itemTemp);
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

        // 道具生效+火力道具计时
        for(AbstractItems item : items){
            if(item.notValid()) {
                continue;
            } else if(item.crash(heroAircraft) || heroAircraft.crash(item)){
                if(item instanceof ItemFire){
                    if(heroAircraft.getFireActivated()){
                        calTimeThread.interrupt();
                    }
                    calTimeThread = new CalTimeThread();
                    calTimeThread.start();
                }
                item.activateItem(heroAircraft);
                item.vanish();
            }
        }
    }


    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        items.removeIf(AbstractFlyingObject::notValid);
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
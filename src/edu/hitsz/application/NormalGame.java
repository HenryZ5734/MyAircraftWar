package edu.hitsz.application;

import edu.hitsz.enemycreator.BossCreator;
import edu.hitsz.enemycreator.EnemyCreator;

import java.awt.*;
import java.util.Map;


/**
 * @author henry
 */
public class NormalGame extends BaseGame{
    // 设置游戏参数
    static{
        enemyMaxNumber = 7;
        bossAppearThreshold = 500;
        dropItemThresh = new double[]{0.35, 0.65, 0.85};
        eliteAppearThreshold = 0.45;
        enemyCycleDuration = 400;
        heroCycleDuration = 250;
        mobParam.putAll(Map.of(
                "speedX", 0,
                "speedY", 8,
                "hp",     30
        ));
        eliteParam.putAll(Map.of(
                "speedX", 2,
                "speedY", 8,
                "hp",     30
        ));
        bossParam.putAll(Map.of(
                "speedX", 2,
                "speedY", 0,
                "hp",     300
        ));
    }

    public NormalGame(){
        super();
    }

    @Override
    protected void generateEnemy() {
        double i = Math.random();
        if(bossAppearFlag >= bossAppearThreshold){
            generateBoss();
        }
        else if(i<eliteAppearThreshold){
            generateMob();
        }
        else{
            generateElite();
        }
    }

    @Override
    protected void generateBoss() {
        bossAppearFlag -= bossAppearThreshold;
        EnemyCreator enemyCreator = new BossCreator();
        enemyAircrafts.add(enemyCreator.createEnemy(
                (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.BOSS_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2),
                bossParam.get("speedX"),
                bossParam.get("speedY"),
                bossParam.get("hp")
        ));
        generateBossMusic(playMusic);
    }

    @Override
    protected void difficultyUpdateCheck() {
        if(time > 0 && time % difficultyUpdateCycle == 0){
            // 普通敌机血量小于120时靠增加血量增强难度
            if (mobParam.get("hp") < 90) {
                mobParam.replace("hp", (int) (eliteParam.get("hp") * 1.3));
                eliteParam.replace("hp", (int) (eliteParam.get("hp") * 1.3));
                bossParam.replace("hp", (int) (eliteParam.get("hp") * 1.3));
                System.out.println("难度增强，敌机血量增加1.3倍");
            }
            // 否则靠增加敌机数量上限增加难度,最大不超过12
            else if(enemyMaxNumber <= 12){
                enemyMaxNumber += 1;
                System.out.println("难度增强，敌机数量上限+1");
            }
            // 否则降低boss出现阈值
            else{
                bossAppearThreshold = Math.max(bossAppearFlag-50, 200);
            }
        }
    }

    @Override
    protected void paintBackground(Graphics g) {
        g.drawImage(ImageManager.BACKGROUND_IMAGE_NORMAL, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE_NORMAL, 0, this.backGroundTop, null);
    }
}

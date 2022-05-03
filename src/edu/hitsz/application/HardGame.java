package edu.hitsz.application;

import edu.hitsz.enemycreator.BossCreator;
import edu.hitsz.enemycreator.EnemyCreator;

import java.awt.*;
import java.util.Map;

/**
 * @author henry
 */
public class HardGame extends BaseGame{
    // 设置游戏参数
    static{
        enemyMaxNumber = 9;
        bossAppearThreshold = 400;
        dropItemThresh = new double[]{0.4, 0.7, 0.9};
        eliteAppearThreshold = 0.5;
        enemyCycleDuration = 400;
        heroCycleDuration = 250;
        mobParam.putAll(Map.of(
                "speedX", 0,
                "speedY", 8,
                "hp",     40
        ));
        eliteParam.putAll(Map.of(
                "speedX", 2,
                "speedY", 8,
                "hp",     40
        ));
        bossParam.putAll(Map.of(
                "speedX", 2,
                "speedY", 0,
                "hp",     200
        ));
    }

    public HardGame(){
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
        bossParam.replace("hp", bossParam.get("hp")+100);
        System.out.println("下次boss敌机血量加100");
        generateBossMusic(playMusic);
    }

    @Override
    protected void difficultyUpdateCheck() {
        if (time > 0 && time % difficultyUpdateCycle == 0) {
            if(mobParam.get("hp") < 120){
                mobParam.replace("hp", (int)(eliteParam.get("hp")*1.5));
                eliteParam.replace("hp", (int)(eliteParam.get("hp")*1.5));
                bossParam.replace("hp", (int)(eliteParam.get("hp")*1.5));
                System.out.println("难度增强，敌机血量增加1.5倍");
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
        g.drawImage(ImageManager.BACKGROUND_IMAGE_HARD, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE_HARD, 0, this.backGroundTop, null);
    }
}

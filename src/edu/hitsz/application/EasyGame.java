package edu.hitsz.application;

import java.awt.*;
import java.util.Map;

/**
 * @author henry
 */
public class EasyGame extends BaseGame{

    // 设置游戏参数
    static{
        enemyMaxNumber = 5;
        dropItemThresh = new double[]{0.2, 0.5, 0.7};
        eliteAppearThreshold = 0.5;
        cycleDuration = 400;
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
    }

    public EasyGame(){
        super();
    }

    @Override
    protected void generateEnemy() {
        double i = Math.random();
        if(i<eliteAppearThreshold){
            generateMob();
        }
        else{
            generateElite();
        }
    }

    @Override
    protected void generateBoss() {}

    @Override
    protected void difficultyUpdateCheck() {}

    @Override
    protected void paintBackground(Graphics g) {
        g.drawImage(ImageManager.BACKGROUND_IMAGE_EASY, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE_EASY, 0, this.backGroundTop, null);
    }
}

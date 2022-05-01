package edu.hitsz.application;

import java.awt.*;

/**
 * @author henry
 */
public class EasyGame extends BaseGame{

    // 设置游戏参数
    static{
        enemyMaxNumber = 5;
        bossAppearThreshold = 500;
        eliteAppearThreshold = 0.6;
        // TODO 设置飞机参数
    }

    public EasyGame(){
        super();
    }

    @Override
    protected void paintBackground(Graphics g) {
        g.drawImage(ImageManager.BACKGROUND_IMAGE_EASY, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE_EASY, 0, this.backGroundTop, null);
    }
}

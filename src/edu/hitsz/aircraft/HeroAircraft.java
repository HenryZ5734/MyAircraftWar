package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.shootstrategy.AbstractShootStrategy;
import edu.hitsz.shootstrategy.HeroSpread;
import edu.hitsz.shootstrategy.HeroStraight;

import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    /**
     * 是否火力加成
     */
    private boolean fireActivated = false;

    /** 英雄机的唯一实例 */
    private static HeroAircraft heroAircraft = new HeroAircraft(
            Main.WINDOW_WIDTH / 2,
            Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
            0, 0, 1000);

    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    初始生命值
     */
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    /**
     * 参数在HeroAircraft中修改
     * @return 英雄机的唯一实例
     */
    public static HeroAircraft getHeroAircraft(){
        return heroAircraft;
    }

    /** 加血 */
    public void increaseHp(int increment){
        this.hp = Math.min(this.hp+increment, maxHp);
    }

    /** 修改火力增强标志 */
    public void alterFireActivated(){
        heroAircraft.fireActivated = true;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    public List<BaseBullet> shoot() {
        AbstractShootStrategy abstractStrategy;
        if(fireActivated){
            abstractStrategy = new HeroSpread();
        }
        else{
            abstractStrategy = new HeroStraight();
        }
        return abstractStrategy.executeStrategy(
                getLocationX(),
                getLocationY(),
                0,
                getSpeedY()
        );
    }
}

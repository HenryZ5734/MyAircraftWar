package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.shootstrategy.AbstractShootStrategy;
import edu.hitsz.shootstrategy.BossSpread;

import java.util.List;

public class BossEnemy extends AbstractEnemy{

    /**
     * 判断boss机是否已经存在
     */
    public static boolean exist=false;

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public List<BaseBullet> shoot() {
        AbstractShootStrategy abstractStrategy = new BossSpread();
        return abstractStrategy.executeStrategy(
                getLocationX(),
                getLocationY(),
                0,
                getSpeedY()
        );
    }
}

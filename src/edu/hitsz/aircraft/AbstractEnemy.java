package edu.hitsz.aircraft;

import edu.hitsz.application.Main;

public abstract class AbstractEnemy extends AbstractAircraft{


    public AbstractEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.hp = hp;
        this.maxHp = hp;
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

}

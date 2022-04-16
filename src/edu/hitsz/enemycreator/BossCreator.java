package edu.hitsz.enemycreator;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.BossEnemy;

public class BossCreator implements EnemyCreator{
    @Override
    public AbstractEnemy createEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        AbstractEnemy boss = new BossEnemy(locationX, locationY, speedX, speedY, hp);
        if(BossEnemy.exist){
            boss.vanish();
        }
        else{
            BossEnemy.exist = true;
        }
        return boss;
    }
}

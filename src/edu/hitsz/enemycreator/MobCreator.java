package edu.hitsz.enemycreator;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.MobEnemy;

public class MobCreator implements EnemyCreator{
    @Override
    public AbstractEnemy createEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        return new MobEnemy(locationX, locationY, speedX, speedY, hp);
    }
}

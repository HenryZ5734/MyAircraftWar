package edu.hitsz.enemycreator;


import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.EliteEnemy;

public class EliteCreator implements EnemyCreator {
    @Override
    public AbstractEnemy createEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        return new EliteEnemy(locationX, locationY, speedX, speedY, hp);
    }
}

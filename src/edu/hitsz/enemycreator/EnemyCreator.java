package edu.hitsz.enemycreator;

import edu.hitsz.aircraft.AbstractEnemy;

public interface EnemyCreator {
    AbstractEnemy createEnemy(int locationX, int locationY, int speedX, int speedY, int hp);
}

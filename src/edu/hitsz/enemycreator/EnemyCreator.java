package edu.hitsz.enemycreator;

import edu.hitsz.aircraft.AbstractEnemy;

public interface EnemyCreator {
    /**
     * 创建敌机的抽象方法
     * @param locationX
     * @param locationY
     * @param speedX
     * @param speedY
     * @param hp
     * @return
     */
    AbstractEnemy createEnemy(int locationX, int locationY, int speedX, int speedY, int hp);
}

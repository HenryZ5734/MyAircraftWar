package edu.hitsz.aircraft;

import edu.hitsz.items.AbstractItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EliteEnemyTest {

    private EliteEnemy eliteEnemy;

    @BeforeEach
    void setUp() {
        eliteEnemy = new EliteEnemy(0,0,0,0,100);
    }

    @AfterEach
    void tearDown() {
        eliteEnemy = null;
    }

    /**
     * 测试精英机掉血功能
     * 1. 是否按子弹伤害掉血
     * 2. 血量小于等于0时是否会消失
     * 3. 血量小于0时是否会置0
     */
    @DisplayName("测试精英机掉血功能")
    @Test
    void decreaseHp() {

        int decrease1 = 50;
        eliteEnemy.decreaseHp(decrease1);
        assertEquals(50, eliteEnemy.getHp());

        int decrease2 = 60;
        eliteEnemy.decreaseHp(decrease2);
        assertEquals(0, eliteEnemy.getHp());
        assertTrue(eliteEnemy.notValid());
    }

    /**
     * 测试精英机掉落道具的功能
     * 判断返回值是否为null或者AbstractItems的子类
     */
    @DisplayName("测试精英机掉落道具的功能")
    @Test
    void dropItems() {
        Object item = eliteEnemy.dropItems(new double[]{0.4, 0.6, 0.9});
        if(item!=null){
            assertInstanceOf(AbstractItem.class, item);
        }else{
            assertNull(null);
        }
    }
}
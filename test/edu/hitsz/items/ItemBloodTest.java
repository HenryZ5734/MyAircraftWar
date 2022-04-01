package edu.hitsz.items;

import edu.hitsz.aircraft.HeroAircraft;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemBloodTest {

    private AbstractItems item;
    private HeroAircraft heroAircraft = HeroAircraft.getHeroAircraft();

    @BeforeEach
    void setUp() {
        item = new ItemBlood(0,0,0,0);
    }

    @AfterEach
    void tearDown() {
        item = null;
    }

    /**
     * 测试道具的消失方法
     */
    @DisplayName("测试道具的消失方法")
    @Test
    void vanish() {
        assertFalse(item.notValid());
        item.vanish();
        assertTrue(item.notValid());
    }

    /**
     * 测试加血道具效果
     * 即是否能按指定血量加
     */
    @DisplayName("测试加血道具效果")
    @Test
    void activateItem() {
        heroAircraft.decreaseHp(30);
        item.activateItem(heroAircraft);
        assertEquals(90, heroAircraft.getHp());
    }
}
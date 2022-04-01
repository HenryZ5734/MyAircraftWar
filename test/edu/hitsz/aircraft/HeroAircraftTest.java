package edu.hitsz.aircraft;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class HeroAircraftTest {

    /**
     * 测试英雄机单例模式
     * 1. 测试实例类型是否为HeroAircraft
     * 2. 测试两个实例是否相同
     */
    @DisplayName("测试英雄机单例模式")
    @Test
    void getHeroAircraft() {
        HeroAircraft instance1 = HeroAircraft.getHeroAircraft();
        HeroAircraft instance2 = HeroAircraft.getHeroAircraft();
        assertAll(
                () -> assertTrue(HeroAircraft.getHeroAircraft() instanceof HeroAircraft),
                () -> assertEquals(instance1, instance2)
        );
    }

    /**
     * 测试英雄机血量增长
     * 1. 测试每次是否正确增长
     * 2. 测试是否会超过最大血量
     */
    @DisplayName("测试英雄机血量增长")
    @Test
    void increaseHp() {

        HeroAircraft heroAircraft = HeroAircraft.getHeroAircraft();

        // 假设decrease小于最大血量
        int decrease = 50;
        assumeTrue(decrease <= heroAircraft.maxHp);

        // 假设decreaseHp方法正确执行
        heroAircraft.decreaseHp(decrease);
        assumeTrue(heroAircraft.getHp() == heroAircraft.maxHp - 50);

        // 测试
        heroAircraft.increaseHp(10);
        assertEquals(60, heroAircraft.getHp());

        heroAircraft.increaseHp(50);
        assertEquals(100, heroAircraft.getHp());
    }
}
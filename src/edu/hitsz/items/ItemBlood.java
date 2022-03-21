package edu.hitsz.items;

import edu.hitsz.aircraft.HeroAircraft;

public class ItemBlood extends AbstractItems{

    private int add_Hp = 20;

    public ItemBlood(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void ActivateItem(HeroAircraft heroAircraft) {
        heroAircraft.increaseHp(add_Hp);
    }
}

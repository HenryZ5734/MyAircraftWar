package edu.hitsz.items;

import edu.hitsz.aircraft.HeroAircraft;

public class ItemBlood extends AbstractItems{

    private int add_Hp = 20;

    public ItemBlood(int locationX, int locationY) {
        super(locationX, locationY);
    }

    @Override
    public void ActivateItem(HeroAircraft heroAircraft) {
        heroAircraft.increaseHp(add_Hp);
    }
}

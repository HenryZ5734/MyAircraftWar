package edu.hitsz.items;

import edu.hitsz.aircraft.HeroAircraft;

public class ItemBomb extends AbstractItems{

    public ItemBomb(int locationX, int locationY) {
        super(locationX, locationY);
    }

    @Override
    public void ActivateItem(HeroAircraft heroAircraft) {
        System.out.println("BombSupply active!");
    }
}

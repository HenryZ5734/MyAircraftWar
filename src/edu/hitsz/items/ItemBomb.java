package edu.hitsz.items;

import edu.hitsz.aircraft.HeroAircraft;

public class ItemBomb extends AbstractItems{

    public ItemBomb(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void ActivateItem(HeroAircraft heroAircraft) {
        System.out.println("BombSupply active!");
    }
}

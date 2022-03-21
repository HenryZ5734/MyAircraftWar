package edu.hitsz.items;

import edu.hitsz.aircraft.HeroAircraft;

public class ItemFire extends AbstractItems{

    private int add_Fire = 10;

    public ItemFire(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void ActivateItem(HeroAircraft heroAircraft) {
        System.out.println("FireSupply active!");
    }
}

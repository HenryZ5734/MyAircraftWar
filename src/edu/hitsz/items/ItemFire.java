package edu.hitsz.items;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * @author HenryZ
 */
public class ItemFire extends AbstractItems{

    private int addFire = 10;

    public ItemFire(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activateItem(HeroAircraft heroAircraft) {
        System.out.println("FireSupply active!");
    }
}

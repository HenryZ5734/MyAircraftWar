package edu.hitsz.items;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * @author HenryZ
 */
public class ItemFire extends AbstractItems{

    public ItemFire(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activateItem(HeroAircraft heroAircraft) {
        heroAircraft.setFireActivated(true);
    }
}

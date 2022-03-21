package edu.hitsz.items;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.basic.AbstractFlyingObject;

public abstract class AbstractItems extends AbstractFlyingObject {

    public AbstractItems(int locationX, int locationY){
        this.locationX = locationX;
        this.locationY = locationY;
    }

    public abstract void ActivateItem(HeroAircraft heroAircraft);
}

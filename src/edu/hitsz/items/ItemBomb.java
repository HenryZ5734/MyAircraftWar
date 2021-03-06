package edu.hitsz.items;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;

import java.util.ArrayList;
import java.util.List;

public class ItemBomb extends AbstractItem {

    private ArrayList<AbstractFlyingObject> subscriber = new ArrayList<>();

    public ItemBomb(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    public void addSubscriber(AbstractFlyingObject obj){
        subscriber.add(obj);
    }

    public void addAllSubscriber(List<BaseBullet> obj){
        subscriber.addAll(obj);
    }

    public void deleteSubscriber(AbstractFlyingObject obj){
        subscriber.remove(obj);
    }

    private void notifySubscriber(){
        for(AbstractFlyingObject obj : subscriber){
            obj.vanish();
        }
    }

    public ArrayList<AbstractFlyingObject> getSubscriber() {
        return subscriber;
    }

    @Override
    public void activateItem(HeroAircraft heroAircraft) {
        notifySubscriber();
    }
}

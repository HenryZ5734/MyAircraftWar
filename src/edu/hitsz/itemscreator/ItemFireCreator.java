package edu.hitsz.itemscreator;

import edu.hitsz.items.AbstractItem;
import edu.hitsz.items.ItemFire;

public class ItemFireCreator implements ItemCreator {

    @Override
    public AbstractItem createItem(int locationX, int locationY, int speedX, int speedY) {
        return new ItemFire(locationX, locationY, speedX, speedY);
    }
}

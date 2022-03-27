package edu.hitsz.itemscreator;

import edu.hitsz.items.AbstractItems;
import edu.hitsz.items.ItemFire;

public class ItemFireCreator implements ItemsCreator{

    @Override
    public AbstractItems CreateItem(int locationX, int locationY, int speedX, int speedY) {
        return new ItemFire(locationX, locationY, speedX, speedY);
    }
}

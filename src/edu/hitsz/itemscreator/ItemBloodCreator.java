package edu.hitsz.itemscreator;

import edu.hitsz.items.AbstractItems;
import edu.hitsz.items.ItemBlood;

public class ItemBloodCreator implements ItemsCreator{
    @Override
    public AbstractItems createItem(int locationX, int locationY, int speedX, int speedY) {
        return new ItemBlood(locationX, locationY, speedX, speedY);
    }
}

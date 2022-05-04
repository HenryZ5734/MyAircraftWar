package edu.hitsz.itemscreator;

import edu.hitsz.items.AbstractItem;
import edu.hitsz.items.ItemBlood;

public class ItemBloodCreator implements ItemCreator {
    @Override
    public AbstractItem createItem(int locationX, int locationY, int speedX, int speedY) {
        return new ItemBlood(locationX, locationY, speedX, speedY);
    }
}

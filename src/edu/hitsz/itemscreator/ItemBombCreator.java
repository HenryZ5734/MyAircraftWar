package edu.hitsz.itemscreator;

import edu.hitsz.items.AbstractItem;
import edu.hitsz.items.ItemBomb;

public class ItemBombCreator implements ItemCreator {
    @Override
    public AbstractItem createItem(int locationX, int locationY, int speedX, int speedY) {
        return new ItemBomb(locationX, locationY, speedX, speedY);
    }
}

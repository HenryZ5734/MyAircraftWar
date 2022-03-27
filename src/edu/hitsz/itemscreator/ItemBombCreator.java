package edu.hitsz.itemscreator;

import edu.hitsz.items.AbstractItems;
import edu.hitsz.items.ItemBomb;

public class ItemBombCreator implements ItemsCreator{
    @Override
    public AbstractItems CreateItem(int locationX, int locationY, int speedX, int speedY) {
        return new ItemBomb(locationX, locationY, speedX, speedY);
    }
}

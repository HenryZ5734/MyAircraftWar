package edu.hitsz.itemscreator;

import edu.hitsz.items.AbstractItems;

public interface ItemsCreator {
    AbstractItems CreateItem(int locationX, int locationY, int speedX, int speedY);
}

package edu.hitsz.itemscreator;

import edu.hitsz.items.AbstractItem;

/**
 *
 * @author HenryZ
 */
public interface ItemCreator {
    /**
     * 创建道具的抽象方法
     * @param locationX
     * @param locationY
     * @param speedX
     * @param speedY
     * @return
     */
    AbstractItem createItem(int locationX, int locationY, int speedX, int speedY);
}

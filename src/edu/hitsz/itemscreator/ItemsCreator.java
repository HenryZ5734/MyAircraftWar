package edu.hitsz.itemscreator;

import edu.hitsz.items.AbstractItems;

/**
 *
 * @author HenryZ
 */
public interface ItemsCreator {
    /**
     * 创建道具的抽象方法
     * @param locationX
     * @param locationY
     * @param speedX
     * @param speedY
     * @return
     */
    AbstractItems createItem(int locationX, int locationY, int speedX, int speedY);
}

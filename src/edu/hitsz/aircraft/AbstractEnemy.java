package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.items.AbstractItem;
import edu.hitsz.itemscreator.ItemBloodCreator;
import edu.hitsz.itemscreator.ItemBombCreator;
import edu.hitsz.itemscreator.ItemFireCreator;
import edu.hitsz.itemscreator.ItemCreator;

/**
 * @author henry
 */
public abstract class AbstractEnemy extends AbstractAircraft{


    public AbstractEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.hp = hp;
        this.maxHp = hp;
    }

    /** 产生道具 */
    public AbstractItem dropItems(double[] thresh){
        double i = Math.random();
        if(i<thresh[0]) {
            return null;
        } else if(i<thresh[1]){
            ItemCreator creator = new ItemBloodCreator();
            return creator.createItem(locationX, locationY, 0, 4);
        } else if(i<thresh[2]){
            ItemCreator creator = new ItemFireCreator();
            return creator.createItem(locationX, locationY, 0, 4);
        } else{
            ItemCreator creator = new ItemBombCreator();
            return creator.createItem(locationX, locationY, 0, 4);
        }
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }

}

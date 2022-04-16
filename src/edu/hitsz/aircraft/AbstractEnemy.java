package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.items.AbstractItems;
import edu.hitsz.itemscreator.ItemBloodCreator;
import edu.hitsz.itemscreator.ItemBombCreator;
import edu.hitsz.itemscreator.ItemFireCreator;
import edu.hitsz.itemscreator.ItemsCreator;

/**
 * @author henry
 */
public abstract class AbstractEnemy extends AbstractAircraft{


    public AbstractEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.hp = hp;
        this.maxHp = hp;
    }

    /**产生道具,概率为0.4，0.3，0.2，0.1*/
    public AbstractItems dropItems(){
        double i = Math.random();
        double[] thresh = {0.4, 0.7, 0.9};
        if(i<thresh[0]) {
            return null;
        } else if(i<thresh[1]){
            ItemsCreator creator = new ItemBloodCreator();
            return creator.createItem(locationX, locationY, 0, 4);
        } else if(i<thresh[2]){
            ItemsCreator creator = new ItemFireCreator();
            return creator.createItem(locationX, locationY, 0, 4);
        } else{
            ItemsCreator creator = new ItemBombCreator();
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

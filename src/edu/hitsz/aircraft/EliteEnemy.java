package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.items.AbstractItems;
import edu.hitsz.itemscreator.ItemBloodCreator;
import edu.hitsz.itemscreator.ItemBombCreator;
import edu.hitsz.itemscreator.ItemFireCreator;
import edu.hitsz.itemscreator.ItemsCreator;

import java.util.LinkedList;
import java.util.List;

public class EliteEnemy extends AbstractEnemy{

    /**攻击方式*/
    private int shootNum = 1; //子弹一次发射数量
    private int power =30; //子弹伤害
    private int direction = 1; //子弹射击方向

    /**精英敌机的构造函数*/
    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = getLocationX();
        int y = getLocationY() + direction*2;
        int speedX = 0;
        int speedY = this.getSpeedY() + direction*5;
        BaseBullet baseBullet;
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            baseBullet = new EnemyBullet(x + (i*2 - shootNum + 1)*10, y, speedX, speedY, power);
            res.add(baseBullet);
        }
        return res;
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
}

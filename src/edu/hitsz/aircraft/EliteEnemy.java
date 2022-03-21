package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.items.AbstractItems;
import edu.hitsz.items.ItemBlood;
import edu.hitsz.items.ItemBomb;
import edu.hitsz.items.ItemFire;

import java.util.LinkedList;
import java.util.List;

public class EliteEnemy extends AbstractEnemy{

    /**攻击方式*/
    private int shootNum = 1;     //子弹一次发射数量
    private int power =30;       //子弹伤害
    private int direction = 1;  //子弹射击方向

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
    public AbstractItems drop_Items(){
        double i = Math.random();
        if(i<0.4) return null;
        else if(i<0.7) return new ItemBlood(locationX, locationY, 0, 4);
        else if(i<0.9) return new ItemFire(locationX, locationY, 0, 4);
        else return new ItemBomb(locationX, locationY, 0, 4);
    }
}

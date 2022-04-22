package edu.hitsz.rank;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author henry
 */
public class Record implements Serializable {

    private final String name;
    private final int score;
    private int rank;
    private String time;

    public Record(String name, int score){
        this.name = name;
        this.score = score;
        this.rank = 0;
        setTime();
    }

    private void setTime(){
        Date time = new Date();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.time = formatter.format(time);
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString(){
        return "第 " + getRank() + " 名, " + getName() + ", " + getScore() + ", " + getTime();
    }
}

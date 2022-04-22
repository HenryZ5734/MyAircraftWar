package edu.hitsz.rank;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author henry
 */
public class RecordDaoImpl implements RecordDao{

    private ArrayList<Record> rank = new ArrayList<>();

    public void sortByScoreDescending(){
        int i = 1;
        rank.sort(Comparator.comparing(Record::getScore).reversed());
        for(var record : rank){
            record.setRank(i++);
        }
    }

    public void writeRecord(String name, int score){
        try{
            // 打开文件
            File f = new File(System.getProperty("user.dir") + "\\rank.dat");
            if(!f.exists()){
                f.createNewFile();
            }

            // 读入原有数据
            if(f.length() != 0){
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
                this.rank = (ArrayList<Record>) ois.readObject();
                ois.close();
            }

            // 增加新的记录
            this.rank.add(addRecord(name, score));

            // 按分数排序
            sortByScoreDescending();

            // 写入文件
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(rank);
            oos.close();

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public Record addRecord(String name, int score) {
        return new Record(name, score);
    }

    @Override
    public void printRecord(String name, int score) {
        writeRecord(name, score);
        System.out.println("***************************************");
        System.out.println("                得分排行榜               ");
        System.out.println("***************************************");
        for(var record : rank){
            System.out.println(record);
        }
    }
}

package edu.hitsz.rank;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author henry
 */
public class RecordDaoImpl implements RecordDao{

    @Override
    public void addRecord(String name, int score, String difficulty) {
        try{
            ArrayList<Record> rank = new ArrayList<>();
            File f = openFile(difficulty);

            // 读入原有数据
            if(f.length() != 0){
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
                rank = (ArrayList<Record>) ois.readObject();
                ois.close();
            }
            else{
                f.createNewFile();
            }

            // 增加新的记录
            rank.add(new Record(name, score));

            // 按分数排序
            sortByScoreDescending(rank);

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
    public void deleteRecordByIndex(int index, String difficulty) {
        try{
            ArrayList<Record> rank;
            // 打开文件
            File f = openFile(difficulty);

            // 读取文件中的对象
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            rank = (ArrayList<Record>) ois.readObject();
            ois.close();

            // 删除记录并更新排名
            rank.remove(index);
            sortByScoreDescending(rank);

            // 写回文件
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(rank);
            oos.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void sortByScoreDescending(ArrayList<Record> rank){
        int i = 1;
        rank.sort(Comparator.comparing(Record::getScore).reversed());
        for(var record : rank){
            record.setRank(i++);
        }
    }

    private File openFile(String difficulty){
        try{
            File f;
            if("Easy".equals(difficulty)){
                f = new File(System.getProperty("user.dir") + "\\easy_rank.dat");
            }
            else if("Normal".equals(difficulty)){
                f = new File(System.getProperty("user.dir") + "\\normal_rank.dat");
            }
            else{
                f = new File(System.getProperty("user.dir") + "\\hard_rank.dat");
            }
            return f;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

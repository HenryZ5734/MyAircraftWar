package edu.hitsz.rank;

import java.io.File;
import java.util.ArrayList;

/**
 * @author henry
 */
public interface RecordDao {

    /**
     * 增加记录
     * @param name 玩家名字
     * @param score 玩家分数
     * @param difficulty 游戏难度
     * */
    void addRecord(String name, int score, String difficulty);

    /**
     * 删除记录
     * @param index 数据索引
     * @param difficulty 游戏难度
     * */
    void deleteRecordByIndex(int index, String difficulty);

    /**
     * 获取文件中的记录
     * @param file 要读取的文件
     */
    ArrayList<Record> getRecordsFromFile(File file);
}

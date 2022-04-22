package edu.hitsz.rank;

/**
 * @author henry
 */
public interface RecordDao {

    /**
     * 增加记录
     * @param name 玩家名字
     * @param score 玩家分数
     * @return 返回一个记录对象
     * */
    Record addRecord(String name, int score);

    /**
     * 将记录打印在控制台
     * @param name 玩家名字
     * @param score 玩家分数
     */
    void printRecord(String name, int score);

}

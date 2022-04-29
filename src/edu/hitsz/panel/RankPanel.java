package edu.hitsz.panel;

import edu.hitsz.rank.Record;
import edu.hitsz.rank.RecordDaoImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

/**
 * @author henry
 */
public class RankPanel {
    private JPanel mainPanel;
    private JButton deleteButton;
    private JTable rankTable;
    private JScrollPane rankScroll;
    private JLabel rankTitle;

    private final String difficulty;
    private DefaultTableModel model;

    public RankPanel(String difficulty) {
        this.difficulty = difficulty;
        file2Table(difficulty);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = rankTable.getSelectedRow();

                // 从文件中删去记录
                deleteRecordInFile(row, difficulty);

                // 更新表格模型
                file2Table(difficulty);

                // 刷新表格
                rankTable.repaint();
            }
        });
    }

    public void file2Table(String difficulty){
        try{
            ArrayList<Record> rank;
            String[] columnName = {"排名", "姓名", "分数", "时间"};
            String[][] tableData = new String[100][];
            // 打开文件
            File f = openFile(difficulty);

            // 读取文件中的对象
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            rank = (ArrayList<Record>) ois.readObject();
            ois.close();

            // 将List转化为Model
            for(var record : rank){
                tableData[record.getRank()-1] = new String[]{
                        String.valueOf(record.getRank()),
                        record.getName(),
                        String.valueOf(record.getScore()),
                        record.getTime()
                };
            }
            this.model = new DefaultTableModel(tableData, columnName){
                @Override
                public boolean isCellEditable(int row, int col){
                    return false;
                }
            };

            // 设置表格
            rankTable.setModel(this.model);
            rankScroll.setViewportView(rankTable);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void deleteRecordInFile(int row, String difficulty){
        RecordDaoImpl recordDao = new RecordDaoImpl();
        recordDao.deleteRecordByIndex(row, difficulty);
    }

    public void getUserName(int score){
        String name = JOptionPane.showInputDialog(
                mainPanel,
                "游戏结束，你的得分是" + String.valueOf(score) + "，请输入你的名字："
        );
        while(name.length() == 0){
            JOptionPane.showMessageDialog(
                mainPanel,
                "输入的名字不能为空，请重新输入"
            );
            name = JOptionPane.showInputDialog(
                    mainPanel,
                    "游戏结束，你的得分是" + String.valueOf(score) + "，请输入你的名字："
            );

        }
        RecordDaoImpl recordDao = new RecordDaoImpl();
        recordDao.addRecord(name, score, difficulty);
        file2Table(difficulty);
        rankTable.repaint();
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

    public JPanel getMainPanel() {
        return mainPanel;
    }
}

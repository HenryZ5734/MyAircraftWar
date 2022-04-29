package edu.hitsz.application;

import edu.hitsz.panel.RankPanel;
import edu.hitsz.panel.StartPanel;

import javax.swing.*;
import java.awt.*;

/**
 * 程序入口
 * @author hitsz
 */
public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;
    public static final int SMALL_WINDOW_WIDTH = 256;
    public static final int SMALL_WINDOW_HEIGHT = 384;
    public static final Object MAIN_LOCK = new Object();

    public static void main(String[] args) {

        System.out.println("Hello Aircraft War");

        // 获得屏幕的分辨率，初始化 Frame
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame("Aircraft War");
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);
        //设置窗口的大小和位置,居中放置
        frame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 开始窗口
        StartPanel startPanel = new StartPanel();
        JPanel mainPanel = startPanel.getMainPanel();
        frame.setContentPane(mainPanel);
        frame.setVisible(true);

        synchronized(MAIN_LOCK){
            while(mainPanel.isVisible()){
                try{
                    MAIN_LOCK.wait();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

        // 游戏窗口
        String difficulty = startPanel.getDifficulty();
        frame.remove(mainPanel);
        Game game = new Game();
        frame.setContentPane(game);
        frame.setVisible(true);
        game.action();

        synchronized(MAIN_LOCK){
            while(!game.isGameOverFlag()){
                try{
                    MAIN_LOCK.wait();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

        // 排行榜窗口
        RankPanel rankPanel = new RankPanel(difficulty);
        mainPanel = rankPanel.getMainPanel();
        frame.remove(game);
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
        try{
            rankPanel.getUserName(game.getScore());
        }
        catch(Exception e) {

        }
    }
}

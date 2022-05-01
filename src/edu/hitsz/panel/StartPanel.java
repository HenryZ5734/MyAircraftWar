package edu.hitsz.panel;

import edu.hitsz.application.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author henry
 */
public class StartPanel {
    private JButton easy;
    private JButton normal;
    private JButton hard;
    private JComboBox music;
    private JPanel mainPanel;
    private JLabel musicLabel;
    private String difficulty;
    private Boolean playMusic=true;

    public StartPanel() {

        easy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                difficulty = "Easy";
                mainPanel.setVisible(false);
                synchronized(Main.MAIN_LOCK) {
                    Main.MAIN_LOCK.notify();
                }
            }
        });
        normal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                difficulty = "Normal";
                mainPanel.setVisible(false);
                synchronized(Main.MAIN_LOCK) {
                    Main.MAIN_LOCK.notify();
                }
            }
        });
        hard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                difficulty = "Hard";
                mainPanel.setVisible(false);
                synchronized(Main.MAIN_LOCK) {
                    Main.MAIN_LOCK.notify();
                }
            }
        });
        music.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(music.getSelectedIndex() == 0){
                    playMusic = true;
                }
                else{
                    playMusic = false;
                }
            }
        });
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Boolean getPlayMusic() {
        return playMusic;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}

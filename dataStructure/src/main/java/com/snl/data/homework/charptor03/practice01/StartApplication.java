package com.snl.data.homework.charptor03.practice01;

import com.snl.data.homework.charptor03.practice01.event.KeyEventImplement;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import javax.swing.*;

public class StartApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            var frame = new JFrame("测试");

            InputState inputState = new InputState();
            KeyEventImplement keyEventImplement = new KeyEventImplement(inputState);
            var p = new BootPanel(inputState,frame);
            frame.add(p);
            frame.addKeyListener(keyEventImplement);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        });
    }
}

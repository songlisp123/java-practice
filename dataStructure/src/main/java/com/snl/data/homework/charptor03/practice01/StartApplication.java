package com.snl.data.homework.charptor03.practice01;

import com.snl.data.homework.charptor03.practice01.event.KeyEventImplement;
import com.snl.data.homework.charptor03.practice01.log.WindowHandler;
import com.snl.data.homework.charptor03.practice01.state.InputState;

import javax.swing.*;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class StartApplication {

    public static Formatter formatter = new SimpleFormatter();
    public static WindowHandler handler = new WindowHandler();
    public static Logger logger =  Logger.getLogger("game");

    static {
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
        handler.setLevel(Level.ALL);
        handler.setFormatter(formatter);
        logger.addHandler(handler);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(()->{
            var frame = new JFrame("测试");

            InputState inputState = new InputState();
            KeyEventImplement keyEventImplement = new KeyEventImplement(inputState);
            var p = new ShowPanel(inputState,frame);
            frame.add(p);
            frame.addKeyListener(keyEventImplement);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.pack();
            frame.setVisible(true);
        });
    }
}

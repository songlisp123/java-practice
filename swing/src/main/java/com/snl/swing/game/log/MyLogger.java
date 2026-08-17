package com.snl.swing.game.log;

import com.snl.swing.practice01.log.WindowHandler;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {

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
}

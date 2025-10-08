package wormsubject.game;

import wormsubject.util.MenuBar;
import wormsubject.util.WindowHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class practice01 extends SimpleFrame {
    private jpanel panel;
    private MenuBar menuBar;
    private static final Logger logger = Logger.getLogger("worms");
    private static final WindowHandler windowHandler = new WindowHandler();

    static {
        logger.setLevel(Level.ALL);
        logger.addHandler(windowHandler);
        logger.setUseParentHandlers(false);
    }
    public practice01() {
        super(800,800);
        panel = new jpanel(40,40,getWidth(),getHeight());

        menuBar =  new MenuBar();
        add(panel);
        setJMenuBar(menuBar);
        setVisible(true);
    }
}

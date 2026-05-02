package com.snl.test.animate;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class MovingButtonContainer extends JComponent implements ActionListener {

    Timer timer;                        // for later start/stop actions
    int animationDuration = 2000; 	// each animation will take 2 seconds
    long animStartTime;			// start time for each animation
    int translateY = 0;                 // current Y location of button
    static final int MAX_Y = 100;
    JButton button = null;
    private static final int DIVISIONS = 10;
    static final int CHECKER_SIZE = 60;

    /** Creates a new instance of TranslucentButton */
    public MovingButtonContainer() {
        setLayout(new java.awt.FlowLayout());
        timer = new Timer(30, this);
        button = new JButton("Start Animation");
        // Need setOpaque(false) to force Swing to paint the button's parent
        button.setOpaque(false);
        button.addActionListener(this);
        add(button);
    }

    /**
     * This method handles both button clicks, which start/stop the animation,
     * and Swing Timer events, which animate translateY.
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(button)) {
            // button click
            if (!timer.isRunning()) {
                animStartTime = System.nanoTime() / 1000000;
                button.setText("Stop Animation");
                timer.start();
            } else {
                timer.stop();
                button.setText("Start Animation");
                // reset translation to 0
                translateY = 0;
                // Must force repaint to get final animated position erased
                repaint();
            }
        } else {
            // Timer event
            // calculate elapsed fraction
            long currentTime = System.nanoTime() / 1000000;
            long totalTime = currentTime - animStartTime;
            if (totalTime > animationDuration) {
                animStartTime = currentTime;
            }
            float fraction = (float)totalTime / animationDuration;
            fraction = Math.min(1.0f, fraction);
            // This calculation will cause translateY to go from 0 to MAX_Y
            // as the fraction goes from 0 to 1
            if (fraction < .5f) {
                translateY = (int)(MAX_Y * (2 * fraction));
            } else {
                translateY = (int)(MAX_Y * (2 * (1 - fraction)));
            }
            // redisplay our container to paint the button in the new location
            repaint();
        }
    }

    /**
     * Paint our container with a checkerboard background. Then set a
     * translation factor on the Graphics object and call the superclass,
     * which will paint the children (e.g, our button) with this 
     * translation.
     */
    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        for (int stripeX = 0; stripeX < getWidth(); stripeX += CHECKER_SIZE) {
            for (int y = 0, row = 0; y < getHeight(); y += CHECKER_SIZE/2, ++row) {
                int x = (row % 2 == 0) ? stripeX : (stripeX + CHECKER_SIZE/2);
                g.fillRect(x, y, CHECKER_SIZE/2, CHECKER_SIZE/2);
            }
        }
        // Translate our graphics according to the animated translateY value
        g.translate(0, translateY);
        // Defer to superclass to handle painting for component, which includes
        // painting the children of this container
        super.paint(g);
    }

    private static void createAndShowGUI() {
        JFrame f = new JFrame("Moving Button Container");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(300, 300);
        JComponent buttonContainer = new MovingButtonContainer();
        f.add(buttonContainer);
        f.setVisible(true);
    }

    public static void main(String args[]) {
        Runnable doCreateAndShowGUI = new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        };
        SwingUtilities.invokeLater(doCreateAndShowGUI);
    }

}
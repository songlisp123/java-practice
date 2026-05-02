package com.snl.test.animate;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class FadingButton extends JButton implements ActionListener {

    float alpha = 1.0f;                 // current opacity of button
    Timer timer;                        // for later start/stop actions
    int animationDuration = 2000; 	// each animation will take 2 seconds
    long animationStartTime;            // start time for each animation
    BufferedImage buttonImage = null;

    /** Creates a new instance of FadingButton */
    public FadingButton(String label) {
        super(label);
        //这一步很重要？？
        setOpaque(false);
        //每隔30ms调用方法
        timer = new Timer(30, this);
        addActionListener(this);
    }

    public void paint(Graphics g) {
        // Create an image for the button graphics if necessary
        if (buttonImage == null || buttonImage.getWidth() != getWidth() ||
                buttonImage.getHeight() != getHeight()) {
            buttonImage = getGraphicsConfiguration().
                    createCompatibleImage(getWidth(), getHeight());
        }
        Graphics gButton = buttonImage.getGraphics();
        gButton.setClip(g.getClip());

        //  Have the superclass render the button for us
        super.paint(gButton);

        // Make the graphics object sent to this paint() method translucent
        Graphics2D g2d  = (Graphics2D)g;
        AlphaComposite newComposite =
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2d.setComposite(newComposite);

        // Copy the button's image to the destination graphics, translucently
        g2d.drawImage(buttonImage, 0, 0, null);
    }

    /**
     * This method receives both click events (which start and stop the
     * animation) and Timer events (which animate the alpha of the button).
     */
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource().equals(this)) {
            // button click
            if (!timer.isRunning()) {
                animationStartTime = System.nanoTime() / 1000000;
                this.setText("Stop Animation");
                timer.start();
            } else {
                timer.stop();
                this.setText("Start Animation");
                // reset alpha to opaque
                alpha = 1.0f;
            }
        } else {
            // timer event
            // calculate the elapsed fraction
            long currentTime = System.nanoTime() / 1000000;
            long totalTime = currentTime - animationStartTime;
            if (totalTime > animationDuration) {
                //循环播放
                animationStartTime = currentTime;
            }
            //
            float fraction = (float)totalTime / animationDuration;
            fraction = Math.min(1.0f, fraction);
            // This calculation will cause alpha to go from 1 to 0 and back to 1
            // as the fraction goes from 0 to 1
            alpha = Math.abs(1 - (2 * fraction));
            // redisplay our button with its new alpha value
            repaint();
        }
    }

    private static void createAndShowGUI() {
        JFrame f = new JFrame("Fading Button");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(300, 300);
        JPanel checkerboard = new Checkerboard();
        checkerboard.add(new FadingButton("Start Animation"));
        f.add(checkerboard);
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

    /**
     * Displays checkerboard background
     */
    private static class Checkerboard extends JPanel {
        private static final int DIVISIONS = 10;
        static final int CHECKER_SIZE = 60;
        public void paintComponent(Graphics g) {
            g.setColor(Color.white);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.BLACK);
            for (int stripeX = 0; stripeX < getWidth(); stripeX += CHECKER_SIZE) {
                for (int y = 0, row = 0; y < getHeight(); y += CHECKER_SIZE/2, ++row) {
                    int x = (row % 2 == 0) ? stripeX : (stripeX + CHECKER_SIZE/2);
                    g.fillRect(x, y, CHECKER_SIZE/2, CHECKER_SIZE/2);
                }
            }
        }
    }
}
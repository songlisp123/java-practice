package com.snl.test.animate;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class FadingButtonTF extends JButton implements ActionListener , TimingTarget {
    float alpha = 1.0f;                 // current opacity of button
    Animator animator;                  // for later start/stop actions
    int animationDuration = 2000; 	// each cycle will take 2 seconds
    BufferedImage buttonImage = null;

    /** Creates a new instance of FadingButtonTF */
    public FadingButtonTF(String label) {
        super(label);
        // ????
        setOpaque(false);
        animator = new Animator(animationDuration/2, Animator.INFINITE,
                Animator.RepeatBehavior.REVERSE, this);
        animator.setStartFraction(1.0f);
        animator.setStartDirection(Animator.Direction.BACKWARD);
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
     * This method receives click events, which start and stop the animation
     */
    public void actionPerformed(ActionEvent ae) {
        if (!animator.isRunning()) {
            this.setText("Stop Animation");
            animator.start();
        } else {
            animator.stop();
            this.setText("Start Animation");
            // reset alpha to opaque
            alpha = 1.0f;
        }
    }
    // Ununsed MouseListener implementations
    public void begin() {}
    public void end() {}
    public void repeat() {}

    /**
     * TimingTarget implementation: this method sets the alpha of our button
     * to be equal to the current elapsed fraction of the animation
     */
    public void timingEvent(float fraction) {
        alpha = fraction;
        // redisplay our cbutton
        repaint();
    }

    private static void createAndShowGUI() {
        JFrame f = new JFrame("Fading Button TF");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(300, 300);
        JPanel checkerboard = new Checkerboard();
        checkerboard.add(new FadingButtonTF("Start Animation"));
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

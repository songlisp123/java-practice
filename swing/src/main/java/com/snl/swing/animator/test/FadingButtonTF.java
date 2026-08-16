package com.snl.swing.animator.test;

import com.snl.swing.animator.Animator;
import com.snl.swing.animator.TimingTarget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class FadingButtonTF extends JButton implements ActionListener , TimingTarget {

    float alpha = 1.0f;
    Animator animator;
    int duration = 2000;
    BufferedImage buttonImage = null;
    int x,y;

    public FadingButtonTF(String text) {
        super(text);
        setOpaque(false);
        animator = new Animator(duration,this);
        addActionListener(this);
    }

    @Override
    public void begin() {

    }

    @Override
    public void end() {

    }

    @Override
    public void repeat() {

    }

    @Override
    public void timingEvent(double fraction) {
        alpha = (float) fraction;
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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

    @Override
    protected void paintComponent(Graphics g) {

        // Create an image for the button graphics if necessary
        if (buttonImage == null || buttonImage.getWidth() != getWidth() ||
                buttonImage.getHeight() != getHeight()) {
            buttonImage = getGraphicsConfiguration().
                    createCompatibleImage(getWidth(), getHeight());
        }
        Graphics gButton = buttonImage.getGraphics();
        gButton.setClip(g.getClip());

        //  Have the superclass render the button for us

        // Make the graphics object sent to this paint() method translucent
        Graphics2D g2d  = (Graphics2D)g;
        AlphaComposite newComposite =
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g2d.setComposite(newComposite);

        // Copy the button's image to the destination graphics, translucently
        g2d.drawImage(buttonImage, 0, 0, null);
        super.paintComponent(g);
    }


    private static void createAndShowGUI() {
        JFrame f = new JFrame("Fading Button TF");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(300, 300);
        f.getContentPane().add(new FadingButtonTF("测试"),BorderLayout.PAGE_END);
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

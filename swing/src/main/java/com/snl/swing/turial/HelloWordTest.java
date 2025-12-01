package com.snl.swing.turial;

import audio.MutipleMixer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class HelloWordTest {

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame.setDefaultLookAndFeelDecorated(false);
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new MyWindow(frame));
        //Add the ubiquitous "Hello World" label.
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label, BorderLayout.CENTER);

        //Display the window.
        frame.setSize(600,400);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static class MyWindow implements WindowListener {

        private final JFrame jFrame;

        public MyWindow(JFrame jFrame) {
            this.jFrame = jFrame;
        }

        @Override
        public void windowOpened(WindowEvent e) {
            System.out.println("e = " + e);
            System.out.println("窗口已打开");
            try {
                MutipleMixer.main(null);
            } catch (InterruptedException ex) {
                System.err.println("发生错误，错误原因:"+ex.getMessage());
                ex.printStackTrace();
            }
        }

        @Override
        public void windowClosing(WindowEvent e) {
            jFrame.setVisible(false);
            System.out.println("窗口已经隐藏");
        }

        @Override
        public void windowClosed(WindowEvent e) {
            System.out.println("程序结束");
            System.exit(0);
        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowDeiconified(WindowEvent e) {

        }

        @Override
        public void windowActivated(WindowEvent e) {

        }

        @Override
        public void windowDeactivated(WindowEvent e) {

        }
    }
}

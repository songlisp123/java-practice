package com.snl.test;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class AbstactPanelDemo extends JPanel {

    protected Object[] objects;



    public AbstactPanelDemo() {
        super(new BorderLayout());
        JColorChooser jColorChooser = new JColorChooser(Color.ORANGE);
        add(jColorChooser);
        objects = jColorChooser.getChooserPanels();
        Arrays.stream(objects).forEach(System.out::println);
        jColorChooser.setChooserPanels(null);
//        Arrays.stream(objects).forEach(System.out::println);
    }



    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ColorChooserDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new AbstactPanelDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
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
}

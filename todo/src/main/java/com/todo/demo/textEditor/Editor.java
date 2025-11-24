package com.todo.demo.textEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Editor {
    public static Runnable task() {
        return ()-> {
            EventQueue.invokeLater(() -> {
                var frame = new JFrame();
                var menuBar = new MenuBar(frame);
                var dialog = new AboutDialog(frame);
                var terminator = new Terminate(dialog);
                var panel = new panel(new JTextArea());
                frame.setSize(600, 500);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.addWindowListener(terminator);
//            frame.add(panel,BorderLayout.NORTH);
                frame.setVisible(true);
                frame.setJMenuBar(menuBar);

            });
        };

    }

    public static void play() {
        new Thread(task(),"后台线程").start();
    }
    private static class Terminate extends WindowAdapter {
        private AboutDialog aboutDialog;
        public Terminate(AboutDialog aboutDialog) {
            super();
            this.aboutDialog = aboutDialog;
        }

        @Override
        public void windowClosing(WindowEvent e) {
            aboutDialog.setVisible(true);
            }
    }


}

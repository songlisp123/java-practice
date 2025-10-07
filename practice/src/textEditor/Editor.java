package textEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Editor {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new JFrame();
            var menuBar = new MenuBar(frame);
            var dialog = new AboutDialog(frame);
            var terminator = new Terminator(dialog);
            var panel = new panel(new JTextArea());
            frame.setSize(600,500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.addWindowListener(terminator);
//            frame.add(panel,BorderLayout.NORTH);
            frame.setVisible(true);
            frame.setJMenuBar(menuBar);

        });
    }

    private static class Terminator extends WindowAdapter {
//        private JFrame jFrame;
        private AboutDialog aboutDialog;
        public Terminator(AboutDialog aboutDialog) {
            super();
//            this.jFrame = jFrame;
            this.aboutDialog = aboutDialog;
        }

        @Override
        public void windowClosing(WindowEvent e) {
            aboutDialog.setVisible(true);
            }
        }

}

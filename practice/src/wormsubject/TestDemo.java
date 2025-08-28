package wormsubject;

import javax.swing.*;
import java.awt.*;

public class
TestDemo {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new SimpleFrame(1200,800);
            var pabel = new initPanel(frame);
            frame.add(pabel,BorderLayout.CENTER);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setVisible(true);
        });
    }
}

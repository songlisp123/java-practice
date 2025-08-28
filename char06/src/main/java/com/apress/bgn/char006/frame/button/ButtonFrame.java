package com.apress.bgn.char006.frame.button;

import com.apress.bgn.char006.frame.picture.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonFrame extends JFrame {

    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_HEIGHT =100;
    private JPanel panel;

    public ButtonFrame() {
        this.panel = new JPanel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int x = (screenWidth - DEFAULT_WIDTH) / 2;
        int y = (screenHeight - DEFAULT_HEIGHT) / 2;
        this.setBounds(x, y, DEFAULT_WIDTH, DEFAULT_WIDTH);

        //自造按钮
        var button = new JButton();
        makeButton("跳出");
        makeButton("天出");
        makeButton("蓝色");
        makeButton("绿色");
        add(panel,BorderLayout.CENTER);
        add(new JButton("lan"),BorderLayout.SOUTH);

    }

    public void makeButton(String name) {
        var button = new JButton(name);
        button.addActionListener(new Mylistener());
        panel.add(button);
    }

    private class Mylistener implements ActionListener {

        public Mylistener() {
        }
        public void actionPerformed(ActionEvent event) {
            var com = new JPanel();
            com.setBounds(( DEFAULT_WIDTH) / 2,
                    getY(),100,100);
            com.setBackground(Color.RED);
            var com2 = new JPanel();
            com2.setBounds(100,
                    100,100,100);
            com2.setBackground(Color.CYAN);
            panel.add(com);
            panel.add(com2);
            repaint();
        }
    }

}

class test {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new ButtonFrame();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setTitle("你好世界！");
            frame.setResizable(true);
            frame.setVisible(true);
        });
    }
}



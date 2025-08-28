package com.apress.bgn.char006.frame.ordinary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonFrame extends JFrame {
    private JPanel buttonPanel;
//    private static final int DEFAULT_WIDTH = 300;
//    private static final int DEFAULT_HEIGHT = 200;

    public ButtonFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit()
                .getScreenSize();

        int screenwidth = screenSize.width;
        int screenheight = screenSize.height;
        var DEFAULT_WIDTH = screenwidth / 2;
        var DEFAULT_HEIGHT = screenheight / 2;
        this.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        this.setTitle("这是一个测试！");

        //创建按钮
        var yellowButton = new JButton("黄色");
        var greenButton = new JButton("绿色");
        var blueButton = new JButton("蓝色");
        buttonPanel = new JPanel();
        //居中面板
        var d = buttonPanel.getBounds();
//        super.add(new )
        int x = (DEFAULT_WIDTH - (int)d.getWidth()) / 2;
        int y = (DEFAULT_HEIGHT - (int)d.getHeight()) / 2;
        buttonPanel.setLocation(x,y);
        //添加按钮
        buttonPanel.add(yellowButton);
        buttonPanel.add(greenButton);
        buttonPanel.add(blueButton);

        //将面板添加到窗体
        add(buttonPanel);
//        buttonPanel.setLocation(x,y);
        //为每一个按钮绑定时间监听
        yellowButton.addActionListener(new ColorAction(Color.YELLOW));
        greenButton.addActionListener(new ColorAction(Color.GREEN));
        blueButton.addActionListener(new ColorAction(Color.blue));
    }
    private class ColorAction implements ActionListener {

        private Color backGroundColor;

        public ColorAction(Color c) {
            backGroundColor = c;
        }

        public void actionPerformed(ActionEvent event) {
            buttonPanel.setBackground(backGroundColor);
        }
    }
}


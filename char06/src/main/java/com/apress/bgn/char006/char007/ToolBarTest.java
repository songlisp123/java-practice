package com.apress.bgn.char006.char007;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ToolBarTest extends SimpleFrame{
    private JToolBar toolBar;
    public ToolBarTest() {
        super();
        toolBar = new JToolBar("工具栏");
        toolBar.addSeparator();
        JButton button = (JButton) toolBar.add(new JButton("1"));
        button.addActionListener(event->{
            System.out.println(1);
        });
        button.setToolTipText("一个简单的打印出1的动作");
        JButton button02 = (JButton) toolBar.add(new JButton("2"));
        button02.addActionListener(event-> {
            System.out.println(2);
        });

        JLabel label = (JLabel) toolBar.add(new JLabel("测试"));
        toolBar.add(new myaction());
        this.add(toolBar,BorderLayout.NORTH);
    }

    private class myaction extends AbstractAction {
        public myaction() {
            putValue(NAME,"黄色");
            putValue("yellow",Color.yellow);
            putValue(SHORT_DESCRIPTION,"简短描述:你是我儿!");
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            Color c = (Color) super.getValue("yellow");
            toolBar.setBackground(c);
            repaint();
        }
    }
}

class test05 {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new ToolBarTest();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setVisible(true);
        });
    }
}

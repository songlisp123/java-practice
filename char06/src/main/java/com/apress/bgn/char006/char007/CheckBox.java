package com.apress.bgn.char006.char007;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

public class CheckBox extends SimpleFrame {
    private JLabel label;
    private JCheckBox bold;
    private JCheckBox italic;
    private static final int FONTSIZE = 24;

    public CheckBox() {
        super();
//        this.setBounds(x,y,DEFAULT_WIDTH,DEFAULT_HEIGHT);
        //添加实例标签
        label = new JLabel("你好，世界！",SwingConstants.CENTER);
//        Rectangle2D d = label.getBounds();
//
//        int x = (int) (DEFAULT_WIDTH-d.getWidth()) /2;
//        System.out.println(x);
//        int y = (int) (DEFAULT_HEIGHT-d.getHeight()) /2;
//        System.out.println(y);
//        label.setFont(new Font("Serif",Font.BOLD,FONTSIZE));
//        label.setLocation(x,y);
        add(label);
//        label.setLocation(x,y);

        //这个监听器设置字体属性
        //复选框状态的标签

        ActionListener listener = (event) -> {
            int mode = 0;
            if (bold.isSelected()) mode += Font.BOLD;
            if (italic.isSelected()) mode += Font.ITALIC;
            label.setFont(new Font("Serif",mode,FONTSIZE));
        };

        //添加检查箱
        var buttonPanel = new JPanel();

        bold = new JCheckBox("Bold");
        bold.addActionListener(listener);
        bold.setSelected(true);
        buttonPanel.add(bold);

        italic = new JCheckBox("Italic");
        italic.addActionListener(listener);
        buttonPanel.add(italic);

        add(buttonPanel,BorderLayout.SOUTH);
    }

}

class test {
    public static void main(String[] args) {
        var frame = new CheckBox();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);
    }
}

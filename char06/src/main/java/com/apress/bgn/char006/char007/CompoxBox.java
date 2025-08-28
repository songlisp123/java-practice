package com.apress.bgn.char006.char007;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CompoxBox extends SimpleFrame {
    private JComboBox<String> comboBox;
    private JComboBox<String> acomboBox;
    private JLabel label;
    private static final int DEFAULT_SIZE = 24;
    private JPanel jPanel;
    public CompoxBox() {
        super();
        comboBox = new JComboBox<>();
        acomboBox = new JComboBox<>();
        jPanel = new JPanel(new GridLayout(1,2));
        label = new JLabel("你好,世界!",SwingConstants.CENTER);
        label.setFont(new Font("Serif",Font.BOLD,DEFAULT_SIZE));
        add(label);

        //第二步骤:这是一个设计优化
        //创建一个 组合下拉框架并添加可选项目
        comboBox.addItem("Serif");
        comboBox.addItem("SansSerif");
        comboBox.addItem("Monospaced");
        comboBox.addItem("Dialog");
        comboBox.addItem("DialogInput");
        acomboBox.addItem("Serif");
        acomboBox.addItem("SansSerif");
        acomboBox.addItem("Monospaced");
        acomboBox.addItem("Dialog");
        acomboBox.addItem("DialogInput");

        //设置组建的可编辑属性
        /*
        可编辑属性:你可以编辑下拉框中的项目,但是不建议
        默认不可编辑,如果你不知道自己在干什么 ,请不要修改这个参数
         */
//        acomboBox.setEditable(true);
//        System.out.println(comboBox.isEditable());

        //下一步骤:创建一个监听器对象
        ActionListener listener = event -> {
            label.setFont(
                    new Font(comboBox.getItemAt(comboBox.getSelectedIndex()),
                            Font.PLAIN,DEFAULT_SIZE));
        };

        comboBox.addActionListener(listener);
        acomboBox.addActionListener(listener);

        //将组合选择框放置在父级面板上
        jPanel.add(comboBox);
        jPanel.add(acomboBox);
        //将父级面板防在顶级框架上
        add(jPanel,BorderLayout.NORTH);

    }
}

class test02 {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new CompoxBox();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setVisible(true);
        });
    }
}

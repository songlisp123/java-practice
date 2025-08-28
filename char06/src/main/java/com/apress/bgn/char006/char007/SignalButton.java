package com.apress.bgn.char006.char007;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

public class SignalButton extends SimpleFrame {
    private ButtonGroup group;
    private JPanel jPanel;
    private JPanel panelButtom;
    private JPanel panelTop;
    private JLabel label;
    private JCheckBox boldButton;
    private JCheckBox iButton;
    private static final int DEFAULT_SIZE = 36;
    //设置边框
    private Border borderButtom;


    public SignalButton() {
        super();
        label = new JLabel("你好，世界！",SwingConstants.CENTER);
        label.setFont(new Font("Serif",Font.BOLD,DEFAULT_SIZE));
        group = new ButtonGroup();
        jPanel = new JPanel(new GridLayout(1,2));
        panelButtom = new JPanel();
        panelTop = new JPanel();
        boldButton = new JCheckBox("粗体",false);
        iButton = new JCheckBox("斜体",false);
        //设置边框--一个直线边框
        borderButtom = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
//        borderButtom = BorderFactory.createBevelBorder(1,Color.CYAN,Color.GREEN);


//        borderButtom = BorderFactory.createLineBorder(Color.black);
//        borderButtom = BorderFactory.createTitledBorder("多选框");

//        borderButtom = BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("AS"),
//                BorderFactory.createDashedBorde
//                r(Color.CYAN));
        //设置另一个边框
        var borderTop = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        //添加按钮
        addButton("small",8);
        addButton("medium",16);
        addButton("large",36);
        addButton("extra large",72);
        panelButtom.setBorder(borderButtom);

//        设置监听器对象
//        ActionListener listener = (event) -> {
//            int mode = 0;
//            if (boldButton.isSelected()) mode += Font.BOLD;
//            if (iButton.isSelected()) mode += Font.ITALIC;
//            label.setFont(new Font("Serif",mode,DEFAULT_SIZE));
//        };

//
        //安装监听器
//        boldButton.addActionListener(listener);
//        iButton.addActionListener(listener);

        //将多选框添加到另一个面板上
        panelTop.add(boldButton);
        panelTop.add(iButton);

        //设置边框
        panelTop.setBorder(borderTop);

        //在顶级窗口上添加各组件
        add(label);
        //在父级面板上添加面板
        jPanel.add(panelTop);
        jPanel.add(panelButtom);

        //将父级面板添加到顶级框架中

        add(jPanel,BorderLayout.SOUTH);

    }

    public void addButton(String name,int size) {
        boolean selected = (size == DEFAULT_SIZE);
        var button = new JRadioButton(name,selected);
        group.add(button);
        panelButtom.add(button);
        ActionListener listener = event ->{
            int mode = 0;
            if (boldButton.isSelected()) mode += Font.BOLD;
            if (iButton.isSelected()) mode += Font.ITALIC;

            label.setFont(new Font("Serif",mode,size));
        };
        boldButton.addActionListener(listener);
        iButton.addActionListener(listener);
        button.addActionListener(listener);
    }

}

class test01 {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new SignalButton();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setVisible(true);
        });
    }
}

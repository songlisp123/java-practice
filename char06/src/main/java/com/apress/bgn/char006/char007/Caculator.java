package com.apress.bgn.char006.char007;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Objects;

public class Caculator extends SimpleFrame  {
    private JPanel jPanel;
    private JTextArea area;
    private JButton button;
    private JMenuBar menuBar;
    private GridLayout gridLayout;

    public Caculator() {
        super();
        //新建所需元素:
        /*
        1.一个父级面板存放按钮
        2.一些数字按钮
        3.一个文本区域表示输入内容
         */

        jPanel = new JPanel();
        jPanel.setBackground(new Color(32,33,32));
        jPanel.setFocusable(false);
        area = new JTextArea(4,60);
        area.setBackground(new Color(32, 32, 32));
        area.setForeground(new Color(255,255,255));
        area.setLineWrap(true);

        menuBar = new JMenuBar();
        /*
        一个具有四行六列的网格布局管理器
         */
        gridLayout = new GridLayout(6,4);
        //设置网格布局组件的间距
        /*
        setVgap:设置组件间的垂直距离
        setHgap:设置组件间的水平距离
        setLayout:设置布局管理器,默认三种:
            1.GridLayout--网格布局管理
            2.Flowlayout--留布局管理
            3.BoradLayout--边框 布局(这是顶级框架默认的布局方式)

         注意:此程序在添加组件前操作
         */
        gridLayout.setVgap(3);
        gridLayout.setHgap(3);
        jPanel.setLayout(gridLayout);
        /*
        一些装饰性的框架,非必要
         */

        /*
        菜单栏顶级框架
        1.添加子项目
        2.在子项目中继续添加子项目
         */

        //添加子项目(子项目类型为JMenu)
        var selectMenu = new JMenu("计算选项");
        var helpMenu = new JMenu("帮助");

        //添加子项目(子项目类型为 :JMenuItem)
        /*
        add:将字符串 或者图像作为子项目添加到末尾,返回
        刚刚添加的子项目
        方便后续添加事件监听器,最后一个例子 用来设置一个退出程序的事件监听器
         */
        selectMenu.add("四则运算");
        selectMenu.add("科学计算").setEnabled(false);

        helpMenu.add("帮助");
        helpMenu.add("文档");
        helpMenu.add("论坛");
        helpMenu.add("更多信息");
        helpMenu.add("退出").addActionListener(event->{
            System.exit(0);
        });

        //将父项目添加到面板上
        /*
        将
         */
        menuBar.add(selectMenu);
        menuBar.add(helpMenu);

        /*
        一些基础的组建框架:
        createTitledBorder:一个带有文本的组件框架,可用来提示信息
        createEtchedBorder:一个蚀刻框架,用来美化界面
        setTitleFont:设置文本家族,样式,大小
        setTitlePosition:设置文本的位置
        setTitleColor:设置文本的颜色
         */
        var boader = BorderFactory.createTitledBorder("计算区域");
        boader.setTitleFont(new Font("宋体",Font.BOLD,12));
        boader.setTitlePosition(TitledBorder.ABOVE_BOTTOM);
        boader.setTitleColor(new Color(255,255,255));
        var keyboard = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

        /*
        一些数字按钮和基本的四则运算
         */
        makeButtons("%");
        makeButtons("CE");
        makeButtons("C");
        makeButtons("x");
        makeButtons("1/x");
        makeButtons("x^2");
        makeButtons("x");
        makeButtons("➗");
        makeButtons("7");
        makeButtons("8");
        makeButtons("9");
        makeButtons("X");
        makeButtons("4");
        makeButtons("5");
        makeButtons("6");
        makeButtons("-");
        makeButtons("1");
        makeButtons("2");
        makeButtons("3");
        makeButtons("+");
        makeButtons("+/-");
        makeButtons("0");
        makeButtons(".");
        makeButtons("=");

        //为面板设置 框架
        /*
        setBorder:设置框架
         */
        area.setBorder(boader);
        jPanel.setBorder(keyboard);

        //将面板安装在顶级框架中
        /*
        第二个例子中的BorderLayout.NORTH是一个常量,代表着
        框架的上部分
        框架默认布局管理:

         */
        add(jPanel);
        add(area,BorderLayout.NORTH);
        /*
        setJMenuBar:将菜单栏设置在窗口的上边
         */
        setJMenuBar(menuBar);

    }
    private void makeButtons(String name) {

        var button = new CustomButton(name);

        //添加监听器
        ActionListener listener = event -> {
            var buttonSelected = (JButton) event.getSource();
            area.append("" + buttonSelected.getText());
        };

        button.addActionListener(listener);

        //将按钮添加到面板上
        jPanel.add(button);

    }
}

class test13 {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new Caculator();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setVisible(true);
        });
    }
}
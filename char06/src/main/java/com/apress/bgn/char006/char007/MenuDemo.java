package com.apress.bgn.char006.char007;

import javax.swing.*;
import java.awt.*;

public class MenuDemo extends SimpleFrame {
    private JMenuBar menuBar;
    public MenuDemo() {
        super();
        //创建一个顶级菜单栏

        menuBar = new JMenuBar();

        //添加父级项目

        var editMenu = new JMenu("编辑");
        var FileMenu = new JMenu("文件");
        var helpMenu = new JMenu("帮助");

        //将项目添加到父级菜单

        menuBar.add(editMenu);
        menuBar.add(FileMenu);
        menuBar.add(helpMenu);

        //添加子集项目

        var subItem = new JMenuItem("复制",
                new ImageIcon(".//icons8-复制-50.png"));
        subItem.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        var subItem02 = new JMenuItem("剪切",
                new ImageIcon(".//icons8-剪刀-50.png"));
        subItem02.setAccelerator(KeyStroke.getKeyStroke("ctrl X"));
        var subItem03 = new JMenuItem("保存",
                new ImageIcon(".//icons8-保存-50.png"));
        subItem03.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        subItem03.setEnabled(false);
        var subItem04 = new JMenuItem("删除",
                new ImageIcon(".//icons8-删除-50.png"));
        var subItem05 = new JMenuItem("备份",
                new ImageIcon(".//icons8-数据备份-50.png"));
        var subItem06 = new JMenuItem("另存为",
                new ImageIcon(".//icons8-另存为-50.png"));
        subItem06.setAccelerator(KeyStroke.getKeyStroke("ctrl Alt S"));
        var subItem07 = new JMenuItem("新建",
                new ImageIcon(".//icons8-创建新的-50.png"));
        subItem07.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
        var subItem08 = new JMenuItem("打开",
                new ImageIcon(".//icons8-打开文件夹-50.png"));
        subItem08.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        var subItem09 = new JMenuItem("文档",
                new ImageIcon(".//icons8-谷歌文档-32.png"));
        var subItem10 = new JMenuItem("论坛",
                new ImageIcon(".//icons8-论坛-50.png"));
        var subItem11 = new JMenuItem("更多信息",
                new ImageIcon(".//icons8-帮助-50.png"));

        var subItem12 = new JMenuItem("可选操作");

        //为子集项目设置监听器对象

        subItem.addActionListener(event->{
            System.out.println(1);
        });

        //这是一个可选的选项,统一操作文档与表情的方向
//        subItem.setHorizontalTextPosition(SwingConstants.LEFT);

        //将子项目添加到父级项目

        editMenu.add(subItem);
        editMenu.addSeparator();
        editMenu.add(subItem02);
        editMenu.addSeparator();
        editMenu.add(subItem03);
        editMenu.addSeparator();
        editMenu.add(subItem04);
        editMenu.addSeparator();
        editMenu.add(subItem05);
//        editMenu.addSeparator();
        editMenu.add(subItem06);

        FileMenu.add(subItem07);
        FileMenu.addSeparator();
        FileMenu.add(subItem08);

        helpMenu.add(subItem09);
        helpMenu.addSeparator();
        helpMenu.add(subItem10);
        helpMenu.addSeparator();
        helpMenu.add(subItem11);
        helpMenu.addSeparator();
        helpMenu.add(subItem12);

        //删除不需要的菜单选项

        editMenu.remove(subItem06);

        //一个可选的操作,设置为单选按钮和多选按钮
        var group = new ButtonGroup();
        var read = new JRadioButtonMenuItem("只读",true);
        var write = new JRadioButtonMenuItem("写",false);
        group.add(read);
        group.add(write);
        subItem12.add(read);
        subItem12.add(write);

        //将此菜单栏设置在窗口顶部
        setJMenuBar(menuBar);
    }
}

class test10 {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new MenuDemo();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setVisible(true);
        });
    }
}
package com.apress.bgn.char006.char007;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Menu extends SimpleFrame {
    private JMenuBar menu;
    public  Menu() {
        super();
        menu = new JMenuBar();
//        super.setJMenuBar(menu);
        var editMenu = new JMenu("编辑");
        var fileMenu = new JMenu("文件");
        var quitMenu = new JMenu("退出");
        //为顶级菜单对象设置子对象
        var pasteItem = new JMenuItem(new myAction());
        //将顶级菜单选项添加到菜单栏
        menu.add(editMenu);
        menu.add(fileMenu);
        menu.add(quitMenu);
        //将子菜单选项添加到父级菜单上
        editMenu.add(pasteItem);
        //一种方便的方法设置菜单选项
        JMenuItem item = editMenu.add("zhantie");
        item.addActionListener(event->{
            System.out.println(2);
        });
        var quitItem = quitMenu.add("立即退出");
        quitItem.addActionListener(event->{
            System.exit(0);
        });
        var closeItem = quitMenu.add("关闭");
        closeItem.addActionListener(event->{
            System.out.println(3);
        });
        editMenu.addSeparator();


        //将顶级菜单条设置在顶层框架上
        super.setJMenuBar(menu);
    }

    private class myAction extends AbstractAction {

        public myAction() {
            putValue(NAME,"复制");
            putValue(SHORT_DESCRIPTION,"编辑文件");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(1);
        }
    }
}

class test04 {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new Menu();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setVisible(true);
        });
    }
}
package com.snl.swing.practice;

import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class CustomMenuBar extends JMenuBar implements ActionListener {

    protected final String FILEMENU = "文件";
    protected final String EDITMENU = "编辑";
    protected final String EXITMENU = "退出";
    protected final String STYLEMENU = "风格";

    //预定义几个选项卡文件?
    //1,文件
    //2.编辑
    //3.退出
    protected JMenu fileMenu;
    protected JMenu editMenu;
    protected JMenu exitMenu;
    protected JMenu setting;
    protected JMenu styleMenu;

    public CustomMenuBar() {
        initComponents();
    }

    private void initComponents() {
        //初始化菜单组件
        //TODO 添加文件
        fileMenu = new JMenu(FILEMENU);
        //TODO 添加编辑文件
        editMenu = new JMenu(EDITMENU);
        exitMenu = new JMenu(EXITMENU);
        setting = new JMenu("设置");
        styleMenu = new JMenu(STYLEMENU);
        //TODO 添加监听器
        JMenuItem helpItem = new JMenuItem("帮助");
        //TODO 添加监听器
        JMenuItem forumItem = new JMenuItem("论坛");
        JMenuItem quitMenu = new JMenuItem("退出");
        quitMenu.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
        quitMenu.addActionListener(this::quitPro);
        helpItem.addActionListener(this);
        exitMenu.add(helpItem);
        exitMenu.add(forumItem);
        exitMenu.addSeparator();
        exitMenu.add(quitMenu);


        Action action = new StyledEditorKit.BoldAction();
        action.putValue(Action.NAME,"粗体");
        JMenuItem jMenuItem = new JMenuItem(action);
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK));
        styleMenu.add(jMenuItem);

        action = new StyledEditorKit.ItalicAction();
        action.putValue(Action.NAME,"斜体");
        JMenuItem jMenuItem1 = new JMenuItem(action);
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
        styleMenu.add(jMenuItem1);

        action = new StyledEditorKit.UnderlineAction();
        action.putValue(Action.NAME,"下划线");
        JMenuItem jMenuItem2 = new JMenuItem(action);
        jMenuItem.setAccelerator(KeyStroke.getKeyStroke("CTRL SHIT I"));
        styleMenu.add(jMenuItem2);

        add(fileMenu);
        add(editMenu);
        add(exitMenu);
        add(styleMenu);
        add(Box.createHorizontalGlue());
        add(setting);
        add(new JMenu("    "));

    }

    protected <T extends AWTEvent> void quitPro(T t) {
        System.exit(0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) e.getSource();
        System.out.println("source = " + source);
    }
}

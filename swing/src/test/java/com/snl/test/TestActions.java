package com.snl.test;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TestActions {
    public static void main(String[] args) {
        JTextPane textPane = new JTextPane();
        textPane.setText("""
                打开任何一款卫星导航地图，将准心定位于广东省东莞市黄江镇。
                沿着灵狮小镇那条年久失修的颠簸公路，走上三公里左右。就来到了我现在正在上班的位置。
                为了保密，也是为了我的安全。我可不能如此轻松地说出我供职的那家公司。
                鉴于我做的这件事多少有点惊世骇俗，以至于我现在还在东躲西藏，密切地注视着“那条河”的新闻。
                （你很快就会明白的），有了这样的前提，我低调行事也就顺理成章。毕竟，谦逊有礼、温
                """);
        Action[] actions = textPane.getActions();
        Arrays.stream(actions).forEach(System.out::println);
        List<Action> list = Arrays.stream(actions).
                filter(TestActions::getAction).toList();
        JMenuBar jMenuBar = new JMenuBar();
        JMenu edit = new JMenu("编辑");
        list.forEach(a->{
            a.getValue(Action.NAME);
            edit.add(new JMenuItem(a));
        });
        jMenuBar.add(edit);

        JFrame frame = new JFrame("测试框架");
        frame.getContentPane().add(textPane, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setJMenuBar(jMenuBar);
        //创新导航栏

    }

    protected static boolean getAction(Action action) {
        String value = (String) action.getValue(Action.NAME);
        if (DefaultEditorKit.beepAction.equals(value)) {
            return true;
        }
        if (DefaultEditorKit.cutAction.equals(value)) {
            return true;
        }
        if (DefaultEditorKit.pasteAction.equals(value)) {
            return true;
        }
        if (DefaultEditorKit.copyAction.equals(value)) {
            return true;
        }
        if (DefaultEditorKit.selectAllAction.equals(value)) {
            return true;
        }
        return false;
    }
}

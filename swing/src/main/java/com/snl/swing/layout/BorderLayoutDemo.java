package com.snl.swing.layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class BorderLayoutDemo {

    private static final boolean RIGHT_TO_LEFT = false;

    protected static <T extends Container> void addComponentsToPane(T t) {
        if (!(t.getLayout() instanceof BorderLayout)) {
            t.add(new JLabel("该容器并没有使用BorderLayout布局管理器!"));
            return;
        }
        if (RIGHT_TO_LEFT) {
            t.setComponentOrientation(
                    ComponentOrientation.RIGHT_TO_LEFT
            );
        }
        BorderLayout layout = (BorderLayout) t.getLayout();
        layout.setHgap(5);
        layout.setVgap(5);
        t.setLayout(layout);
        JButton button_1 = new JButton("第一个按钮");
        t.add(button_1,BorderLayout.PAGE_START);

        JButton button_2 = new JButton("第二个按钮");
        button_2.setPreferredSize(new Dimension(300,250));
        t.add(button_2,BorderLayout.CENTER);

        JButton button_3 = new JButton("第三个按钮");
        t.add(button_3,BorderLayout.LINE_START);

        JButton button_4 = new JButton("第四个按钮");
        t.add(button_4,BorderLayout.LINE_END);

        JButton button_5 = new JButton("第五个按钮");
        t.add(button_5,BorderLayout.PAGE_END);

    }

    private static void createUi() {
        JFrame frame = new JFrame("测试border布局管理");
        frame.setFocusable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(createListener());
        addComponentsToPane(frame.getContentPane());
        frame.setBounds(200,200,600,400);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            EventQueue.invokeLater(BorderLayoutDemo::createUi);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

    }

    private static WindowListener createListener() {
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("正在关闭窗口......");
            }

            @Override
            public void windowOpened(WindowEvent e) {
                System.out.println("打开窗口......");
            }

            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("窗口已关闭");
                System.out.println("程序退出");
                System.exit(0);
            }
        };
    }

}

package com.snl.swing.layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


public class GridBagLayoutDemo {

    private static final boolean shouldFill = true;
    private static final boolean shouldWeightX = true;
    private static final boolean RIGHT_TO_LEFT = false;

    protected static <T extends Container> void addComponentsToPane(T t) {
        //初始化容器
        if (!(t.getLayout() instanceof GridBagLayout)) {
            GridBagLayout gridBagLayout = new GridBagLayout();
            t.setLayout(gridBagLayout);
        }else {
            System.out.println("✅ 改容器是由布局管理器管理的");
        }


        //添加组件
        JButton button_1 = new JButton("第一个按钮");
        GridBagConstraints constraints_1 = createConstaints();
        constraints_1.gridx = 0;
        constraints_1.gridy = 0;
        constraints_1.gridwidth = 1;
        t.add(button_1,constraints_1);
        JButton button_2 = new JButton("按钮2");
        GridBagConstraints c_2 = createConstaints();
        c_2.gridx = 1;
        c_2.gridy = 0;
        c_2.gridwidth = GridBagConstraints.RELATIVE;

        t.add(button_2,c_2);

        JButton button_3 = new JButton("第三个按钮");
        GridBagConstraints c_3 = createConstaints();
        c_3.gridx = 2;
        c_3.gridy = 0;
        c_3.gridwidth = GridBagConstraints.REMAINDER;

        t.add(button_3,c_3);

        JButton button_4 = new JButton("第四个按钮");
        GridBagConstraints c_4 = createConstaints();
        c_4.gridy = 1;
        c_4.gridx = 0;
        c_4.gridwidth = 3;
        c_4.ipady = 100;
        c_4.weightx = 0.0;
        t.add(button_4,c_4);

        JButton button_5 = new JButton("第五个按钮");
        GridBagConstraints c_5 = createConstaints();
        c_5.gridy = 2;
        c_5.gridx = 1;
        c_5.weighty = 1.0;
        c_5.anchor = GridBagConstraints.PAGE_END;
        c_5.insets = new Insets(10,0,0,0);
        c_5.gridwidth = 2;
        t.add(button_5,c_5);



    }

    private static GridBagConstraints createConstaints() {
        GridBagConstraints constraints = new GridBagConstraints();
        if (shouldFill) {
            constraints.fill = GridBagConstraints.HORIZONTAL;
        }
        if (shouldWeightX) {
            constraints.weightx = 0.5;
        }
        return constraints;
    }


    private static void createUi() {
        JFrame frame = new JFrame("测试GridBag布局管理器");
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
            EventQueue.invokeLater(GridBagLayoutDemo::createUi);
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

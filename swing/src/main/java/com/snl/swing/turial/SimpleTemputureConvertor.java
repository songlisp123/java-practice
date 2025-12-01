package com.snl.swing.turial;

import javax.swing.*;
import java.awt.*;

public class SimpleTemputureConvertor extends JFrame {

    //设置属性
    private JLabel celsiusLabel;
    private JLabel fahrenheit;
    private JButton convertButton;
    private JTextField inputTxt;
    private JPanel panel;


    public SimpleTemputureConvertor() {
        initComponents();
    }

    private void initComponents() {
        setSize(600,400);
        celsiusLabel = new JLabel("摄氏温度");
        fahrenheit = new JLabel("华氏温度");
        convertButton = new JButton("转换");
        convertButton.addActionListener(event->{
            int tem = (int) (Double.parseDouble(inputTxt.getText())*1.8 + 32);
            fahrenheit.setText(tem + "华氏温度");
        });
        inputTxt = new JTextField("请输入xxxx");
        panel = new JPanel();
        this.add(panel);
        //设置布局管理
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(inputTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(celsiusLabel))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(convertButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(fahrenheit)))
                                .addContainerGap(27, Short.MAX_VALUE))
        );
        layout.linkSize(SwingConstants.HORIZONTAL, convertButton,inputTxt);
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(inputTxt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(celsiusLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(convertButton)
                                        .addComponent(fahrenheit))
                                .addContainerGap(21, Short.MAX_VALUE))
        );
        setTitle("温度装换器");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            try {
                UIManager.setLookAndFeel(
                        "com.sun.java.swing.plaf.motif.MotifLookAndFeel"
                );
            } catch (UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            new SimpleTemputureConvertor().setVisible(true);
//            String systemLookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
//            System.out.println("当前系统外观："+systemLookAndFeelClassName);

        });
    }


}

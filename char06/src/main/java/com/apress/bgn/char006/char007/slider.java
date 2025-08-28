package com.apress.bgn.char006.char007;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Hashtable;

public class slider extends  SimpleFrame {
    private JSlider slider;
    private JSlider aslider;
    private JPanel panel;
    private JPanel apanel;
    private JPanel bpanel;
    private Border border;
    private Hashtable<Integer,Component> hashtable;
    private ChangeListener listener;
    private JTextField textField;

    public slider() {
        super();
        slider = new JSlider(SwingConstants.HORIZONTAL,100,0);
        hashtable = new Hashtable<>();
        textField = new JTextField();
        //为您的刻度线设置自定义图形
        hashtable.put(0,new JLabel("0"));
        hashtable.put(20,new JLabel("20"));
        hashtable.put(40,new JLabel("40"));
        hashtable.put(60,new JLabel("60"));
        hashtable.put(80,new JLabel("80"));
        hashtable.put(100,new JLabel("100"));
        /*
        设置以下属性:
        每20个单位设置一个打标签
        每五个单位设置一个小标签
        最后调通setPaintTicks来显示刻度
        setSnapToTicks:强制滑块吸附到刻度线
        setPaintLabels:绘制刻度线的🈯️,默认是你设置的字典
        setPaintTrack:设置您的轨道,默认是真,表示拥有轨道
        否,表示没有轨道
        setMajorTickSpacing:设置大刻度,与你设置的值有关
        setMinorTickSpacing:设置小蝌蚪 ,与你 设置的值有关
         */
        slider.setMajorTickSpacing(20);
        slider.setMinorTickSpacing(5);
        slider.setLabelTable(hashtable);
        slider.setPaintTrack(false);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        slider.setPaintLabels(true);

        //创建一个事件更改监听器
        listener = event->{
            JSlider source = (JSlider) event.getSource();
            System.out.println(source.getValue());
            textField.setText(""+source.getValue());
        };

        slider.addChangeListener(listener);
        //创建一个边框
        border = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                "上边框");
        //创建另一个边框
        aslider = new JSlider(SwingConstants.VERTICAL);
        //创建 父级面板
        panel = new JPanel();
        apanel = new JPanel();
        bpanel = new JPanel();
        //将滑块添加到父级容器内
        panel.add(slider);
        apanel.add(aslider);
        bpanel.add(textField,FlowLayout.LEFT);
        //设置父级容器的边框
        panel.setBorder(border);
        apanel.setBorder(border);
        bpanel.setBorder(border);
        add(panel,BorderLayout.NORTH);
        add(apanel,BorderLayout.EAST);
        add(bpanel,BorderLayout.SOUTH);
        /*
        以下是 另一个不同方向的滑块
         */

        //创建另一个滑块

        //将父级容器添加到顶级窗口中

    }
}

class test03 {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new slider();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setVisible(true);
        });
    }
}
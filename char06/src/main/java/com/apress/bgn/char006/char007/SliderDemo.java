package com.apress.bgn.char006.char007;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Hashtable;

public class SliderDemo extends SimpleFrame{
     //顶级面板
    private JSlider slider;
    private JPanel panel;
    private Border border;
    private Hashtable<Integer,Component> hashtable;
    private Hashtable<Integer,Component> hash;
    private Hashtable<Integer,Component> hashtable02;
    private static final String path = ".";

    public SliderDemo() {
        super();

        //第一个平平无奇的滑块

        slider = new JSlider();
        panel = new JPanel();
        border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        panel.add(slider);
        panel.setBorder(border);
        hashtable = new Hashtable<>();
        hash = new Hashtable<>();
        hashtable02 = new Hashtable<>();

        //第二个滑块,带有刻度线标签和
        var slider2 = new JSlider();
        slider2.setPaintTicks(true);
        slider2.setMajorTickSpacing(20);
        slider2.setMinorTickSpacing(5);
        hashtable.put(0,new JLabel("0"));
        hashtable.put(20,new JLabel("20"));
        hashtable.put(40,new JLabel("40"));
        hashtable.put(60,new JLabel("60"));
        hashtable.put(80,new JLabel("80"));
        hashtable.put(100,new JLabel("100"));

        slider2.setLabelTable(hashtable);
        slider2.setPaintLabels(true);
        slider2.setSnapToTicks(true);
        panel.add(slider2);
        //第三个滑块,带有英文标签
        var slider3 = new JSlider();
        hash.put(0,new JLabel("A"));
        hash.put(20,new JLabel("B"));
        hash.put(40,new JLabel("C"));
        hash.put(60,new JLabel("D"));
        hash.put(80,new JLabel("E"));
        hash.put(100,new JLabel("F"));

        slider3.setPaintTicks(true);
        slider3.setMajorTickSpacing(20);
        slider3.setMajorTickSpacing(5);
        slider3.setLabelTable(hash);
        slider3.setPaintLabels(true);
        slider3.setSnapToTicks(true);
        panel.add(slider3);

        //第四个滑块,带有图片标签
        hashtable02.put(0,new JLabel(new ImageIcon(".//icons8-梅花j-50.png")));
        hashtable02.put(20,new JLabel(new ImageIcon(".//icons8-红心j-50.png")));
        hashtable02.put(40,new JLabel(new ImageIcon(".//icons8-黑桃2-50.png")));
        hashtable02.put(60,new JLabel(new ImageIcon(".//icons8-方块10-50.png")));
        hashtable02.put(80,new JLabel(new ImageIcon(".//icons8-方块10-50.png")));
        hashtable02.put(100,new JLabel(new ImageIcon(".//icons8-方块10-50.png")));
//        hashtable02.put(20,new JLabel(new ImageIcon(".//icons8-红心j-50.png")));
        var slider4 = new JSlider();
        slider4.setMajorTickSpacing(20);
//        slider4.setMinorTickSpacing(5);
        slider4.setPaintTicks(true);
        slider4.setLabelTable(hashtable02);
        slider4.setPaintLabels(true);
        slider4.setSnapToTicks(true);
        panel.add(slider4);

        //第五个滑块,未有导轨
        var slider5 = new JSlider();
        slider5.setPaintTicks(true);
        slider5.setMajorTickSpacing(20);
        slider5.setMinorTickSpacing(5);
        slider5.setPaintTrack(false);
        slider5.setLabelTable(hashtable);
        slider5.setPaintLabels(true);
        slider5.setSnapToTicks(true);

        panel.add(slider5);
        add(panel);
    }
}

class test09 {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new SliderDemo();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(true);
            frame.setVisible(true);

        });
    }
}

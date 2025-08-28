package com.apress.bgn.char006.intefaces;

import java.awt.*;
import java.awt.event.*;
import java.time.*;
import javax.swing.*;

public class HuiDiao {
    public static void main(String[] args) {
        var listener = new TimerPrinter();
        var timer = new Timer(1500,listener);
        timer.start();
        JOptionPane.showMessageDialog(null,"退出程序！");
        System.exit(0);
    }
}


class TimerPrinter implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent event) {
        System.out.println("当前时间是："
                +LocalDateTime.now());
        System.out.println(event);
        Toolkit.getDefaultToolkit().beep();
    }
}


//以上的程序代表着回调
/*
该程序的逻辑是：
我们首先创建一个类继承事件监听接口，然后实现了actionPerformed方法
在HuiDiao类中，创建了一个listener监听其对象和定时器对象
定时器对象接受两个参数，一个延迟时间（默认为毫秒），表示当时间经过给定的时间后，会自动调用
监听器的actionPerformed方法，该方法打印出当前时间，并产生翁鸣。
 */
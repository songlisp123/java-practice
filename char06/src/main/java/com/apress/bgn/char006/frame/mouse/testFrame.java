package com.apress.bgn.char006.frame.mouse;

public class testFrame {
    public static void main(String[] args) {
        var frame = new SimpleFrame();
        frame.setTitle("这是一个测试！");
        frame.setResizable(true);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

package com.apress.bgn.char006.frame.ordinary;

import javax.swing.*;
import java.awt.*;

public class frame {
    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new JFrame();
//            Dimension screenSize = Toolkit.getDefaultToolkit()
//                    .getScreenSize();
//            int screenWidth = screenSize.width;
//            int screenHeight = screenSize.height;
//            frame.setSize(screenWidth/2,screenHeight/2);
            //添加组件
//            compoent.setSize(20,20);
            frame.setSize(500,400);
            frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
            frame.setVisible(true);

//            //添加组件
//            var compoent = new JPanel();
//            frame.add(compoent);
        });
    }
}

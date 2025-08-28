package com.apress.bgn.char006.char007;

import javax.swing.*;
import java.awt.*;
import java.awt.desktop.QuitEvent;
import java.util.logging.Logger;

public class TestCompent extends JFrame {
    public static final int ROW = 8;
    public static final int COLUMNS = 20;

     public TestCompent() {
         var testFileds = new JTextField();
         var password = new JPasswordField();
         password.setEchoChar('*');
         var northPanel = new JPanel();
         northPanel.setLayout(new GridLayout(2,2));
         northPanel.add(new JLabel("<html><b>用户名：</b></html>",SwingConstants.RIGHT));
         northPanel.add(testFileds);
         northPanel.add(new JLabel("密码：",SwingConstants.RIGHT));
         northPanel.add(password);
         this.add(northPanel,BorderLayout.NORTH);

         var textArea = new JTextArea(ROW,COLUMNS);
         var scrollPanle = new JScrollPane(textArea);
         add(scrollPanle,SwingConstants.CENTER);

         var southPanle = new JPanel();
         var insertButton = new JButton("Insert");
         insertButton.addActionListener(event->{
             textArea.append("用户名："+testFileds.getText()+"\t"+
                     "密码:"+new String(password.getPassword())+"\n");
         });
         southPanle.add(insertButton);
         add(southPanle,BorderLayout.SOUTH);
         pack();
     }

    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            var frame = new TestCompent();
            frame.setSize(new Dimension(520,520));
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.setResizable(true);
        });
    }
}

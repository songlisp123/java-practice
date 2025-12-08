package com.snl.test.filechooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SimpleJFileChooserDemo extends JFrame implements ActionListener {

    private JButton button;
    private JFileChooser chooser;
    private JLabel label;

    public SimpleJFileChooserDemo()  {

    }

    public SimpleJFileChooserDemo(String title)  {
        super(title);
        button = new JButton("弹窗demo");
        button.addActionListener(this);

        label = new JLabel("默认文本",JLabel.CENTER);
        label.setForeground(Color.YELLOW);

        chooser = new JFileChooser(".");
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        getContentPane().setBackground(Color.BLACK);

        getContentPane().add(button, BorderLayout.PAGE_START);
        getContentPane().add(label,BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            if (selectedFile.exists()) {
                if (selectedFile.isDirectory()) {
                    System.out.println("挑选的是文件夹");
                } else {
                    System.out.println("挑选的是文件");
                    System.out.println("selectedFile = " + selectedFile);
                }
                label.setText("挑选的是："+selectedFile);
            }else {
                label.setText("很抱歉，你挑选的文件不存在");
            }

        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(()->new SimpleJFileChooserDemo("这是一个测试"));
    }


}

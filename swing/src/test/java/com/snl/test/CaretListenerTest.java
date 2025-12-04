package com.snl.test;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class CaretListenerTest extends JLabel implements CaretListener {

    private static  JTextArea area;

    public CaretListenerTest(String text) {
        super(text);
        initComponents();
    }

    private void initComponents() {
        //初始化组件
        area = new JTextArea(5,30);
        area.addCaretListener(this);
    }

    @Override
    public void caretUpdate(CaretEvent e) {
//            System.out.println("光标更新");
        displaySelectionInfo(e.getDot(),e.getMark());
    }

    private void displaySelectionInfo(int dot, int mark) {
        SwingUtilities.invokeLater(()->{
            if (dot == mark) {
                //未选择内容
                try {
                    Rectangle2D rectangle2D = area.modelToView2D(dot);
                    //TODO 将当前光标定位到视图坐标 [✅ 完成]
                    setText("光标：文本位置：" + dot + ",视图位置：[" +
                            rectangle2D.getX() + ", " + rectangle2D.getY() +
                            "]" );
                } catch (BadLocationException e) {
                    System.err.println("发生错误……"+e.getMessage());
                    setText("光标：文本位置："+dot );
                }
            }else if(dot < mark) {
                //从前向后选
                setText("选择范围：[" +dot + ", "+ mark + "]" );
            } else {
                //从后向前选
                setText("选择范围：[" +mark + ", "+ dot + "]" );
            }
        });
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试框架");
        JPanel jPanel = new JPanel(new BorderLayout());
        CaretListenerTest listenerTest = new CaretListenerTest("测试光标监听器");
        jPanel.add(area,BorderLayout.CENTER);
        jPanel.add(listenerTest,BorderLayout.PAGE_END);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(200,50,300,300);
        frame.add(jPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(CaretListenerTest::createUi);
    }
}

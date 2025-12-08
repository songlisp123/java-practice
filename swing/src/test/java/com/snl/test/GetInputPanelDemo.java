package com.snl.test;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GetInputPanelDemo extends JDialog implements PropertyChangeListener {

    protected String answer;

    public GetInputPanelDemo() {
        super();
        //有限几个选项
//        answer = (String) JOptionPane.showInputDialog(
//                this,
//                "这是正文内容",
//                "输入与否？",
//                JOptionPane.QUESTION_MESSAGE,
//                null,
//                new Object[]{
//                        "1","2","3"
//                },
//                "1"
//        );
        answer = (String) JOptionPane.showInputDialog(
                this,
                "这是正文内容",
                "输入与否？",
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null
        );
        setLocation(new Point(200,50));
        pack();
        setVisible(true);

        handlerResults();
    }

    private void handlerResults() {
        if (answer != null) {
            System.out.println("answer = " + answer);
            System.exit(0);
        }else {
            System.err.println("异常！");
            System.exit(-1);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //TODO 你好
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(GetInputPanelDemo::new);
    }
}

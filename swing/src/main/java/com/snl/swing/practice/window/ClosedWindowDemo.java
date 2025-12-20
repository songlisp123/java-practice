package com.snl.swing.practice.window;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ClosedWindowDemo extends JDialog implements PropertyChangeListener, WindowListener {

    protected JOptionPane optionPane;
    protected Object[] options;

    public ClosedWindowDemo(JFrame frame) {
        super(frame);
        initComponents();
        this.add(optionPane);
        setVisible(false);
        pack();
    }

    private void initComponents() {
        options = new Object[] {
                "是的",
                "不，绝对不要"
        };
        optionPane = new JOptionPane(
                "你想要退出吗",
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,//默认试图
                options,//按钮文本选项
                options[1]);//默认焦点处于哪一个按钮上面？
        optionPane.addPropertyChangeListener(this);
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String newValue = evt.getNewValue().toString();
        //获取操作
        var source =(JOptionPane) evt.getSource();
        Object value1 = source.getValue();
        System.out.println("value1 = " + value1);
        if (options[0].equals(newValue)) {
            System.exit(0);
        }
        else {
            setVisible(false);
        }

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        this.setVisible(true);
    }

    @Override
    public void windowClosed(WindowEvent e) {
        System.out.println("程序结束");
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}

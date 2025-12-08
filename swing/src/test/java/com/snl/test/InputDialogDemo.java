package com.snl.test;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class InputDialogDemo extends JDialog implements PropertyChangeListener {

    private JOptionPane pane;
    private final String[] buttonString = {"不","是"};

    public InputDialogDemo() {
        super();
        pane = new JOptionPane(
                "这是一个主要内容区域",
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION,
                null,
                buttonString,
                buttonString[0]
        );
        pane.addPropertyChangeListener(this);
        getContentPane().add(pane, BorderLayout.CENTER);
        pack();
        setLocation(new Point(200,50));
        setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        System.out.println("propertyName = " + propertyName);

        if (this.isVisible() && (evt.getSource() == pane)
                && (JOptionPane.VALUE_PROPERTY.equals(propertyName) ||
                JOptionPane.INPUT_VALUE_PROPERTY.equals(propertyName))) {
            Object newValue = evt.getNewValue();
            Object value = pane.getValue();
            System.out.println(newValue==value);
            System.out.println("value = " + value);
            if (buttonString[0].equals(value)) {
                //不处理逻辑
            }else {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            InputDialogDemo inputDialogDemo = new InputDialogDemo();
        });
    }
}

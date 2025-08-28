package com.apress.bgn.char006.char007;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Model {
    public static void main(String[] args) {


        var button = new JButton(new myClass());
        ButtonModel model = button.getModel();
        System.out.println(model.toString());
        var UI = model.getActionCommand();
        System.out.println(UI);
    }
    private static class myClass extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(1);
        }
    }
}

package com.snl.data.tree;

import com.snl.swing.practice.button.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TreePanel extends JPanel implements ActionListener {

    private personGroup thePersonGroup;

    private JTextField tf = new JTextField(5);

    private JButton fillButton = new CustomButton("Fill");
    private JButton findButton = new CustomButton("Find");
    private JButton insButton  = new CustomButton("Ins");
    private JButton travButton = new CustomButton("Trav");
    private JButton delButton  = new CustomButton("Del");

    private boolean isNumber;
    private int GPNumber;

    public TreePanel() {
        setLayout(new FlowLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(fillButton);
        buttonPanel.add(findButton);
        buttonPanel.add(insButton);
        buttonPanel.add(travButton);
        buttonPanel.add(delButton);

        add(buttonPanel);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        inputPanel.add(new JLabel("输入数字: "));
        inputPanel.add(tf);
        add(inputPanel);

        fillButton.addActionListener(this);
        findButton.addActionListener(this);
        insButton.addActionListener(this);
        travButton.addActionListener(this);
        delButton.addActionListener(this);

        thePersonGroup = new personGroup();
        thePersonGroup.doFill(20);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        thePersonGroup.draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        isNumber = true;
        try {
            GPNumber = Integer.parseInt(tf.getText());
        } catch (NumberFormatException ex) {
            GPNumber = 0;
            isNumber = false;
        }

        thePersonGroup.setDrawAll(true);

        Object src = e.getSource();

        if (src == fillButton) {
            thePersonGroup.fill(isNumber, GPNumber);
        } else if (src == findButton) {
            thePersonGroup.find(isNumber, GPNumber);
        } else if (src == insButton) {
            thePersonGroup.insert(isNumber, GPNumber);
        } else if (src == travButton) {
            thePersonGroup.traverse();
        } else if (src == delButton) {
            thePersonGroup.remove(isNumber, GPNumber);
        }
        repaint();
        beep();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 300);
    }

    private void beep() {
        Toolkit.getDefaultToolkit().beep();
    }

}

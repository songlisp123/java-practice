package com.snl.data.homework.charptor01;

import com.snl.swing.practice.button.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Problem06 extends JPanel {
    protected JButton startButton;
    protected JLabel label;
    protected JTextField textField;
    protected JLabel resultLabel;

    public Problem06() {
        super(new BorderLayout());
        setBackground(Color.black);
        textField = new JTextField(15);
        label = new JLabel("请输入一个字符串:");
        label.setLabelFor(textField);
        label.setForeground(Color.YELLOW);
        resultLabel = new JLabel("测试文本",JLabel.CENTER);
        resultLabel.setForeground(Color.GREEN);
        startButton = new CustomButton("运行");
        startButton.addActionListener(new ActionImplement());
        alignSpace();
    }

    private void alignSpace() {
        LayoutManager layout = getLayout();
        if (!(layout instanceof GridBagLayout)) {
            layout = new GridBagLayout();
            setLayout(layout);
        }
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.weighty = 0.1f;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(10,5,10,5);
        constraints.anchor = GridBagConstraints.PAGE_START;
        add(label,constraints);

        constraints.gridx = 1;
        add(textField,constraints);

        constraints.gridx = 2;
        constraints.weightx = 0.3f;
        constraints.gridwidth = GridBagConstraints.HORIZONTAL;
        add(startButton,constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weighty = 0.8f;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        add(resultLabel,constraints);

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(700,500);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.green);
        g2.scale(1.25,1.25);
        g2.drawString("来自java数据结构第一章第六题该要求是:",30,50);
        g2.drawString("输入一个字符串,将会打印出改字符串的所有可能排序",30,75);
        g2.dispose();
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var problem = new Problem06();
        frame.add(problem);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Problem06::createUi);
    }

    class ActionImplement implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String text = textField.getText();
            if (text.isBlank()) {
                System.err.println("请输入正确的格式");
                return;
            }
            //输出数据
            permute(text);
        }
    }

    public void permute(String text) {
        return;
    }

    private void permute(char[] str,int low,int high) {

    }
}

package com.snl.data.homework.charptor01;

import com.snl.swing.practice.button.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Problem03 extends JPanel {
    protected JTextField textField;
    protected JLabel label;
    protected JLabel resultTable;
    protected JButton start;
    protected double number;
    protected final String result = "结果是：";
    protected String text = "";

    public Problem03() {
        super(new BorderLayout());
        init();
    }

    private void init() {
        textField = new JTextField(15);
        label = new JLabel("请输入一个数字:");
        label.setLabelFor(textField);

        resultTable = new JLabel("测试作用",JLabel.CENTER);
        start = new CustomButton("运行");
        start.addActionListener(new MyActionListenerImplement());
        alignSpace();
    }

    private void alignSpace() {
        LayoutManager layout = getLayout();
        if (!(layout instanceof GridBagLayout)) {
            layout = new GridBagLayout();
            setLayout(layout);
        }

        var constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0.1f;
        add(label,constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.5f;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(textField,constraints);

        constraints.gridx = 2;
        constraints.weightx = 0.3f;
        add(start,constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1.0f;
        constraints.gridheight = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        add(resultTable,constraints);
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var problem03 = new Problem03();
        frame.add(problem03);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Problem03::createUi);
    }

    class MyActionListenerImplement implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String text = textField.getText();
            number = Double.parseDouble(text);
            long integerPart = (long) number;
            printIntegerPart(integerPart);
            //获取小数部分？
            var little = number - integerPart;
            if (little != 0) {
                printLittle(little);
            }
            resultTable.setText(result + text);
        }

        private void printLittle(double little) {
            text += little;
        }

        private void printIntegerPart(long integerPart) {
            if (integerPart > 10)
                printIntegerPart(integerPart / 10);
            text += (integerPart % 10);
        }
    }


}

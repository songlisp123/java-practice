package com.snl.data.homework.charptor01;

import com.snl.swing.practice.button.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Problem05 extends JPanel {
    protected JButton startButton;
    protected JButton convertButton;
    protected JLabel result;
    protected JLabel textLabel;
    protected JTextField textField;
    /**
     * 查询所给定的数字的二进制表示中含有的一
     */
    protected int count;
    protected String binaryString = "";
    /**
     * 默认情况在微-1表示无事发生，否则为0的时候计算，为1的时候转换
     */
    protected int stateCheck = -1;

    public Problem05() {
        super(new BorderLayout());
        init();
    }

    public Problem05(LayoutManager layout) {
        super(layout);
        init();
    }

    private void init() {
        setBackground(Color.black);
        startButton = new CustomButton("运行");
        startButton.addActionListener(e -> {
            stateCheck = 0;
            action();
        });
        startButton.setToolTipText("运行程序,找到该数字的二进制中1的数量");

        convertButton = new CustomButton("转换");
        convertButton.addActionListener(e -> {
            stateCheck = 1;
            action();
        });
        convertButton.setToolTipText("将所给数字转换成二进制表示,默认不支持小数");

        textField = new JTextField(12);
        textLabel = new JLabel("请输入字段：");
        result = new JLabel("结果是：",JLabel.CENTER);
        result.setForeground(Color.GREEN);
        alignSpace();
    }

    private void alignSpace() {

        LayoutManager layout = getLayout();
        if (!(layout instanceof GridBagLayout)) {
            layout = new GridBagLayout();
            setLayout(layout);
        }

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.1f;
        constraints.weighty = 1.0f;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.insets = new Insets(10,5,10,5);
        constraints.fill = GridBagConstraints.NONE;
        add(textLabel,constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.5f;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(textField,constraints);

        constraints.gridx = 2;
        constraints.weightx = 0.5f;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(convertButton,constraints);

        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridx = 3;
        constraints.weightx = 0.2f;
        add(startButton,constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1.0f;
        constraints.weighty = 0.5f;
        constraints.anchor = GridBagConstraints.CENTER;
        add(result,constraints);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(700,500);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2 = (Graphics2D)g.create();

        g2.scale(1.25,1.25);
        g2.setColor(Color.green);
        g2.drawString("这是第五个练习题，要求是:找出给定的数字的二进制代码中含有的1的数量",20,75);
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var problem05 = new Problem05();
        frame.add(problem05);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Problem05::createUi);
    }


    public void action() {
        String text = textField.getText();
        if (Objects.isNull(text)) {
            System.err.println("参数为空！");
            return;
        }
        if (text.isBlank()) {
            System.err.println("参数为空！");
            return;
        }
        long anInt = Long.parseLong(text);
        if (stateCheck == 0) {
            //计算二进制表示中一的个数
            calculateNumberOf1(anInt);
            result.setText("一共含有 %d 个1".formatted(count));
            count = 0;
        } else if (stateCheck == 1) {
            //二进制转换
            calculateBinaryNumber(anInt);
            binaryString = reversedNumber(binaryString);
            result.setText("数字 %d 的二进制是 %s".formatted(anInt,binaryString));
            binaryString = "";
        }
    }

    private String reversedNumber(String binaryString) {
        StringBuilder s = new StringBuilder();
        int length = binaryString.length();
        for (int i=length -1;i>=0;i--) {
            s.append(binaryString.charAt(i));
        }
        return s.toString();
    }

    private void calculateBinaryNumber(long anInt) {
        if (anInt == 0 || anInt == 1) {
            binaryString += "1";
            return;
        }
        if (anInt % 2 == 0) {
            binaryString += "0";
        }else {
            binaryString += "1";
        }
        calculateBinaryNumber(anInt / 2);
    }

    private void calculateNumberOf1(long anInt) {
        if (anInt == 0) {
            return;
        }
        if (anInt == 1) {
            count++;
            return;
        }
        if (!((anInt % 2) == 0)) count++;
        calculateNumberOf1(anInt /2);
    }
    //补充练习，算出给定的十进制的二进制表达式

}

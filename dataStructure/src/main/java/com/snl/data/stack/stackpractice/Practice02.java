package com.snl.data.stack.stackpractice;

import com.snl.data.stack.ArrayStack;
import com.snl.swing.practice.button.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Practice02 extends JPanel {
    protected JButton run;
    private JLabel label;
    private JTextField field;
    private JLabel area;
    private Task task;

    public Practice02() {
        super(new BorderLayout());
        init();
    }

    private void init() {
        setBackground(Color.black);
        run = new CustomButton("运行");
        run.addActionListener(new ActionImplement());
        run.setToolTipText("运行程序将Infix表示法转换成Postfix后缀表示法");

        label = new JLabel("输入字段：",JLabel.CENTER);
        field = new JTextField(25);
        label.setLabelFor(field);
        label.setForeground(Color.YELLOW);

        area = new JLabel("测试",JLabel.CENTER);
        area.setForeground(Color.GREEN);

        alignSpace();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(700,600);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2 = (Graphics2D) g.create();

        g2.setColor(Color.green);
        g2.scale(1.25,1.25);
        g2.drawString("改题目是将算术中的中缀转换为计算机中的后缀表示法",10,50);
        g2.drawString("如果一个表达式是a + b * c + ( d * e + f ) * g",10,70);
        g2.drawString("那么其后缀形式是：a b c * + d e * f + g * +",10,90);
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
        constraints.weightx = 0.0f;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(10,50,10,5);
        constraints.anchor = GridBagConstraints.PAGE_START;
        add(label,constraints);

        constraints.gridx = 1;
        constraints.weightx = 0.0f;
        constraints.insets = new Insets(10,0,10,5);
        add(field,constraints);

        constraints.gridx = 2;
        constraints.weightx = 1.0f;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        add(run,constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weighty = 0.5f;
        constraints.weightx = 1.0f;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(10,50,10,50);

        add(area,constraints);
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var problem = new Practice02();
        frame.add(problem);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Practice02::createUi);
    }

    class ActionImplement implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String text = field.getText();
            if (text.isBlank()) {
                System.err.println("暂无输入");
                return;
            }

            if (task != null && !task.isDone()) {
                task.cancel(true);
            }
            task = new Task(text);
            task.execute();
        }
    }

    class Task extends SwingWorker<Void,Void> {
        private String s;
        private ArrayStack<Character> stack;
        private String result = "";

        public Task(String s) {
            this.s = s.strip();
            stack = new ArrayStack<>();
        }

        @Override
        protected Void doInBackground() throws Exception {
            //暴力破解
            char out;
            for (int i = 0;i<s.length();i++) {
                char c = s.charAt(i);
                if (c == '(') stack.push(c);
                else if (c == '+') {
                    while (!stack.isEmpty()) {
                        out = stack.pop();
                        if (out == '(')
                        {
                            stack.push(out);
                            break;
                        }
                        result += out;
                    }
                    stack.push(c);
                } else if (c == '*') {
                    stack.push(c);
                } else if (c == '/') {
                    stack.push(c);
                } else if (c == ')') {
                    while (!stack.isEmpty()) {
                        out = stack.pop();
                        if (out == '(') break;
                        result += out;
                    }
                } else if (c == ' '){
                    continue;
                }else {
                    result += c;
                }
            }
            while (!stack.isEmpty()) {
                out = stack.pop();
                result += out;
            }
            return null;
        }

        @Override
        protected void done() {
            Toolkit.getDefaultToolkit().beep();
            area.setText(result);
        }
    }

}

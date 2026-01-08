package com.snl.data.homework.charptor03;

import com.snl.swing.practice.button.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.random.RandomGenerator;

public class Problem06 extends JPanel {
    private JLabel label;
    private JTextField field;
    private JLabel label2;
    private JTextField field2;
    private JButton button;
    private JButton run;
    private int people;
    private final int WEIGHT = 30;
    private int xPos;
    private int origin;
    private  int M = 1;
    private Task task;
    /**
     * 我如何使用这个 属性？
     */
    private int transCount = 2;
    private int step = 0;
    private boolean[] dead;

    private long start;
    private long end;

    public Problem06() {
        super(new BorderLayout());
        init();
    }

    private void init() {
        xPos  = WEIGHT + WEIGHT / 2 - 2;
        origin = xPos;
        setBackground(Color.black);
        label = new JLabel("传递次数(M):");
        label.setForeground(Color.GREEN);
        field = new JTextField(10);

        label.setLabelFor(field);

        label2 = new JLabel("游戏总人数(N):");
        label2.setForeground(Color.GREEN);
        field2 = new JTextField(10);

        label2.setLabelFor(field2);

        button = new CustomButton("重置");
        button.setToolTipText("重置人数");
        button.addActionListener(e -> reset());

        run  = new CustomButton("运行");
        run.setToolTipText("运行游戏");
        run.addActionListener(new MyListenerImplement());
        run.setEnabled(false);

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
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0.0f;
        constraints.weighty = 0.2f;
        constraints.insets = new Insets(10,10,10,5);
        constraints.fill = GridBagConstraints.NONE;
        add(label,constraints);

        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.0f;
        add(field,constraints);

        constraints.gridx = 2;
        constraints.weightx = 0.0f;
        constraints.fill = GridBagConstraints.NONE;
        add(label2,constraints);

        constraints.gridx = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.0f;
        add(field2,constraints);

        constraints.gridx = 4;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.2f;
        add(button,constraints);

        constraints.gridx = 5;
        constraints.anchor = GridBagConstraints.NORTHEAST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.2f;
        add(run,constraints);

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(700,600);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2 = (Graphics2D) g.create();
        g2.setColor(Color.CYAN);
        g2.scale(1.25,1.25);
        g2.drawString("这是关于约瑟夫问题的一个题目",10,70);
        g2.drawString("编写一个程序来解决约瑟夫问题，其中 M 和 N 取一般值。",10,90);
        g2.drawString("你的程序的运行时间是多少？",10,110);
        if (people != 0) {
            int y = 0;
            int x;
            for (int i=0;i<people;i++) {
                x = i * WEIGHT + WEIGHT;
                y = getHeight() / 3 - WEIGHT / 2;
                g2.setColor(Color.CYAN);
                g2.drawRect(x, y, WEIGHT, WEIGHT);
                if (dead != null) {
                    if (dead[i]) {
                        g2.setColor(Color.red);
                        g2.drawString("❌", x + WEIGHT / 3, getHeight() / 3);
                    } else {
                        g2.setColor(Color.green);
                        g2.drawString(i + 1 + "", x + WEIGHT / 3, getHeight() / 3);
                    }
                }
            }

            g2.setColor(Color.YELLOW);
            g2.fillRect(xPos,y+WEIGHT,4,18);
        }
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

    class MyListenerImplement implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            task = new Task();
            task.execute();
            run.setEnabled(false);
        }
    }

    class Task extends SwingWorker<Void,Void> {

        /*
        对于M=0的特殊情况下，确实是对的，但是如何推广到一般情况下呢？
         */
        @Override
        protected Void doInBackground() throws Exception {
                repaint();
                var g = RandomGenerator.getDefault();
                /*
                第一种实现破产了，说实在话，这个实现在传递次数等于0的情况下可以运行，
                但是不具有普遍性，我需要另一种算法
                 */
//            while (true) {
//                if (task.isCancelled()) {
//                    return null;
//                }
//                if (dead[M-1] && dead[M]) {
//                    M++;
//                    if (M >= people ) {
//                        xPos = origin;
//                        M = 1;
//                    } else {
//                        xPos += WEIGHT;
//                    }
//                    continue;
//                }else {
//                    if (M >= people) {
//                        xPos = origin;
//                        M = 1;
//                        if (dead[M-1] && dead[M]) {
//                            M++;
//                            if (M >= people) {
//                                xPos = origin;
//                                M = 1;
//                            } else {
//                                xPos += WEIGHT;
//                            }
//                            continue;
//                        }
//                    } else {
//                        M++;
//                        xPos += WEIGHT;
//                    }
//                    dead[g.nextInt(0,M)] = true;
//                }
//                Thread.sleep(1000);
//                repaint();
//                if (isGamingOver()) break;
//            }
            //TODO 不会了，难受😣
            /**
             * 以下是推广到任意的MN的约瑟夫环
             */
            start = System.currentTimeMillis();
            int count = 0;
            while (!isGamingOver()) {
                if (task.isCancelled()) {
                    return null;
                }
                //传递
                if (!dead[M -1] && step == transCount) {
                    dead[M- 1] = true;
                    step = 0;
                }
                if (!dead[M -1]) {
                    step++;
                }
                moveNext();
                Thread.sleep(1000);
                repaint();
                count++;
            }
            end = System.currentTimeMillis();
            System.out.printf("一共耗时 %d ms%n",end - count * 1000L - start);
            return null;
        }

        @Override
        protected void done() {
            System.err.println("任务结束");
            run.setEnabled(true);
            beep();
        }
    }

    private void beep() {
        Toolkit.getDefaultToolkit().beep();
    }

    private boolean isGamingOver() {
        int count = 0;
        for (int i=0;i<people;i++) {
            boolean b = dead[i];
            if (!b) count++;
        }
        return count == 1;
    }

    private void moveNext() {
        if (M >= people) {
            M = 1;
            xPos = origin;
        }else {
            xPos += WEIGHT;
            M++;
        }
    }

    private void  reset() {
        String text = field.getText();
        if (text.isBlank()) {
            System.err.println("暂无输入");
            return;
        }
        transCount = Integer.parseInt(text);
        text = field2.getText();
        if (text.isBlank()) {
            System.err.println("暂无输入");
            return;
        }
        people = Integer.parseInt(text);
        dead = new boolean[people];
        Arrays.fill(dead,false);
        xPos = origin;
        M = 1;
        step = 0;
        if (task != null && !task.isDone()) task.cancel(true);
        run.setEnabled(true);
        repaint();
    }

}

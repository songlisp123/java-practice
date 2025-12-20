package com.snl.data.homework.charptor01.problem08;

import com.snl.swing.practice.button.CustomButton;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Problem08 extends JPanel {
    protected JButton startButton;
    protected JLabel label;
    protected JTextField textField;
    protected JLabel resultLabel;
    protected JComboBox comboBox;
    /**
     * 状态机制:-1表示无状态,0表示第一个以此类推
     */
    protected int stateCheck = -1;

    protected final int N = 100000000;

    /**
     * 程序将要进行的数字计算
     */
    protected Task task;

    public Problem08() {
        super(new BorderLayout());
        setBackground(Color.black);
        textField = new JTextField(15);
        label = new JLabel("请输入一个字符串:");
        label.setLabelFor(textField);
        label.setForeground(Color.YELLOW);
        resultLabel = new JLabel("测试文本",JLabel.CENTER);
        resultLabel.setForeground(Color.GREEN);
        startButton = new CustomButton("运行");
        startButton.setToolTipText("运行该程序,模拟出方程的解");
        startButton.addActionListener(e -> {
            if (task != null && !task.isDone()) {
                task.cancel(true);
                resultLabel.setText("取消任务!");
            }else {
                task = new Task();
                task.execute();
            }
        });

        comboBox = new JComboBox<>(new ComBoxModelTestDemo());

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
        constraints.weightx = 0.2f;
        add(comboBox,constraints);

        constraints.gridx = 3;
        constraints.weightx = 0.5f;
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
        g2.drawString("来自java数据结构第一章第八题该要求是:",30,115);
        g2.drawString("计算上面下拉框中各个方程值,对于i趋近于给定的数字来说",30,135);
        g2.dispose();
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var problem = new Problem08();
        frame.add(problem);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Problem08::createUi);
    }

    class ComBoxModelTestDemo implements ComboBoxModel<Icon> , ListDataListener {

        protected Icon currentItem;
        protected Icon oldItem;
        protected Icon[] data;
        protected final List<ListDataListener> listeners =
                new ArrayList<>();
        protected final Icon icon01 = new ImageIcon("icons/01.png");
        protected final Icon icon02 = new ImageIcon("icons/02.png");
        protected final Icon icon03 = new ImageIcon("icons/03.png");
        protected final Icon icon04 = new ImageIcon("icons/04.png");
        protected final Icon icon05 = new ImageIcon("icons/05.png");

        /**
         * 上一个索引,当尚未选择的时候为-1
         */
        protected int index0 = -1;
        /**
         * 当前所选物体的模型索引,初始化为-1
         */
        protected int index1 = -1;

        public ComBoxModelTestDemo() {
            data = new Icon[]{
                    icon01,icon02,icon03,icon04,icon05
            };
            addListDataListener(this);
            setSelectedItem(icon01);
        }

        @Override
        public void setSelectedItem(Object anItem) {
            oldItem = currentItem;
            if (oldItem != null)
                index0 = getIndex(oldItem);
            currentItem = (Icon) anItem;
            index1 = getIndex(currentItem);
            fireDateChanged();
        }

        private int getIndex(Icon item) {
            for(int i = 0;i<getSize();i++) {
                Icon datum = data[i];
                if (Objects.equals(datum,item)) return i;
            }
            throw new IndexOutOfBoundsException("不存在该文件");
        }

        private void fireDateChanged() {
            ListDataEvent listDataEvent =
                    new ListDataEvent(this,ListDataEvent.CONTENTS_CHANGED,index0,index1);
            for (ListDataListener listener : listeners) {
                listener.contentsChanged(listDataEvent);
            }
        }

        @Override
        public Object getSelectedItem() {
            return currentItem;
        }

        @Override
        public int getSize() {
            return data.length;
        }

        @Override
        public Icon getElementAt(int index) {
            if (index < 0 || index > getSize()) {
                throw new ArrayIndexOutOfBoundsException("超出索引边界");
            }
            return data[index];
        }

        @Override
        public void addListDataListener(ListDataListener l) {
            listeners.add(l);
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
            listeners.remove(l);
        }

        @Override
        public void intervalAdded(ListDataEvent e) {
            //空方法体
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            //空方法体
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            //主要逻辑
            System.out.println("数据模型变更");
            stateCheck = index1;
        }
    }

    class Task extends SwingWorker<Void,Double> {

        @Override
        protected Void doInBackground() throws Exception {
            if (stateCheck == 0) {
                System.out.println("选择第1个方程");
                //重点
                double result = 0;
                double tem;
                for (int i =0;i<=N;i++) {
                    if (task.isCancelled()) {
                        return null;
                    }
                    tem = 1;
                    for (int k=1;k<=i;k++) {
                        tem *= 1.0 /4;
                    }
                    result += tem;
                    publish(result);
                }
            } else if (stateCheck == 1) {
                double result = 0;
                double tem;
                for (int i=0;i<=N;i++) {
                    if (task.isCancelled()) {
                        return null;
                    }
                    if (i == 0) {
                        continue;
                    }
                    tem = 1.0;
                    for (int k=1;k<=i;k++) {
                         tem *= 0.25;
                    }
                    System.out.println("tem = " + tem);
                    result += tem;
                    publish(result);
                }
            } else if (stateCheck == 2) {
                double result = 0;
                double tem;
                for (int i=0;i<=N;i++) {
                    if (task.isCancelled()) {
                        return null;
                    }
                    if (i == 0) {
                        continue;
                    }
                    tem = 1.0;
                    double f = i * i;
                    for (int k =1;k<=i;k++) {
                        tem *= 0.25;
                    }
                    double v = f * tem;
                    result += v;
                    publish(result);
                }
            } else if (stateCheck == 3) {
                System.out.println("选择第4个方程");
            } else if (stateCheck == 4) {
                //与上面相同
                double result = 0;
                for (int i=N/2;i<=N;i++) {
                    if (task.isCancelled()) {
                        return null;
                    }
                    result += (1.0/i);
                    publish(result);
                }
            }
            return null;
        }

        @Override
        protected void process(List<Double> chunks) {
            Double last = chunks.getLast();
            resultLabel.setText(last.toString());
        }

        @Override
        protected void done() {
            Toolkit.getDefaultToolkit().beep();
            System.out.println("任务终止");
        }
    }
}

package com.snl.data.homework.charptor02;

import com.snl.swing.practice.button.CustomButton;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

public class Problem09 extends JPanel {

    protected JComboBox<String> comboBox;
    protected JLabel comBoxLabel;
    protected JButton sizeButton;
    protected long size;
    protected TableModel model;
    protected JTable table;
    protected List<Long> sizes = new ArrayList<>();
    /**
     * 状态机制.默认为-1表示无事发生
     */
    protected int stateCheck = -1;
    protected JButton startButton;

    protected long startTime;
    protected long endTime;

    protected Task task;
    protected JLabel resultLabel;
    protected final RandomGenerator generator =
            RandomGenerator.getDefault();
    protected long[] data;
    protected Object[][] dataSet;
    protected JButton reset;

    public Problem09(LayoutManager layout) {
        super(layout);
        init();
    }

    public Problem09() {
        super(new BorderLayout());
        init();
    }

    private void init() {
        setBackground(Color.black);

        comBoxLabel = new JLabel("选择一种算法",SwingConstants.CENTER);
        comBoxLabel.setForeground(Color.GREEN);
        comboBox = new JComboBox<>(new MyComBoxModelImplement());
        comBoxLabel.setLabelFor(comboBox);

        sizeButton = new CustomButton("样本数量");
        sizeButton.addActionListener(new MyImplements());
        sizeButton.setToolTipText("设置样本数量");

        startButton = new CustomButton("运行");
        startButton.setToolTipText("运行此程序,将会消耗您的电脑几秒钟");
        startButton.addActionListener(new StartTask());

        reset = new CustomButton("重置");
        reset.setToolTipText("重新设置");
        reset.addActionListener(e -> {
            sizes.clear();
            repaint();
        });

        resultLabel = new JLabel("测试文本",JLabel.CENTER);
        resultLabel.setForeground(Color.GREEN);

        model = new TableModelInplement();
        table = new JTable(model);
        table.setRowHeight(25);
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
        constraints.insets = new Insets(10,30,10,5);
        constraints.fill = GridBagConstraints.NONE;
        add(comBoxLabel,constraints);

        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.5f;
        add(comboBox,constraints);

        constraints.gridx = 2;
        constraints.weightx = 0.1f;
        constraints.fill = GridBagConstraints.NONE;
        add(sizeButton,constraints);

        constraints.gridx = 3;
        constraints.weightx = 0.1f;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(reset,constraints);

        constraints.gridx = 4;
        constraints.weightx = 0.1f;
        add(startButton,constraints);

        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1.0f;
        constraints.weighty = 0.5f;
        constraints.fill = GridBagConstraints.NONE;
        JScrollPane jScrollPane = new JScrollPane(table);
        add(jScrollPane,constraints);

        constraints.gridy = 2;
        constraints.weighty = 0.1f;
        add(resultLabel,constraints);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2 = (Graphics2D) g.create();

        g2.setColor(Color.CYAN);
        g2.drawString("这是关于最大子序列的问题的总和",10,70);
        g2.drawString("其中包含了四种不同的算法,请分析算法运行的时间和效率",10,90);
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(700,600);
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var problem = new Problem09();
        frame.add(problem);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Problem09::createUi);
    }

    class MyImplements implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String o = (String) JOptionPane.showInputDialog(null,
                    "请输入样本数量大小", "样本数量", JOptionPane.QUESTION_MESSAGE,
                    new ImageIcon("queen.gif"), null, null);
            if (Objects.isNull(o)||o.isBlank()) {
                System.err.println("输入为空或者为null!请重试!");
                return;
            }
            size = Long.parseLong(o);
            sizes.add(size);
            model = new TableModelInplement();
            table.setModel(model);
            data = new long[(int) size];
            for (int i =0;i<size;i++) {
                data[i] = generator.nextLong(-size,size+1);
            }
            repaint();
        }
    }

    class TableModelInplement implements TableModel, TableModelListener {

        protected String[] columnNames;

        protected final List<TableModelListener> listeners =
                new ArrayList<>();

        protected Object oldItem;
        protected Object currentItem;
        protected int lastModifiedIndex;

        public TableModelInplement() {
            columnNames = new String[]{"样本数量","O(N³)","O(N²)","O(NlogN)","O(N)"};
            if (dataSet == null) {
                dataSet = new Object[sizes.size()][columnNames.length];
            }
            else {
                Object[] o = new Object[columnNames.length];
                dataSet = Arrays.copyOf(dataSet,sizes.size());
                dataSet[sizes.size() - 1] = o;
                for (int i=0;i<sizes.size();i++) {
                    Long l = sizes.get(i);
                    dataSet[i][0] = "N = " + l;
                }
            }
            addTableModelListener(this);

        }

        @Override
        public int getRowCount() {
            return dataSet.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int columnIndex) {
            if (columnIndex < 0 || columnIndex > getColumnCount())
                throw new ArrayIndexOutOfBoundsException("超出边界");
            return columnNames[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex < 0 || columnIndex > getColumnCount())
                throw new ArrayIndexOutOfBoundsException("超出边界");
            return columnNames[columnIndex].getClass();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex != 0;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex < 0 || rowIndex > getRowCount() ||
                    columnIndex < 0 || columnIndex > getColumnCount())
                throw new ArrayIndexOutOfBoundsException("索引超出边界");
            return dataSet[rowIndex][columnIndex];
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (rowIndex < 0 || rowIndex > getRowCount() ||
                    columnIndex < 0 || columnIndex > getColumnCount())
                throw new ArrayIndexOutOfBoundsException("索引超出边界");
            oldItem = dataSet[rowIndex][columnIndex];
            dataSet[rowIndex][columnIndex] = aValue;
            currentItem = aValue;
            lastModifiedIndex = rowIndex;
            fireDataChange(aValue,rowIndex,columnIndex);
        }

        private void fireDataChange(Object aValue, int rowIndex, int columnIndex) {
            TableModelEvent tableModelEvent = new TableModelEvent(this,rowIndex,columnIndex);
            for (TableModelListener listener : listeners) {
                listener.tableChanged(tableModelEvent);
            }
        }

        @Override
        public void addTableModelListener(TableModelListener l) {
            listeners.add(l);
        }

        @Override
        public void removeTableModelListener(TableModelListener l) {
            listeners.remove(l);
        }

        @Override
        public void tableChanged(TableModelEvent e) {
            System.out.println("模型事件发生变更");
        }
    }

    class MyComBoxModelImplement implements ComboBoxModel<String> , ListDataListener {

        protected Object currentItem;
        protected Object oldItem;
        protected int index0 = -1;
        protected int index1 = -1;
        protected final List<ListDataListener> listeners =
                new ArrayList<>();
        protected String[] data;

        public MyComBoxModelImplement() {
            data = new String[] {"O(N³)","O(N²)","O(NlogN)","O(N)"};
            setIndex(0);
            addListDataListener(this);
        }

        @Override
        public void setSelectedItem(Object anItem) {
            if (anItem == null) {
                throw new IllegalArgumentException("参数错误");
            }
            oldItem = currentItem;
            currentItem = anItem;
            if (oldItem != null)
                index0 = getIndex(oldItem);
            index1 = getIndex(currentItem);
            fireDataChanged();
        }

        private void fireDataChanged() {

            var e =
                    new ListDataEvent(this,ListDataEvent.CONTENTS_CHANGED,index0,index1);
            for (ListDataListener listener : listeners) {
                listener.contentsChanged(e);
            }
        }

        private int getIndex(Object oldItem) {
            for (int i = 0;i<getSize();i++) {
                if (Objects.equals(data[i],oldItem)) return i;
            }
            throw new RuntimeException("未找到当前项目");
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
        public String getElementAt(int index) {
            if (index <0 || index > getSize()) {
                throw new ArrayIndexOutOfBoundsException("索引超过边界");
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

        private void setIndex(int index) {
            if (index <0 || index > getSize()) {
                throw new ArrayIndexOutOfBoundsException("索引超过边界");
            }
            oldItem = currentItem;
            currentItem = getElementAt(index);
            stateCheck = index;
        }

        @Override
        public void intervalAdded(ListDataEvent e) {
            //无动作
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            //动作
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            System.out.println("模型数据变更");
            System.out.println("上次选项: " + oldItem);
            System.out.println("本次选项: " + currentItem);
            stateCheck = index1;
            System.out.println("stateCheck = " + stateCheck);
        }
    }

    class Task extends SwingWorker<Void,Void> {

        @Override
        protected Void doInBackground() throws Exception {
            startTime = System.currentTimeMillis();
            switch (stateCheck) {
                case -1 -> System.err.println("暂未运行");
                case 0 -> saveProblem01();
                case 1 -> saveProblem02();
                case 2 -> saveProblem03();
                case 3 -> saveProblem04();
            }
            endTime = System.currentTimeMillis();
            return null;
        }

        private void saveProblem04() {
            long maxSum = 0,thisSum = 0;
            for (long datum : data) {
                thisSum += datum;
                if (thisSum > maxSum) {
                    maxSum = thisSum;
                } else if (thisSum < 0) {
                    thisSum = 0;
                }
            }
            System.out.println("maxSum = " + maxSum);
        }

        private void saveProblem03() {
            //TODO
        }

        private void saveProblem02() {
            long max = 0;
            for (int i=0;i< data.length;i++) {
                long thisSum = 0;
                for (int j=i;j< data.length;j++) {
                    thisSum += data[j];
                    if (thisSum > max) max = thisSum;
                }
            }
            System.out.println("maxSum = " + max);
        }

        private void saveProblem01() {
            long maxSum = 0;
            for (int i=0;i< data.length;i++) {
                for (int j=i;j< data.length;j++) {
                    long thisSum = 0;
                    for (int k = i;k<=j;k++) {
                        thisSum += data[k];
                    }
                    if (thisSum > maxSum)
                        maxSum = thisSum;
                }
            }
            System.out.println("maxSum = " + maxSum);
        }

        @Override
        protected void done() {
            Toolkit.getDefaultToolkit().beep();
            resultLabel.setText("一共花费 %d ms".formatted(endTime - startTime));
        }
    }

    class StartTask implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (task != null && !task.isDone()) {
                System.err.println("当前有任务暂未运行完成");
                return;
            }
            if (sizes.isEmpty()) {
                System.err.println("暂未有样本数量请重试!");
                return;
            }
            task = new Task();
            task.execute();
        }
    }
}

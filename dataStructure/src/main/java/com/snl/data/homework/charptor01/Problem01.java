package com.snl.data.homework.charptor01;

import com.snl.swing.practice.button.CustomButton;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

public class Problem01 extends JPanel {

    protected JButton start;
    protected JButton selectButton;
    protected JButton resetButton;
    protected JButton insertButton;
    protected JButton customSolve;
    protected JTable table;
    protected final int N = 100000;
    protected final int K = N / 2;
    protected final Integer[] dataSet = new Integer[N];
    protected final RandomGenerator generator =
            RandomGenerator.getDefault();
    protected TableModel tableModel;
    protected JLabel label;
    protected JLabel resultLabel;
    protected Task task;
    /**
     * 0标志冒泡排序，1表示选择排序，2标志插入排序,3或者其他是自定义排序
     */
    protected int stateCheck = -1;

    /**
     * 模拟计算时间
     */
    protected long startTime;
    protected long endTime;

    public Problem01() {
        super(new BorderLayout());
        init();
    }

    private void init() {
        setBackground(Color.black);
        start = new CustomButton("冒泡排序");
        start.addActionListener(new BubbleSortImplement());
        selectButton = new CustomButton("选择排序");
        selectButton.addActionListener(new SelectSortImplement());
        resetButton = new CustomButton("重置");
        resetButton.addActionListener(e -> {

            if (task != null && !task.isDone()) {
                System.err.println("还有任务未完成！");
                return;
            }
            resultLabel.setText("这是您的答案😄");
            for (int i = 0;i<N;i++) {
                dataSet[i] = generator.nextInt(N);
            }
            repaint();
        });

        insertButton = new CustomButton("插入排序");
        insertButton.addActionListener(e -> {
            if (task != null && !task.isDone()) {
                System.err.println("有任务正在运行");
                return;
            }
            stateCheck = 2;
            task = new Task();
            task.execute();
        });

        customSolve = new CustomButton("自定义排序");
        customSolve.addActionListener(e -> {
            if (task != null && !task.isDone()) {
                System.err.println("有任务正在运行");
                return;
            }
            stateCheck = 3;
            task = new Task();
            task.execute();
        });
        customSolve.setToolTipText("""
                前 k 个元素读入数组并排序（降序）。接下来，逐个读取剩余的元素。
                当一个新的元素到达时，如果它小于数组中的第 k 个元素，则被忽略。
                否则，它被放置在数组中的正确位置，将一个元素挤出数组。
                当算法结束时，数组中第 k 个位置的元素作为答案返回。
                """);
        label = new JLabel("测试文本",JLabel.CENTER);
        label.setForeground(Color.YELLOW);

        resultLabel = new JLabel("这是您的答案😄",JLabel.CENTER);
        resultLabel.setForeground(Color.GREEN);

        for (int i = 0;i<N;i++) {
            dataSet[i] = generator.nextInt(N);
        }
        //创建表格模型
        tableModel = new MyJtableModeImplement();
        table = new JTable(tableModel);
        table.setBackground(Color.black);
        table.setForeground(Color.cyan);
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
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10,10,10,10);
        constraints.anchor = GridBagConstraints.EAST;
        add(start,constraints);

        constraints.gridx = 1;
        add(selectButton,constraints);

        constraints.gridx = 2;
        add(insertButton,constraints);

        constraints.gridx = 3;
        add(customSolve,constraints);

        constraints.gridx = 4;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        add(resetButton,constraints);

        constraints.gridx = 5;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.PAGE_START;
        add(label,constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weighty = 1.0f;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        JScrollPane jScrollPane = new JScrollPane(table);
        add(jScrollPane,constraints);

        constraints.gridy = 2;
        constraints.weighty = 0.1f;
        add(resultLabel,constraints);

    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Problem01 problem01 = new Problem01();
        frame.add(problem01);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800,700);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2 = (Graphics2D) g.create();

        tableModel = new MyJtableModeImplement();
        table.setModel(tableModel);
        g2.setColor(Color.green);
        g2.scale(1.25,1.25);
        g2.drawString("这是java数据结构第一章第一道练习题要求:\n" +
                "给定一个长度为N数组，找出第K大的数字，其中K = N / 2?",10,75);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Problem01::createUi);
    }

    class MyJtableModeImplement implements TableModel ,TableModelListener {

        protected String[] columnNames;
        protected Object[][] data;

        protected final List<TableModelListener> listeners =
                new ArrayList<>();

        /**
         * 记录上一次修改的单元格的行数->对应于数据的下标索引
         */
        protected int lastModifiedIndex;

        /**
         * 修改前的单元格数据
         */
        protected Object oldValue;

        /**
         * 修改后的单元格数据
         */
        protected Object newValue;

        public MyJtableModeImplement() {
            initData();
            addTableModelListener(this);
        }

        private void initData() {
            columnNames = new String[]{"序号","数字"};
            //TODO 如何初始化数据呢？
            data = new Object[dataSet.length][columnNames.length];
            for (int i = 0;i< dataSet.length;i++) {
                data[i][0] = i;
                data[i][1] = dataSet[i];
            }
        }

        @Override
        public int getRowCount() {
            return data.length;
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int columnIndex) {
            if (columnIndex < 0 || columnIndex > columnNames.length)
                throw new ArrayIndexOutOfBoundsException("超出边界");
            return columnNames[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex < 0 || columnIndex > columnNames.length)
                throw new ArrayIndexOutOfBoundsException("超出边界");
            return columnNames[columnIndex].getClass();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            //默认不可以编辑
            return columnIndex != 0;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex < 0 || rowIndex > getRowCount() ||
                    columnIndex < 0 || columnIndex > getColumnCount())
                throw new ArrayIndexOutOfBoundsException("索引超出边界");
            return data[rowIndex][columnIndex];
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            //只调用一次
            if (rowIndex < 0 || rowIndex > getRowCount() ||
                    columnIndex < 0 || columnIndex > getColumnCount())
                throw new ArrayIndexOutOfBoundsException("索引超出边界");
            oldValue = data[rowIndex][columnIndex];
            data[rowIndex][columnIndex] = aValue;
            newValue = aValue;
            lastModifiedIndex = rowIndex;
            fireDataChange(aValue,rowIndex,columnIndex);
        }

        private void fireDataChange(Object value,int row,int column) {
            TableModelEvent tableModelEvent = new TableModelEvent(this,row,column);
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
            System.out.println("模型事件变更");
            dataSet[lastModifiedIndex] = Integer.parseInt((String) newValue);
        }
    }

    class BubbleSortImplement implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (task != null && !task.isDone()) {
                System.err.println("有任务正在运行");
                return;
            }
            stateCheck = 0;
            task = new Task();
            task.execute();
        }
    }

    class SelectSortImplement implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (task != null && !task.isDone()) {
                System.err.println("有任务正在运行");
                return;
            }
            stateCheck = 1;
            task = new Task();
            task.execute();
        }
    }

    class Task extends SwingWorker<Void,Void> {

        @Override
        protected Void doInBackground() throws Exception {
            startTime = System.currentTimeMillis();
            if (stateCheck == 0) {
                //冒泡排序
                boolean swapped = false;
                for (int i = 0;i< dataSet.length - 1;i++) {
                    for (int j = 0;j< dataSet.length - 1 - i;j++) {
                        if (dataSet[j] > dataSet[j + 1]) {
                            int temp = dataSet[j + 1];
                            dataSet[j+1] = dataSet[j];
                            dataSet[j] = temp;
                            swapped = true;
                        }
                        publish();
                    }
                    if (!swapped) break;
                }
            }else if (stateCheck == 1) {
                for (int i = 0;i< dataSet.length - 1;i++) {
                    int min = i;
                    for (int j = i + 1;j<dataSet.length;j++){
                        if (dataSet[j] < dataSet[min]) {
                            int temporary = dataSet[i];
                            dataSet[i] = dataSet[j];
                            dataSet[j] = temporary;
                        }
                    }
                    publish();
                }
            }else if (stateCheck == 2) {
                for (int i = 1;i< dataSet.length;i++) {
                    for (int j=i;j>0 && (dataSet[j] < dataSet[j-1]);j--) {
                        int temp = dataSet[j];
                        dataSet[j] = dataSet[j -1];
                        dataSet[j - 1] = temp;
                    }
                    publish();
                }
            } else if (stateCheck == 3) {
                for (int i = 0;i< K + 1;i++) {
                    int max = i;
                    for (int j=i + 1;j<K;j++) {
                        if (dataSet[j] > dataSet[max]) {
                            int tem = dataSet[j];
                            dataSet[j] = dataSet[max];
                            dataSet[max] = tem;
                        }
                    }
                    publish();
                }

                // 你的核心逻辑补全
                for (int i = K; i < dataSet.length; i++) {
                    // 当前元素比前K个的最后一个元素大（说明有机会插入前K）
                    if (dataSet[i] > dataSet[K - 1]) { // 修正：原dataSet[K]可能越界，改为K-1（前K个的最后一位）
                        // 遍历前K个元素，找到第一个比当前元素小的位置j
                        for (int j = 0; j < K; j++) {
                            if (dataSet[j] < dataSet[i]) {
                                // 1. 保存当前要插入的元素（避免被覆盖）
                                int insertVal = dataSet[i];
                                // 2. 保存前K个中j位置的原元素（用于后移）
                                int temp = dataSet[j];
                                // 3. 先将插入元素放到j位置
                                dataSet[j] = insertVal;
                                // 4. 从j+1到K-1的元素依次后移一位（核心补全逻辑）
                                for (int m = j + 1; m < K; m++) {
                                    // 保存当前m位置的元素
                                    int nextTemp = dataSet[m];
                                    // 将前一个位置的temp放到m位置
                                    dataSet[m] = temp;
                                    // 更新temp为下一个元素，继续后移
                                    temp = nextTemp;
                                }
                                // 5. 找到第一个位置后退出循环（避免重复插入）
                                break;
                            }
                        }
                    }
                }
            }
            endTime = System.currentTimeMillis();
            label.setText("一共花费 %d ms".formatted(endTime - startTime));
            return null;
        }

        @Override
        protected void process(List<Void> chunks) {
            repaint();
        }

        @Override
        protected void done() {
            Toolkit.getDefaultToolkit().beep();
            System.out.println("任务运行完毕");
            resultLabel.setText("当前第[%d]大的值是：%d".formatted(K,dataSet[K]));
            repaint();
        }
    }
}

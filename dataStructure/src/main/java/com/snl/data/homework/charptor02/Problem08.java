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

public class Problem08 extends JPanel {

    protected JButton startButton;
    protected JLabel label;
    protected JTextField textField;
    protected JLabel resultLabel;
    protected final RandomGenerator generator =
            RandomGenerator.getDefault();
    /**
     * 数字的位数
     */
    protected int count;

    /**
     * 准备任务
     */
    protected Task task;

    protected Integer[] data;

    protected JTable table;

    protected TableModel modelOfTable ;

    protected JComboBox<String> comboBox;

    protected int stateCheck;

    protected long startTime;
    protected long endTime;

    public Problem08() {
        super(new BorderLayout());
        init();
    }

    private void init() {
        setBackground(Color.black);
        textField = new JTextField(15);
        label = new JLabel("请输入随机数字数量:");
        label.setLabelFor(textField);
        label.setForeground(Color.YELLOW);

        resultLabel = new JLabel("测试文本",JLabel.CENTER);
        resultLabel.setForeground(Color.GREEN);

        startButton = new CustomButton("运行");
        startButton.addActionListener(new MyActionListenerImplement());

        modelOfTable = new TableModelInplement();
        table = new JTable(modelOfTable);

        comboBox = new JComboBox<>(new MyComBoxModelImplement());
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
        add(comboBox,constraints);

        constraints.gridx = 3;
        constraints.weightx = 0.3f;
        constraints.gridwidth = GridBagConstraints.HORIZONTAL;
        add(startButton,constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weighty = 0.8f;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        JScrollPane jScrollPane = new JScrollPane(table);
        add(jScrollPane,constraints);

        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.PAGE_END;
        constraints.weighty = 0.1f;
        add(resultLabel,constraints);
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2 = (Graphics2D) g.create();
    }

    class MyActionListenerImplement implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String text = textField.getText();
            if (text.isBlank()) {
                System.err.println("输入为空或者为空字符串,请重新尝试");
                return;
            }
            count =  Integer.parseInt(text);
            if (task != null&&!task.isDone()) {
                System.err.println("任务正在执行");
                return;
            }
            task = new Task();
            task.execute();
        }
    }

    class Task extends SwingWorker<Void,Void> {

        @Override
        protected Void doInBackground() throws Exception {
            //需要做什么?执行什么?要做什么?
            data = new Integer[count];
            //TODO 填充数组
            startTime = System.currentTimeMillis();
            switch (stateCheck) {
                case 0 -> solveProblem01();
                case 1 -> solveProblem02();
                case 2 -> solveProblem03();
            }
            endTime = System.currentTimeMillis();
//            data = Arrays.stream(data).sorted().toList().toArray(data);
            return null;
        }

        /**
         * 效率最高的做法
         */
        private void solveProblem03() {
            int i;
            int r;
            for (i=0;i<count;i++) {
                data[i] = i + 1;
            }

            for (i = 1;i<count;i++) {
                int tem = data[i];
                r =generator.nextInt(0,i);
                data[i] = data[r];
                data[r] = tem;
            }
        }

        /**
         * 效率次高的做法
         */
        private void solveProblem02() {
            boolean[] used = new boolean[count];
            for (int i=0;i<count;i++) {
                while (true) {
                    int r = generator.nextInt(0, count);
                    if (!used[r]) {
                        used[r] = true;
                        data[i] = r+1;
                        break;
                    }
                }
            }
        }

        /**
         * 效率最低的做法
         */
        private void solveProblem01() {
            for (int i=0;i<count;i++) {
                int k;
                boolean hasFinding = true;
                while (hasFinding) {
                    int r = generator.nextInt(1, count + 1);
                    for (k=0;k<i;k++) {
                        if (data[k] == r) {
                            break;
                        }
                    }
                    if (k < i) {
                        continue;
                    }
                    data[i] = r;
                    hasFinding = false;
                }
            }
        }

        @Override
        protected void done() {
            modelOfTable = new TableModelInplement();
            table.setModel(modelOfTable);
            repaint();
            resultLabel.setText("共花费 %d ms".formatted(endTime - startTime));
            Toolkit.getDefaultToolkit().beep();
        }
    }

    class TableModelInplement implements TableModel , TableModelListener {

        protected String[] columnNames;
        protected Object[][] data;
        protected final List<TableModelListener> listeners =
                new ArrayList<>();

        protected Object oldItem;
        protected Object currentItem;
        protected int lastModifiedIndex;

        public TableModelInplement() {
            columnNames = new String[]{"序号","大小"};
            data = new Object[count][columnNames.length];
            for (int i=0;i<count;i++) {
                data[i][0] = i;
                data[i][1] = Problem08.this.data[i];
            }
            addTableModelListener(this);
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
            return false;
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
            if (rowIndex < 0 || rowIndex > getRowCount() ||
                    columnIndex < 0 || columnIndex > getColumnCount())
                throw new ArrayIndexOutOfBoundsException("索引超出边界");
            oldItem = data[rowIndex][columnIndex];
            data[rowIndex][columnIndex] = aValue;
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

    class MyComBoxModelImplement implements ComboBoxModel<String> , ListDataListener{

        protected Object currentItem;
        protected Object oldItem;
        protected int index0 = -1;
        protected int index1 = -1;
        protected final List<ListDataListener> listeners =
                new ArrayList<>();
        protected String[] data;

        public MyComBoxModelImplement() {
            data = new String[] {
                    "解法一","解法二","解法三"
            };
            setIndex(0);
            stateCheck = 0;
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
        }
    }
}

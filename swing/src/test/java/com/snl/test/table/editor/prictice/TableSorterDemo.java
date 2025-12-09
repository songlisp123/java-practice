package com.snl.test.table.editor.prictice;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class TableSorterDemo extends JFrame implements TableModelListener, ListSelectionListener {

    protected JPanel panel;

    protected TableRowSorter<SimpleTableModel> sorter;
    protected SimpleTableModel simpleTableModel;
    protected JTable table;
    protected FilterTextFieldDemo<SimpleTableModel> filterTextField;
    protected JLabel filterTextLabel;

    public TableSorterDemo() throws HeadlessException {
        super("测试标题");
        initComponents();
        alignAtrribute();
    }

    public TableSorterDemo(String title) throws HeadlessException {
        super(title);
        initComponents();
        alignAtrribute();
    }

    private void initComponents() {
        panel = new JPanel(new BorderLayout());
        simpleTableModel = new SimpleTableModel();
        table = new JTable(simpleTableModel);
        sorter = new TableRowSorter<>(simpleTableModel);
        filterTextField = new FilterTextFieldDemo<>(sorter);
        simpleTableModel.addTableModelListener(this);
        filterTextLabel = new JLabel("过滤器：");
        filterTextLabel.setLabelFor(filterTextField);
        table.setRowSorter(sorter);
        panel.add(filterTextLabel,BorderLayout.LINE_START);
        panel.add(filterTextField);
        table.getSelectionModel().addListSelectionListener(this);
    }

    private void alignAtrribute() {
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setPreferredScrollableViewportSize(new Dimension(200,150));
        table.setFillsViewportHeight(true);

        getContentPane().add(table.getTableHeader(),BorderLayout.PAGE_START);
        getContentPane().add(table);
        getContentPane().add(panel,BorderLayout.PAGE_END);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        System.out.println("表格模型数据事件变更……");
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        System.out.println("选择事件开始……");
        int row = table.getSelectedRow(); //这是视图模型的选择
        if (row != -1) {
            int rowIndexToModel = table.convertRowIndexToModel(row);
            System.out.printf("视图行：%d,模型行：%d%n",row,rowIndexToModel);
        }
    }
}

package com.snl.test.table;

import com.snl.test.table.model.CustomListSelectionModel;
import com.snl.test.table.model.SimpleTableColumnModel;
import com.snl.test.table.model.SimpleTableModel;
import com.snl.test.table.sorter.SimpleTableSorterDemo;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class UserSimpleTableModelDemo extends JFrame
        implements TableModelListener , ListSelectionListener , TableColumnModelListener {

    protected SimpleTableModel simpleTableModel;
    protected JTable table;
    protected TableColumnModel columnModel;
    protected ListSelectionModel selectionModel;
    protected TableRowSorter<TableModel> sorter;

    public UserSimpleTableModelDemo() throws HeadlessException {
        super("默认标签");
        initComponents();
    }

    public UserSimpleTableModelDemo(String title) throws HeadlessException {
        super(title);
        initComponents();
    }

    private void initComponents() {
        simpleTableModel = new SimpleTableModel();
        simpleTableModel.addTableModelListener(this);

        selectionModel = new CustomListSelectionModel();
        selectionModel.addListSelectionListener(this);

        columnModel = new SimpleTableColumnModel(selectionModel);
        columnModel.addColumnModelListener(this);

        sorter = new SimpleTableSorterDemo<>(simpleTableModel);
//        sorter.setRowFilter();

        table = new JTable(simpleTableModel);
        table.setSelectionModel(selectionModel);
//        table.setAutoCreateRowSorter(true);
//        table.setColumnModel(columnModel);
        table.getColumnModel().addColumnModelListener(this); //这是一个事件
        table.setRowSorter(sorter);//为什么我设置的行排序器不能用呢？

        var rowSorter = table.getRowSorter();
        System.out.println("rowSorter = " + rowSorter);

        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);


        getContentPane().add(table.getTableHeader(),BorderLayout.PAGE_START);
        getContentPane().add(table,BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pack();
        setVisible(true);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        System.out.println("表格模型事件程序开始");
        TableCellEditor cellEditor = table.getCellEditor();
        System.out.println("cellEditor = " + cellEditor);
        int type = e.getType();
        int column = e.getColumn();
        int firstRow = e.getFirstRow();
        int lastRow = e.getLastRow();
        if (type == TableModelEvent.UPDATE) {
            if (firstRow == lastRow) {
                System.out.printf("行[%d]，列[%d]单元格数据已更新为：%s%n", firstRow,column,
                        simpleTableModel.getValueAt(firstRow,column));
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(UserSimpleTableModelDemo::new);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int firstIndex = e.getFirstIndex();
        int row = table.getSelectedRow();
        if (row != -1) {
            int rowIndexToModel = table.convertRowIndexToModel(row);
            System.out.println("模型视图坐标" + rowIndexToModel);
        }
        System.out.println("视图坐标:" + row);

        int lastIndex = e.getLastIndex();
        System.out.println("lastIndex = " + lastIndex);

        int firstIndex1 = e.getFirstIndex();
        System.out.println("firstIndex1 = " + firstIndex1);
    }

    @Override
    public void columnAdded(TableColumnModelEvent e) {
        System.out.println("添加列触发……");
    }

    @Override
    public void columnRemoved(TableColumnModelEvent e) {
        System.out.println("删除列触发……");
    }

    @Override
    public void columnMoved(TableColumnModelEvent e) {
        System.out.println("模型列移动……");
        int selectedColumn = table.getSelectedColumn();
        int toModel =
                table.convertColumnIndexToModel(selectedColumn);
    }

    @Override
    public void columnMarginChanged(ChangeEvent e) {
        System.out.println("列宽度变更触发……");
    }

    @Override
    public void columnSelectionChanged(ListSelectionEvent e) {
        System.out.println("类选择触发……");
    }
}

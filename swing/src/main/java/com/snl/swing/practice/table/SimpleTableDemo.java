package com.snl.swing.practice.table;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Comparator;

public class SimpleTableDemo extends JPanel implements TableModelListener {

    protected SimpleTableModelDemo tableModel;
    protected JTable table;
    protected TableRowSorter<TableModel> tableRowSorter;
    protected SimpleFilter<TableModel> filter;
    protected TableCellRenderer renderer;
    protected TableCellEditor editor;

    public SimpleTableDemo() {
        super(new BorderLayout());
        init();
    }

    private void init() {
        tableModel = new SimpleTableModelDemo();
        tableModel.addTableModelListener(this);
        tableRowSorter = new TableRowSorter<>(tableModel);
        tableRowSorter.setComparator(1, Comparator.comparing(String::length));
        filter = new SimpleFilter<>(tableRowSorter);
//        renderer = new SimpleRenderDemo("难顶");
        renderer = new CustomRenderDemo();
        editor = new CustomEditorDemo();
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(200,150));
        table.setFillsViewportHeight(true);
        table.setRowHeight(50);
        table.setAutoCreateRowSorter(true);
        table.setRowSorter(tableRowSorter);
        table.getColumn(table.getColumnName(2)).setCellRenderer(renderer);
        table.setDefaultEditor(ImageIcon.class,editor);

        add(table.getTableHeader(),BorderLayout.PAGE_START);
        add(table,BorderLayout.CENTER);
        add(filter,BorderLayout.PAGE_END);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        System.out.println("表格模型数据事件变更……");
    }


}

package com.snl.test.table.sorter;

import com.snl.test.table.filter.SimpleRowFilterImplement;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.Comparator;

public class SimpleTableSorterDemo<M extends TableModel> extends TableRowSorter<M> {

    protected M model;
    protected Comparator<?> comparator;
    protected RowFilter<M,Integer> filter;

    public SimpleTableSorterDemo() {
        init();
    }

    public SimpleTableSorterDemo(M model) {
        super(model);
        this.model = model;
        init();
    }


    private void init() {
        System.out.println(0);
        comparator = Comparator.comparing(String::length);
        filter = new SimpleRowFilterImplement<>();
        setRowFilter(filter);
    }

    private int columnCounts() {
        System.out.println(2);
        return model.getColumnCount();
    }

    @Override
    public int getModelRowCount() {
        System.out.println(1);
        return model.getColumnCount();
    }

    @Override
    public Comparator<?> getComparator(int column) {
        Class<?> columnClass = model.getColumnClass(column);
        String name = columnClass.getName();
        if (name.equals("java.lang.String")) {
            return this.comparator;
        }
        return super.getComparator(column);
    }

    @Override
    public void setComparator(int column, Comparator<?> comparator) {
        if (column < 0 || column > this.columnCounts())
            throw new IllegalArgumentException("列数超出限制！");
        Class<?> columnClass = model.getColumnClass(column);
        String name = columnClass.getName();
        if (name.equals("java.lang.String")) {
            this.comparator = comparator;
            sort();
        }
    }
}

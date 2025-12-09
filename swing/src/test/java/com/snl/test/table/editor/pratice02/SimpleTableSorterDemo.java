package com.snl.test.table.editor.pratice02;

import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableStringConverter;
import java.util.Comparator;

public class SimpleTableSorterDemo<M extends TableModel> extends TableRowSorter<M> {

    protected M m;
    protected TableStringConverter stringConverter;
    protected Comparator comparator;

    public SimpleTableSorterDemo() {
    }

    public SimpleTableSorterDemo(M model) {
        this.m = model;
    }

    //TODO 还需要努力一点完成更多的逻辑


    @Override
    public void setModel(M model) {
        this.m = model;
    }

    @Override
    public void setStringConverter(TableStringConverter stringConverter) {
        this.stringConverter = stringConverter;
    }

    @Override
    public TableStringConverter getStringConverter() {
        return this.stringConverter;
    }

    @Override
    public Comparator<?> getComparator(int column) {
        return super.getComparator(column);
    }

    public M getM() {
        return m;
    }

    public void setM(M m) {
        this.m = m;
    }
}

package com.snl.test.table.editor.prictice;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.regex.PatternSyntaxException;

public class FilterTextFieldDemo<T extends TableModel> extends JTextField implements DocumentListener {

    private TableRowSorter<T> sorter;

    public FilterTextFieldDemo() {
        super(10);
        init();
    }

    public FilterTextFieldDemo(TableRowSorter<T> sorter) {
        super(10);
        this.sorter = sorter;
        init();
    }

    private void init() {
        this.getDocument().addDocumentListener(this);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        newFilter();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        newFilter();
    }

    private void newFilter() {
        //获取新的行过滤器
        RowFilter<T,Object> filter = null;
        //如果当前句子无法解析，跳过这个
        try {
            filter = RowFilter.regexFilter(getText(),0);
        }catch (PatternSyntaxException e) {
            e.printStackTrace();
            return;
        }
//        System.out.println(filter);
        sorter.setRowFilter(filter);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        //默认情况下不会调用此事件
    }

    public TableRowSorter<T> getSorter() {
        return sorter;
    }

    public void setSorter(TableRowSorter<T> sorter) {
        this.sorter = sorter;
    }
}

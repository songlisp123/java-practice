package com.snl.swing.practice.table;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.regex.PatternSyntaxException;


/**
 *
 * @param <T>
 */
public class SimpleFilter<T extends TableModel> extends JPanel implements DocumentListener {

    protected TableRowSorter<T> sorter;
    protected JTextField textField;
    protected JLabel label;

    public SimpleFilter(TableRowSorter<T> sorter) {
        super(new BorderLayout());
        this.sorter = sorter;
        init();
    }

    private void init() {
        label = new JLabel("过滤：");
        textField = new JTextField(10);
        label.setLabelFor(textField);
        textField.getDocument().addDocumentListener(this);

        add(label,BorderLayout.LINE_START);
        add(textField);
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        newFilter();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        newFilter();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        //基本上不调用！
        newFilter();
    }

    private void newFilter() {
        RowFilter<T,Object> rowFilter = null;
        try {
            rowFilter = RowFilter.regexFilter(textField.getText(),1); //硬编码，不好
        }catch (PatternSyntaxException e) {
            return;
        }
//        System.out.println("rowFilter = " + rowFilter);
        sorter.setRowFilter(rowFilter);
    }

    public TableRowSorter<T> getSorter() {
        return sorter;
    }

    public void setSorter(TableRowSorter<T> sorter) {
        this.sorter = sorter;
    }

    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    public JTextField getTextField() {
        return textField;
    }

    public void setTextField(JTextField textField) {
        this.textField = textField;
    }
}

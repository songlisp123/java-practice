package com.snl.swing.practice.table;

import com.snl.swing.practice.button.CustomButton;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CustomEditorDemo extends AbstractCellEditor implements TableCellEditor, ActionListener {

    private CustomButton button;
    protected final List<CellEditorListener> listeners =
            new ArrayList<>();
    protected Icon current;
    protected JFileChooser chooser;

    public CustomEditorDemo() {
        button = new CustomButton("播放");
        button.addActionListener(this);
        button.setBorderPainted(false);

        chooser = new JFileChooser(".");
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("鼠标点击");
        chooser.setVisible(true);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        current = (Icon) value;
        System.out.println("value = " + value);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return current;
    }
}

package com.snl.test.table.editor.pratice02;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class SimpleTableCellRenderer extends JLabel implements TableCellRenderer  {

    public SimpleTableCellRenderer() {
        setOpaque(true);
//        this.addMouseListener(this);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column)
    {
        if (table == null) throw new IllegalArgumentException("非法参数异常");
        String string = (String) value;
        setText(string);

        if (isSelected) {
            setFont(new Font("宋体", Font.BOLD, 30));
            setForeground(Color.GREEN);
            setBackground(new Color(184, 207, 229));

        }else {
            setFont(new Font("宋体", Font.PLAIN, 20));
            setForeground(Color.BLACK);
            setBackground(null);
        }
        if (hasFocus) {
            setBorder(BorderFactory.createMatteBorder(2,5,2,5,Color.CYAN));
        }else {
            setBorder(null);
        }
        return this;
    }

}

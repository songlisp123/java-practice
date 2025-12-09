package com.snl.swing.practice.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class SimpleRenderDemo extends JLabel implements TableCellRenderer {

    public SimpleRenderDemo(String text) {
        super(text);
        setOpaque(true);
        setForeground(null);
        setFont(new Font("楷体",Font.PLAIN,15));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (table == null) throw new IllegalArgumentException("参数错误");
        String string = (String) value;
        if (isSelected) {
            setBackground(new Color(184, 207, 229));
        }else {
            setBackground(null);
        }

        if (hasFocus) {
            setForeground(Color.cyan);
            setFont(new Font("楷体",Font.BOLD,25));
            setBorder(BorderFactory.createMatteBorder(2,5,2,5,Color.green));
        }
        else {
            setForeground(null);
            setFont(new Font("楷体",Font.PLAIN,15));
            setBorder(null);
        }
        return this;
    }

}

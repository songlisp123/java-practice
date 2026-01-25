package com.snl.test.image.homework01;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class SimpleTableCellRenderer extends JLabel implements TableCellRenderer {

    public SimpleTableCellRenderer() {
        setOpaque(true);
//        this.addMouseListener(this);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column)
    {
        if (table == null) throw new IllegalArgumentException("非法参数异常");
        int v = (int) value;
        setText(v+"");

        if (isSelected) {
            setFont(new Font("宋体", Font.BOLD, 12));
            setBackground(new Color(184, 207, 229));

        }else {
            setFont(new Font("宋体", Font.PLAIN, 10));
            setBackground(null);
        }
        if (hasFocus) {
            setBorder(BorderFactory.createMatteBorder(2,5,2,5,Color.CYAN));
            setForeground(Color.MAGENTA);
        }else {
            setBorder(null);
            setForeground(Color.BLACK);
        }
        return this;
    }

}

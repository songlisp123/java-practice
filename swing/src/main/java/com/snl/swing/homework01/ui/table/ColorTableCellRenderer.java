package com.snl.swing.homework01.ui.table;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * 这个静态渲染器将会设置颜色
 */
public class ColorTableCellRenderer extends JPanel implements TableCellRenderer
{
   public Component getTableCellRendererComponent(JTable table, Object value, 
         boolean isSelected, boolean hasFocus, int row, int column)
   {
      setBackground((Color) value);
      if (isSelected) {
         if (hasFocus) {
            setBorder(BorderFactory.createMatteBorder(2, 3, 2, 3, Color.CYAN));
            return this;
         }
         setBorder(BorderFactory.createMatteBorder(2,3,2,3,new Color(184, 207, 229)));
         return this;

      }else {
         setBorder(null);
      }
      return this;
   }
}

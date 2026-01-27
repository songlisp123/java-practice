package com.snl.swing.homework01.data;

import com.snl.swing.homework01.ui.imageFrame.ColorCompomentImplement;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class TableModelImplement implements TableModel {

    private String[] title;//标题
    private final List<TableModelListener> listeners =
            new ArrayList<>(); //监听器

    private final  List<ColorCompomentImplement> colorCompomentImplementLists =
            new ArrayList<>();
    private Object[][] data;

    //更改第一次发生的行数
    private int firstRow;
    private int x;
    //事件发生的末行
    private int lastRow;
    private int y;
    private int id = 0;

    public TableModelImplement() {
        title = new String[]{
                "坐标x","坐标y","颜色"
        };
        //默认情况
//        data = new Object[][] {
//                {"xx",25,32,45,0xffffffff},
//                {"xx",25,32,45,0xffffffff},
//                {"xx",25,32,45,0xffffffff},
//                {"xx",25,32,45,0xffffffff},
//                {"xx",25,32,45,0xffffffff},
//                {"xx",25,32,45,0xffffffff},
//        };
        initData();
    }

    private void initData() {
        if (data == null || data.length == 0)
        {
            //data没有初始化，或者没有数据
            data = new Object[][] {
                    {"没有数据","没有数据",0}
            };
        }
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return title.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex < 0 ||
            columnIndex > getColumnCount())
            throw new IllegalArgumentException("非法参数异常");
        return title[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex < 0||
                columnIndex > getColumnCount())
            throw new IllegalArgumentException("非法参数异常");
        if (data.length != 0) {
            Object o = data[0][columnIndex];
            if (o != null) {
                return o.getClass();
            }
        }
        return title[columnIndex].getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex  > 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //判断索引
        if (rowIndex < 0 || rowIndex > getRowCount())
            throw new IllegalArgumentException("非法参数异常");
        if (columnIndex < 0 ||
                columnIndex > getColumnCount())
            throw new IllegalArgumentException("非法参数异常");
        return data[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //判断索引
        if (rowIndex < 0 || rowIndex > getRowCount())
            throw new IllegalArgumentException("非法参数异常");
        if (columnIndex < 0 ||
                columnIndex > getColumnCount())
            throw new IllegalArgumentException("非法参数异常");
        Object value = getValueAt(rowIndex, columnIndex);
        if (Objects.equals(aValue,value))
            return;
        if (aValue instanceof Integer i)
        {
            if (i > 255 || i < 0)
                throw new IllegalArgumentException("非法参数异常");
        }
        getXAndY(rowIndex);
        firstRow = lastRow = columnIndex;
        data[rowIndex][columnIndex] = aValue;
        fireColorEvent(aValue);
    }

    private void getXAndY(int rowIndex) {
        x = (int) data[rowIndex][0];
        y = (int) data[rowIndex][1];
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    public void addColorComponentListener(ColorCompomentImplement l) {
        colorCompomentImplementLists.add(l);
    }

    public void removeColorComponentListener(ColorCompomentImplement l) {
        colorCompomentImplementLists.remove(l);
    }

    public void fireColorEvent(Object value) {
        for (ColorCompomentImplement l : colorCompomentImplementLists)
        {
            l.updateColors(this,x,y,(Color) value);
        }
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }

    public void fireModelEventChange(int columnIndex) {
        TableModelEvent event = new TableModelEvent(this,x,y,columnIndex,
                TableModelEvent.UPDATE);
        for (TableModelListener l : listeners)
            l.tableChanged(event);
    }

    public void addElements(int x, int y, Color color) {
        data = Arrays.copyOf(data, id + 1); //效率太低，能否优化？？？
        data[id] = new Object[]{
                x,
                y,
                color
        };
        id++;
    }

    public void clear() {
        data = new Object[][] {
                {"没有数据","没有数据",0}
        };
        id = 0;
    }

}

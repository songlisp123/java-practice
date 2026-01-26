package com.snl.swing.homework01.data;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.*;

public class TableModelImplement implements TableModel {

    private String[] title;//标题
    private final List<TableModelListener> listeners =
            new ArrayList<>(); //监听器
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
                "坐标","R颜色分量","G颜色分量","B颜色分量","ARGB"
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
                    {"没有数据","没有数据","没有数据","没有数据","没有数据"}
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
        return columnIndex != 0;
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
        fireModelEventChange(columnIndex);
    }

    private void getXAndY(int rowIndex) {
        String o = (String)data[rowIndex][0];
        String[] sArray = o.split(":");
        String y_String = sArray[2].substring(0,sArray[2].length() - 1);
        String x_String = sArray[1].split(",")[0];
        try {
            y = Integer.parseInt(y_String);
            x = Integer.parseInt(x_String);
        }catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
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

    public void addElements(int x, int y,int[] comps, int rgb) {
        data = Arrays.copyOf(data, id + 1); //效率太低，能否优化？？？
        //获取红色分量
        int red = comps[0];
        //绿色分量
        int green = comps[1];
        //蓝色分量
        int blue = comps[2];
        //填充数据TODO
        data[id] = new Object[]{
                "[x:%d,y:%d]".formatted(x,y),
                red,
                green,
                blue,
                rgb
        };
        id++;
    }

    public void clear() {
        data = new Object[][] {
                {"没有数据","没有数据","没有数据","没有数据","没有数据"}
        };
        id = 0;
    }

}

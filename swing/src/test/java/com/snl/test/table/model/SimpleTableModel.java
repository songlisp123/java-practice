package com.snl.test.table.model;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimpleTableModel implements TableModel {

    //管理表格的数据
    protected String[] columnNames;
    protected Object[][] rowData;
    protected final List<TableModelListener> listeners = new ArrayList<>();

    public SimpleTableModel() {
        //初始化数据
        initData();
    }

    private void initData() {
        columnNames = new String[] {"姓名","生日","年龄","身高","是否处"};
        rowData = new Object[][] {
                {"赵云", LocalDateTime.now(),21,168.54,true},
                {"黄忠", LocalDateTime.now(),78,1.645,false},
                {"马超", LocalDateTime.now(),32,568.625,true},
                {"关元长", LocalDateTime.now(),56,156.23,false},
        };

    }

    @Override
    public int getRowCount() {
        return rowData.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (rowData.length != 0) {
            Object o = rowData[0][columnIndex];
            if (o != null) {
                return o.getClass();
            }
        }
        return columnNames[columnIndex].getClass();

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rowData[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //TODO 这是设置单元格的操作
        Object value = this.getValueAt(rowIndex, columnIndex);
        if (Objects.equals(aValue,value)) {
            System.out.println("无改变");
        }else {
            rowData[rowIndex][columnIndex] = aValue;
            fireChangeEvent(rowIndex,columnIndex);
        }

    }

    private void fireChangeEvent(int rowIndex, int columnIndex) {
        System.out.println("触发事件……");
        TableModelEvent tableModelEvent = new TableModelEvent(this,rowIndex,rowIndex,columnIndex);
        for(TableModelListener listener : listeners) {
            listener.tableChanged(tableModelEvent);
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
}

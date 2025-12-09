package com.snl.test.table.model;

import javax.swing.*;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

public class SimpleTableColumnModel implements TableColumnModel {

    protected Vector<TableColumn> tableColumns;
    protected final List<TableColumnModelListener> listeners = new ArrayList<>();
    protected int columnMargin;
    protected ListSelectionModel selectionModel;
    protected boolean columnSelectionAllowed;
    protected int totalColumnWidth;

    public SimpleTableColumnModel() {
        super();
        tableColumns = new Vector<>();
        setSelectionModel(createSelectionMode());
        setColumnMargin(50);
        setColumnSelectionAllowed(true);

    }

    @Override
    public void addColumn(TableColumn aColumn) {
        if (aColumn == null) {
            throw new IllegalArgumentException("非法参数异常");
        }
        tableColumns.addElement(aColumn);
    }

    @Override
    public void removeColumn(TableColumn column) {
        int indexed = tableColumns.indexOf(column);
        if (indexed != -1) {
            tableColumns.removeElementAt(indexed);
        }
    }

    @Override
    public void moveColumn(int columnIndex, int newIndex) {
        //TODO 触发事件
        if ((columnIndex < 0) || (columnIndex >= getColumnCount()) ||
                (newIndex < 0) || (newIndex >= getColumnCount()))
            throw new IllegalArgumentException("moveColumn() - Index out of range");

        TableColumn aColumn ;
        if (columnIndex == newIndex) return;

        aColumn = tableColumns.elementAt(columnIndex);
        tableColumns.removeElementAt(columnIndex);
        boolean selected = selectionModel.isSelectedIndex(columnIndex);
        selectionModel.removeIndexInterval(columnIndex,columnIndex);

        tableColumns.insertElementAt(aColumn,newIndex);
        selectionModel.insertIndexInterval(newIndex,1,true);
        if (selected) {
            selectionModel.addSelectionInterval(newIndex,newIndex);
        }else {
            selectionModel.removeSelectionInterval(newIndex,newIndex);
        }
        //手动触发移动事件
        fireColumnMoved(columnIndex,newIndex);
    }

    private void fireColumnMoved(int columnIndex, int newIndex) {
        TableColumnModelEvent tableColumnModelEvent =
                new TableColumnModelEvent(this, columnIndex, newIndex);
        for (TableColumnModelListener listener : listeners) {
            listener.columnMoved(tableColumnModelEvent);
        }

    }

    @Override
    public void setColumnMargin(int newMargin) {
        columnMargin = newMargin;
    }

    @Override
    public int getColumnCount() {
        return tableColumns.size();
    }

    @Override
    public Enumeration<TableColumn> getColumns() {
        return tableColumns.elements();
    }

    @Override
    public int getColumnIndex(Object columnIdentifier) {
        if (columnIdentifier == null) {
            System.err.println("标识符为null");
        }
        Enumeration<TableColumn> columns = this.getColumns();
        TableColumn aColumn;
        int index = 0;

        while (columns.hasMoreElements()) {
            aColumn = columns.nextElement();
            if (columnIdentifier.equals(aColumn.getIdentifier())) {
                return index;
            }
            index++;
        }
        //如果没找到，抛出异常
        throw new IllegalArgumentException("未找到属于该标识符的列");
    }

    @Override
    public TableColumn getColumn(int columnIndex) {
        return tableColumns.get(columnIndex);
    }

    @Override
    public int getColumnMargin() {
        return columnMargin;
    }

    @Override
    public int getColumnIndexAtX(int xPosition) {
        if (xPosition < 0) {
            return -1;
        }
        int cc = getColumnCount();
        for(int column = 0; column < cc; column++) {
            xPosition = xPosition - getColumn(column).getWidth();
            if (xPosition < 0) {
                return column;
            }
        }
        return -1;
    }

    @Override
    public int getTotalColumnWidth() {
        recalcWidthCache();
        return totalColumnWidth;
    }

    private void recalcWidthCache() {
        Enumeration<TableColumn> columns = getColumns();
        totalColumnWidth = 0;
        while (columns.hasMoreElements()) {
            totalColumnWidth += columns.nextElement().getWidth();
        }
    }

    @Override
    public void setColumnSelectionAllowed(boolean flag) {
        columnSelectionAllowed = flag;
    }

    @Override
    public boolean getColumnSelectionAllowed() {
        return columnSelectionAllowed;
    }

    @Override
    public int[] getSelectedColumns() {
        if (selectionModel != null) return selectionModel.getSelectedIndices();
        return new int[0];
    }

    @Override
    public int getSelectedColumnCount() {
        if (selectionModel != null) return selectionModel.getSelectedItemsCount();
        return 0;
    }

    @Override
    public void setSelectionModel(ListSelectionModel newModel) {
        if (newModel == null) {
            throw new IllegalArgumentException("异常！");
        }
        ListSelectionModel oldModel = selectionModel;

        if (newModel != oldModel) {
            if (oldModel != null) {
//                oldModel.removeListSelectionListener(this);
            }

            selectionModel= newModel;
//            newModel.addListSelectionListener(this);
        }
        selectionModel = newModel;
    }

    @Override
    public ListSelectionModel getSelectionModel() {
        return selectionModel;
    }

    @Override
    public void addColumnModelListener(TableColumnModelListener x) {
        listeners.add(x);
    }

    @Override
    public void removeColumnModelListener(TableColumnModelListener x) {
        listeners.remove(x);
    }



    private ListSelectionModel createSelectionMode() {
        return new DefaultListSelectionModel();
    }
}

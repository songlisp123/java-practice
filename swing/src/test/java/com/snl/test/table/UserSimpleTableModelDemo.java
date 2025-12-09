package com.snl.test.table;

import com.snl.test.table.model.SimpleTableColumnModel;
import com.snl.test.table.model.SimpleTableModel;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class UserSimpleTableModelDemo extends JFrame implements TableModelListener {

    protected SimpleTableModel simpleTableModel;
    protected JTable table;
    protected SimpleTableColumnModel columnModel;

    public UserSimpleTableModelDemo() throws HeadlessException {
        super("默认标签");
        initComponents();
    }

    public UserSimpleTableModelDemo(String title) throws HeadlessException {
        super(title);
        initComponents();
    }

    private void initComponents() {
        simpleTableModel = new SimpleTableModel();
        simpleTableModel.addTableModelListener(this);
        columnModel = new SimpleTableColumnModel();

        table = new JTable();
        table.setColumnModel(columnModel);
        table.setModel(simpleTableModel);
        ListSelectionModel selectionModel = table.getSelectionModel();
        System.out.println("selectionModel = " + selectionModel);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        getContentPane().add(table.getTableHeader(),BorderLayout.PAGE_START);
        getContentPane().add(table,BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        System.out.println("表格模型事件程序开始");
        TableCellEditor cellEditor = table.getCellEditor();
        System.out.println("cellEditor = " + cellEditor);
        int type = e.getType();
        int column = e.getColumn();
        int firstRow = e.getFirstRow();
        int lastRow = e.getLastRow();
        if (type == TableModelEvent.UPDATE) {
            if (firstRow == lastRow) {
                System.out.printf("行[%d]，列[%d]单元格数据已更新为：%s%n", firstRow,column,
                        simpleTableModel.getValueAt(firstRow,column));
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(UserSimpleTableModelDemo::new);
    }
}

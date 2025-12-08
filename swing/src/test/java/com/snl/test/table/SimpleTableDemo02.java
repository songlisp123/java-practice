package com.snl.test.table;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SimpleTableDemo02 extends JPanel {

    protected final boolean DEBUG = true;

    public SimpleTableDemo02() {
        super(new BorderLayout());

        String[] columnNames = {
                "名字",
                "年龄",
                "素食主义者"
        };

        Object[][] data = {
                {"赵云",54,true},
                {"李白",36,false},
                {"王者",24,true},
        };

        JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        if (DEBUG) {
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    printDebug(table);
                }
            });
        }

        add(new JScrollPane(table));

    }

    private void printDebug(JTable table) {
        //获取所有行列
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();

        TableModel model = table.getModel();
        System.out.println("表格模型:" + model);

        TableColumnModel columnModel = table.getColumnModel();
        System.out.println("列模型：" + columnModel);

        for (int i = 0;i<rowCount;i++) {
            System.out.println("   行" + i + ":");
            for (int j = 0;j<columnCount;j++) {
                System.out.println("   " + model.getValueAt(i,j));
            }
            System.out.println();
        }
        System.out.println("---------------------------");
    }

    protected static void createUi() {
        JFrame frame = new JFrame("测试");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //创建设置内容面板
        SimpleTableDemo02 tableDemo02 = new SimpleTableDemo02();
        tableDemo02.setOpaque(true); //必须的

        frame.add(tableDemo02);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(SimpleTableDemo02::createUi);
    }
}

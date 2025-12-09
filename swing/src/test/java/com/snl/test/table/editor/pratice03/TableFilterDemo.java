package com.snl.test.table.editor.pratice03;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class TableFilterDemo extends JPanel {

    private final boolean DEBUG = false;
    private JTable table;
    private JTextField filterText;
    private JTextField statusText;
    private TableRowSorter<MyTableModel> sorter;

    public TableFilterDemo() {
        super();
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));

        //创建表格过滤器
        MyTableModel model = new MyTableModel();
        sorter = new TableRowSorter<>(model);
        table = new JTable(model);
        table.setRowSorter(sorter);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        //设置单选模式
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //为用户提供信息
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow(); //这是一个视图模型的选择
            if (row < 0) {
                //无选择
                statusText.setText(" ");
            }else {
                int modelIndex = table.convertRowIndexToModel(row);
                statusText.setText("选择视图行：%d,选择模型行：%d".formatted(row,modelIndex));
            }
        });

        JScrollPane jScrollPane = new JScrollPane(table);

        add(jScrollPane);

        JPanel form = new JPanel(new SpringLayout());
        JLabel label = new JLabel("过滤文本：",SwingConstants.TRAILING);
        form.add(label);
        filterText = new JTextField();

        //当过滤文本组件发生变化调用此事件
        filterText.getDocument().addDocumentListener(
                new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        newFilter();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        newFilter();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        newFilter();
                    }
                }
        );

        label.setLabelFor(filterText);
        form.add(filterText);
        JLabel l2 = new JLabel("状态：",SwingConstants.TRAILING);
        form.add(l2);

        statusText = new JTextField();
        l2.setLabelFor(statusText);
        form.add(statusText);
        SpringUtilities.makeCompactGrid(form, 2, 2, 6, 6, 6, 6);
        add(form);

    }

    /**
     * Update the row filter regular expression from the expression in
     * the text box.
     */
    private void newFilter() {
        RowFilter<MyTableModel, Object> rf = null;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(filterText.getText(), 0);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        sorter.setRowFilter(rf);
    }


    class MyTableModel extends AbstractTableModel {
        private String[] columnNames = {"First Name",
                "Last Name",
                "Sport",
                "# of Years",
                "Vegetarian"};
        private Object[][] data = {
                {"Kathy", "Smith",
                        "Snowboarding", 5, false},
                {"John", "Doe",
                        "Rowing", 3,true},
                {"Sue", "Black",
                        "Knitting", 2, false},
                {"Jane", "White",
                        "Speed reading", 20, true},
                {"Joe", "Brown",
                        "Pool", 10, false}
        };

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 2) {
                return false;
            } else {
                return true;
            }
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        public void setValueAt(Object value, int row, int col) {
            if (DEBUG) {
                System.out.println("Setting value at " + row + "," + col
                        + " to " + value
                        + " (an instance of "
                        + value.getClass() + ")");
            }

            data[row][col] = value;
            fireTableCellUpdated(row, col);

            if (DEBUG) {
                System.out.println("New value of data:");
                printDebugData();
            }
        }

        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();
            for (int i=0; i < numRows; i++) {
                System.out.print("    row " + i + ":");
                for (int j=0; j < numCols; j++) {
                    System.out.print("  " + data[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TableFilterDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        TableFilterDemo newContentPane = new TableFilterDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

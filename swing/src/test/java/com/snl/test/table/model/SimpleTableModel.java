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
                {"赵云枪出如龙", LocalDateTime.now(),21,168.54,true},
                {"黄忠老二", LocalDateTime.now(),78,1.645,false},
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


    public User getUser(int position) {
        User user = new User();
        Object[] userData = rowData[position];
        user.setUsername((String) userData[0]);
        user.setBirthDay((LocalDateTime) userData[1]);
        user.setAge((Integer) userData[2]);
        user.setHeight((Double) userData[3]);
        user.setMan((Boolean) userData[4]);
        return user;
    }


    public class User {
        protected String username;
        protected LocalDateTime birthDay;
        protected int age;
        protected double height;
        protected boolean isMan;

        public User() {
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public boolean isMan() {
            return isMan;
        }

        public void setMan(boolean man) {
            isMan = man;
        }

        public double getHeight() {
            return height;
        }

        public void setHeight(double height) {
            this.height = height;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public LocalDateTime getBirthDay() {
            return birthDay;
        }

        public void setBirthDay(LocalDateTime birthDay) {
            this.birthDay = birthDay;
        }
    }
}

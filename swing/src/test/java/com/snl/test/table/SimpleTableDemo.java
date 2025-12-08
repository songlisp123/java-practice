package com.snl.test.table;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SimpleTableDemo extends JFrame {

    protected JTable table;
    protected Field[] fields;
    protected Object[][] data;
    protected String[] columnNames;
    protected List<Student> students;

    public SimpleTableDemo() throws HeadlessException {
        super("默认的标题");
        initComponents();
        alignAttribute();
    }

    public SimpleTableDemo(String title) throws HeadlessException {
        super(title);
        initComponents();
        alignAttribute();
    }

    private void alignAttribute() {
        setBounds(new Rectangle(200,50,600,400));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        //TODO 列表头
        //我要使用反射来获取类的字段
        students = getStudentData();
        if (students.isEmpty()) {
            columnNames = new String[]{"无表头"};
        }else {
            fields = Student.class.getDeclaredFields();
            columnNames = new String[fields.length];
            for (int i = 0; i < fields.length; i++) {
                var field = fields[i];
                String name = field.getName();
                columnNames[i] = name;
            }
        }

        //初始化几个学生的属性
        //可能从其他地方获取数据


//        data = new Object[][] {
                //先硬编码
//                {student}
//                {"赵云",54,LocalDateTime.now(),156.42,98.56}
                //完善
//                convertArray(student,fields),
//                convertArray(student1,fields)
//        };
        data = convertStudentData(students);
        table = new JTable(data,columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        getContentPane().add(table.getTableHeader(),BorderLayout.PAGE_START);
        getContentPane().add(table,BorderLayout.CENTER);

    }

    private Object[][] convertStudentData(List<Student> students) {
        Object[][] outLayer = null;
        if(students.isEmpty()) {
            outLayer = new Object[][]{
                    {"暂无数据"}
            };
        }else {
            outLayer = new Object[students.size()][];
            Object[] innerLayer = null;
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                innerLayer = convertArray(student);
                outLayer[i] = innerLayer;
            }
        }
        return outLayer;
    }

    private List<Student> getStudentData() {
        //TODO 从其他地方获取数据？
        List<Student> lists = new ArrayList<>();
        Student student = new Student("赵云", 54, LocalDateTime.now(), 156.54, 56.36);
        Student student1 = new Student("黄忠", 60, LocalDateTime.now(), 250.23, 68.36);
        lists.add(student);
        lists.add(student1);
        return lists;
    }

    private Object[] convertArray(Student student) {
        AccessibleObject.setAccessible(fields,true);
        Object[] data = new Object[fields.length];
        for (int i=0;i<fields.length;i++) {
            Field field = fields[i];
            try {
                Object o = field.get(student);
                if (o == null) {
                    continue;
                }
                data[i] = o;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(SimpleTableDemo::new);
    }
}

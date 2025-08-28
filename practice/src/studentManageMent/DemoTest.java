package studentManageMent;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DemoTest {
    public static void main(String[] args) {

//        var stu_1 = new Student("赵云",2015,6,12,98,Rank.A);
//        var stu_2 = new Student("马超",2015,6,12,78.56,Rank.A);
//        var stu_3 = new Student("刘备",2015,6,12,56,Rank.A);
//        Student.writeIntoFile();

//        var timer = new TimeringStorage(list);
//        var writer = new Writer();
//        writer.write(list);
        var m = new ManageSystem();
//        System.out.println(m.getFirstStudent());
//        System.out.println(m.totalStudents());
//        m.showTotalStudent();
//        m.getIndexStudent(56);
        System.out.println(m.totalStudents());
//        System.out.println(m.getIndexStudent(0));
        m.showTotalStudent();
    }
}

package studentManageMent;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class FileInputDemo {
    public static void main(String[] args) {
        var stu_1 = new Student("赵云", 2015, 6, 12, 98, Rank.A);
        var stu_2 = new Student("马超", 2015, 6, 12, 78.56, Rank.A);
        var stu_3 = new Student("刘备", 2015, 6, 12, 56, Rank.A);

        //创建一个数组存放学生数组
        Student[] students = new Student[]{stu_1, stu_2, stu_3};
        //创建一一个输出流PrintWriter类
        /*
        FileOutputStream:一个低级流可以将输入转换为字节
        PrintWriter:一个高级流,可以向文本文件里面输入文本
         */
        try (PrintWriter printWriter = new PrintWriter(new FileOutputStream("1.dat"))) {
            printWriter.println(5);
            printWriter.println(stu_1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

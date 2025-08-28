package studentManageMent;

import java.io.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Reader {

    private File file;

    public Reader(String path) {
        file = new File(path);
    }

    public  List<Student> read() {
        List<Student> alist = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f:files) {
                try {
                    System.out.println(f);
                    var fileInputStream = new FileInputStream(f);
                    var objectInputStream = new ObjectInputStream(fileInputStream);
                    Student t = (Student) objectInputStream.readObject();
                    alist.add(t);
                    System.out.println("读取成功！");
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("读取完毕！");
            return alist;
        }
        return new ArrayList<>();
    }

    public <T> List<T> read(boolean b) {
        try {
            var fileInputStream = new FileInputStream(".//data//student//student.dat");
            var objectInputStream = new ObjectInputStream(fileInputStream);
            List<T> t = (List<T>) objectInputStream.readObject();
            System.out.println("读取成功!");
            return t;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return List.of();
    }
}

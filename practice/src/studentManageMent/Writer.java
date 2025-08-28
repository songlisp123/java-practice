package studentManageMent;

import java.io.*;
import java.util.List;

public class Writer {

    public Writer() {
    }

    public void write(Student a) {
        try {
            var fileOutputStream = new FileOutputStream(".//data//student//"
                    +a.getName()+".dat");
            var objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(a);
            System.out.println("写入完毕！");
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> void write(List<T> a) {

        try {
             var fileOutputStream = new FileOutputStream(".//data//student//"
                    +"student.dat");
            var objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(a);
            System.out.println("写入完毕!");
            objectOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

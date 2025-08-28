package studentManageMent;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class TimeringStorage {
    private Timer timer;

    public TimeringStorage(ArrayList<Student> a) {
        timer  = new Timer(5000,event->{
            for (Student s:a) {
                try {
                    var fileOutputStream = new FileOutputStream(".//data//student//"+
                            s.getName()+".dat");
                    var  objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(s);
                    System.out.println("写入成功！");
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        timer.start();
        JOptionPane.showMessageDialog(null, "^退出程序^");
        timer.setRepeats(false);
    }
}

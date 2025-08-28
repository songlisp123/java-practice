package input和outPut;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Input {
    public static void main(String[] args) {
        File file = new File("test.dem");
        try( var out = new PrintWriter(file, StandardCharsets.UTF_8))
        {
            String name = "你好世界!";
            LocalDateTime currentTime = LocalDateTime.now();
            double salary = 1250.36;
            int age = 12;
            out.println(name);
            out.println(age);
            out.println(salary);
            out.println(currentTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.apress.bgn.char03.input;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;
public class Fileinput {
    public static void main(String[] args) throws IOException {
        try {
            Scanner scanner = new Scanner(Path.of("//home//snl//caraasdad"),
                    StandardCharsets.UTF_8);
            while (scanner.hasNext()) System.out.println(scanner.nextLine());
        }catch (IOException e) {
            e.getCause();
            e.getMessage();
        }

    }
}

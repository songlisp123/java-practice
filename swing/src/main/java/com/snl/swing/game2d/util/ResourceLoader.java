package com.snl.swing.game2d.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ResourceLoader {

    public static InputStream load(Class<?> t,String filePath,String resPath) {
        System.out.println("resPath = " + resPath);
        InputStream in = null;
        if (!(resPath == null || resPath.isEmpty())) {
            in = t.getResourceAsStream(resPath);
        }
        if (in == null)
        {
            //尝试文件路径
            try {
                in = new FileInputStream(filePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return in;
    }
}

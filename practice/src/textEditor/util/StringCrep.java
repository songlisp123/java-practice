package textEditor.util;

import java.nio.charset.StandardCharsets;

public class StringCrep {
    public static void main(String[] args) {
        String s = "我的世界";
        for (int i = 0;i<s.length();i++) {
            String c = s.substring(i,i+1);
            System.out.println("===============");
            System.out.println(c);
            for (byte b : c.getBytes(StandardCharsets.UTF_8)) {
                System.out.println(b);
            }
        }
    }
}

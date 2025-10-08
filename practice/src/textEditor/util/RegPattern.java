package textEditor.util;

import java.util.regex.Pattern;

public class RegPattern {
    private static final Pattern reg = Pattern.compile(".+\\.(mp3|flac)$");
    public static void main(String[] args) {
        String s = "周杰伦.mp3";
        System.out.println(reg.pattern());
        System.out.println(Pattern.matches(reg.pattern(),s));
    }
}

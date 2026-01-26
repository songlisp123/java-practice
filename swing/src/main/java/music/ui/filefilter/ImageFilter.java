package music.ui.filefilter;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.regex.Pattern;

public class ImageFilter extends FileFilter {

    protected static final Pattern pattern =
            Pattern.compile(".+\\.(png|gif|jepg|jpg|tif|tiff)$");

    @Override
    public boolean accept(File f) {
        return f.getPath().matches(pattern.pattern());
    }

    @Override
    public String getDescription() {
        return "仅图片";
    }
}

package textEditor;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;
import java.io.File;

public class imageView extends FileView {
    private Icon icon;
    private FileFilter fileFilter;

    public imageView(FileFilter fileFilter,Icon aicon) {
        this.fileFilter = fileFilter;
        this.icon = aicon;
    }

    public Icon getIcon(File file) {
        if (!file.isDirectory()&&file.toString().endsWith("txt")
                &&!fileFilter.accept(file)) return new ImageIcon(".//icons8-谷歌文档-32.png");
        else if (!file.isDirectory()&&file.toString().endsWith("pdf")
                &&!fileFilter.accept(file)) return new ImageIcon(".//icons8-pdf-48.png");
        else if(!file.isDirectory()&&fileFilter.accept(file)) return icon;
        else return null;
    }
}

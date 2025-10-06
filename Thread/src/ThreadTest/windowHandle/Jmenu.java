package ThreadTest.windowHandle;

import javax.swing.*;

public class Jmenu extends JMenuBar {

    //设置第一级子项目
    public Jmenu() {
        super();
        var fileMenu = new JMenu("文件");
        var helpMenu = new JMenu("帮助");
        JMenuItem openItem = fileMenu.add("打开");
        fileMenu.addSeparator();
        openItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        JMenuItem save = fileMenu.add("保存");
        save.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        JMenuItem helpItem = helpMenu.add("帮助");
        helpItem.setEnabled(false);
        add(fileMenu);
        add(helpMenu);
    }
}

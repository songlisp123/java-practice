package wormsubject;

import javax.swing.*;
import java.awt.*;

public class MenuBar extends JMenuBar {
//    private Dialog dialog;
//    private JPanel jPanel;
    public MenuBar() {
        super();

        //添加一级子项目
        var fileMenu = new JMenu("文件");
        var helpMenu = new JMenu("帮助");
        var exitMenu = new JMenu("退出");

        //添加二级子项目
        var saveItem = fileMenu.add("保存");
        saveItem.setIcon( new ImageIcon(".//icons8-保存-50.png"));

        var helpButton = helpMenu.add("帮助");
        var exitButton = exitMenu.add("退出");
        exitMenu.addSeparator();
        var quitButton = exitMenu.add("关闭");

        //添加事件监听器
        exitButton.addActionListener(event->{
            System.exit(0);
        });
        quitButton.addActionListener(event->{
            System.exit(0);
        });

        //将二级子项目添加到一级子项目上
        helpMenu.add(helpButton);
        exitMenu.add(exitButton);
        exitMenu.add(quitButton);

        //将一级子项目添加到菜单栏上面
        this.add(fileMenu);
        this.add(helpMenu);
        this.add(exitMenu);
    }
}

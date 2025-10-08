package textEditor;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class MenuBar extends JMenuBar {
//    private Dialog dialog;
    private JPanel jPanel;
    private JLabel jLabel;
    private JFrame jFrame;
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private JLabel label;
//    private JToolBar toolBar;
    private panel panel;

    public MenuBar(JFrame jFrame) {
        super();
        this.jFrame = jFrame;
        jLabel = new JLabel();
        label = new JLabel(new ImageIcon(".//icons8-谷歌文档-32.png"));
        jPanel = new JPanel();
        textArea = new JTextArea();
//        toolBar = new JToolBar("工具栏");
        textArea.setCaretColor(Color.WHITE);

        panel = new panel(textArea);
        this.jFrame.add(panel,BorderLayout.NORTH);
        /*
        向工具栏添加用用
         */
//        toolBar.add(new myAction(Color.WHITE,"白色"));
//        toolBar.addSeparator();
//        toolBar.add(new myAction(Color.GREEN,"绿色"));
//        toolBar.addSeparator();
//        toolBar.add(new myAction(Color.RED,"红色"));
//        toolBar.addSeparator();
//        toolBar.add(new myAction(Color.CYAN,"青色"));


//        this.jFrame.add(toolBar,BorderLayout.WEST);
        textArea.setEditable(true);
        textArea.setLineWrap(true);
        textArea.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        var border = BorderFactory.createTitledBorder("记事本");
        fileChooser = new JFileChooser();
        //添加一级子项目
        var fileMenu = new JMenu("文件");
        var helpMenu = new JMenu("帮助");
        var exitMenu = new JMenu("退出");

        //添加二级子项目
        var newFile = fileMenu.add("新建");
        fileMenu.addSeparator();
        newFile.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
        newFile.setIcon( new ImageIcon(".//icons8-创建新的-50.png"));
        var saveItem = fileMenu.add("保存");
        saveItem.setIcon( new ImageIcon(".//icons8-保存-50.png"));
        fileMenu.addSeparator();
        saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        var saveAs = fileMenu.add("另存为");
        saveAs.setIcon( new ImageIcon(".//icons8-另存为-50.png"));
        fileMenu.addSeparator();
        saveAs.setAccelerator(KeyStroke.getKeyStroke("ctrl alt S"));
        var openItem = fileMenu.add("打开");
        openItem.setIcon(new ImageIcon(".//icons8-打开文件夹-50.png"));
        openItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        var helpButton = helpMenu.add("帮助");
        var exitButton = exitMenu.add("退出");
        helpButton.setAccelerator(KeyStroke.getKeyStroke("ctrl Q"));
        exitMenu.addSeparator();
        var quitButton = exitMenu.add("关闭");

        //添加事件监听器
        exitButton.addActionListener(event->{
            System.exit(0);
        });
        quitButton.addActionListener(event->{
            System.exit(0);
        });

        newFile.addActionListener(event->{
            fileChooser.setCurrentDirectory(new File("."));
            //显示文件
            int result = fileChooser.showDialog(jFrame,"创建");
            File file = fileChooser.getSelectedFile();
            String path = file.getPath();
            if (!file.exists()) {
                try {
                    boolean f = file.createNewFile();
                    System.out.println("创建文件【%s】成功".formatted(path));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                int answer = JOptionPane.showConfirmDialog(jFrame,"是否覆盖此文件？",
                        "创建新文件",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
                if (answer == JOptionPane.OK_OPTION) {
                    try {
                        file.deleteOnExit();
                        boolean f = file.createNewFile();
                        System.out.println("创建文件【%s】成功！".formatted(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                textArea.setText(write(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            label.setText(file.toString());
        });

        openItem.addActionListener(event->{
            fileChooser.setCurrentDirectory(new File("."));
            //显示文件选择框
            int result =fileChooser.showOpenDialog(jFrame);
            File file = fileChooser.getSelectedFile();
            if (result == JFileChooser.APPROVE_OPTION)
            {
                String name = file.getPath();
                if (file.toString().endsWith(".txt") || file.toString().endsWith(".java")) {
                    try {
                        textArea.setText(write(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (name.endsWith("gif")|| name.endsWith("png")
                )   jLabel.setIcon(new ImageIcon(name));
                else {
                    try {
                        jLabel.setText(write(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            label.setText(file.toString());
        });

        saveItem.addActionListener(event->{
            fileChooser.setCurrentDirectory(new File("."));
            int result = fileChooser.showDialog(jFrame,"保存");
            File file = fileChooser.getSelectedFile();
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
//                    System.out.println(textArea.getText());
                    save(textArea.getText(),file);
                    System.out.println("文件保存成功！");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });

        //将二级子项目添加到一级子项目上
        helpMenu.add(helpButton);
        exitMenu.add(exitButton);
        exitMenu.add(quitButton);
        jFrame.add(jLabel);
        jFrame.add(label,BorderLayout.SOUTH);
        jFrame.add(new JScrollPane(textArea),BorderLayout.CENTER);
        //添加文件过滤器
        var filter = new FileNameExtensionFilter(
                "txt files", "txt");
        var filter01 = new FileNameExtensionFilter(
                "image files", "png","jpg","gif","jpeg");
        var javaFilter = new FileNameExtensionFilter("java file",
                "java","class"
        );
        fileChooser.setFileFilter(filter);
        fileChooser.addChoosableFileFilter(filter01);
        fileChooser.addChoosableFileFilter(javaFilter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        //将一级子项目添加到菜单栏上面
        this.add(fileMenu);
        this.add(helpMenu);
        this.add(exitMenu);

        border.setTitleFont(new Font("宋体",Font.BOLD,12));
        border.setTitlePosition(TitledBorder.BELOW_TOP);
        border.setTitleColor(Color.CYAN);
        textArea.setBackground(new Color(30, 31, 34));
        textArea.setForeground(Color.CYAN);
        textArea.setBorder(border);
        fileChooser.setAccessory(new JScrollPane(new ImagePreviewer(fileChooser)));
        fileChooser.setFileView(new imageView(filter01, new ImageIcon(".//palette.gif")));

    }

    public String write(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] b = fileInputStream.readAllBytes();
        String result = new String(b, StandardCharsets.UTF_8);
        fileInputStream.close();
        return result;
    }

    public void save(String out,File f) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(new BufferedOutputStream(
                new FileOutputStream(f)
        ));
        printWriter.println(out);
        printWriter.close();
    }


    private class myAction extends AbstractAction {
        private Color c;
        private String name;
        public myAction(Color c,String name) {
            this.c = c;
            this.name = name;
            putValue(NAME,name);
            putValue("color",c);
            putValue(SHORT_DESCRIPTION,"将文本变成%s".formatted(name));
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            Color c = (Color) this.getValue("color");
            textArea.setForeground(c);
        }
    }
}

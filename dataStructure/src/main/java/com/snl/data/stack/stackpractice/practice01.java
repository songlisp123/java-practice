package com.snl.data.stack.stackpractice;

import com.snl.data.stack.ArrayStack;
import com.snl.swing.practice.button.CustomButton;
import com.snl.swing.practice.filefilter.JFileChooserDemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class practice01 extends JPanel {

    protected JButton run;
    protected JFileChooser fileChooser;
    protected File currentSelectedFile;
    protected File oldSelectedFile;
    protected Task task;
    protected ArrayStack<Character> stack;
    protected JTextArea area;

    public practice01() {
        super(new BorderLayout());
        init();
    }

    private void init() {
        setBackground(Color.black);
        run = new CustomButton("挑选文件");
        run.addActionListener(new ChooseFile());
        run.setToolTipText("挑选java文件读取java文本结构");

        fileChooser = new JFileChooserDemo();
        stack = new ArrayStack<>();

        area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setForeground(Color.BLACK);

        alignSpace();
    }

    private void alignSpace() {
        LayoutManager layout = getLayout();
        if (!(layout instanceof GridBagLayout)) {
            layout = new GridBagLayout();
            setLayout(layout);
        }
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.weighty = 0.1f;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(10,5,10,5);
        constraints.anchor = GridBagConstraints.PAGE_START;
        add(run,constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weighty = 0.7f;
        constraints.weightx = 1.0f;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(10,50,10,50);
        JScrollPane jScrollPane = new JScrollPane(area);
        add(jScrollPane,constraints);

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(700,600);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2 = (Graphics2D) g.create();

        g2.setColor(Color.green);
        g2.scale(1.25,1.25);
        g2.drawString("这是关于栈的知识,使用栈的数据结构来模拟编译器检查语法错误",10,70);
        g2.drawString("如果出现未闭合的()或者{}则将报错",10,90);
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        var problem = new practice01();
        frame.add(problem);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(practice01::createUi);
    }

    class ChooseFile implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int choice = fileChooser.showOpenDialog(null);
            if (choice == JFileChooser.APPROVE_OPTION) {
                oldSelectedFile = currentSelectedFile;
                currentSelectedFile = fileChooser.getSelectedFile();
                if (currentSelectedFile.isFile() &&
                        currentSelectedFile.canRead() &&
                        currentSelectedFile.getName().endsWith("java")) {
                    //TODO 读取文件内容但是io程序需要另一个线程
                    if (task != null && !task.isDone()) {
                        task.cancel(true);
                    }
                    area.setText("");
                    task = new Task();
                    task.execute();
                }
            }else {
                System.err.println("用户取消选择");
            }
        }
    }

    class Task extends SwingWorker<Void,String> {

        @Override
        protected Void doInBackground() throws Exception {
            AtomicInteger line = new AtomicInteger(0);
            try(Stream<String> lines =  Files.lines(currentSelectedFile.toPath(), StandardCharsets.UTF_8)) {
                lines.forEach(string -> {
                    line.getAndIncrement();
                    if (task.isCancelled()) {
                        return;
                    }
                    for (int i=0;i<string.length();i++) {
                        char c = string.charAt(i);
                        switch (c) {
                            case '(' :
                            case '[' :
                            case '{' :
                                stack.push(c);
                                break;
                            case ')' :
                            case ']' :
                            case '}' :
                                if (stack.isEmpty())
                                    printErrorMessage(line,i);
                                Character pop = stack.pop();
                                if ((c == ')' && pop != '(') ||
                                        (c == ']' && pop != '[') ||
                                        (c == '}' && pop != '{'))
                                    printErrorMessage(line,i);
                                break;
                            default:
                                break;//默认无操作
                        }
                    }
                    publish(string);
                });
            }
            return null;
        }

        @Override
        protected void process(List<String> chunks) {
            for (String s : chunks) {
                area.append(s+"\n");
            }
        }

        @Override
        protected void done() {
            Toolkit.getDefaultToolkit().beep();
            if (!stack.isEmpty()) System.err.println("语法错误发生在最后一行");
        }

        private void printErrorMessage(AtomicInteger line, int i) {
            System.err.printf("第 [%d] 行 索引 [%d] 处发生语法错误%n",line.get(),i);
        }
    }

}

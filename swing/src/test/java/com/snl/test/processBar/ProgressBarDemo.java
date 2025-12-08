package com.snl.test.processBar;

import com.snl.swing.practice.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

public class ProgressBarDemo extends JPanel implements ActionListener, PropertyChangeListener {

    private CustomButton startButton;
    private JTextArea area;
    private JProgressBar progressBar;
    private Task task;
    private boolean done;

    public ProgressBarDemo() {
        super(new BorderLayout());
        done = false;
        startButton = new CustomButton("开始");
        startButton.setActionCommand("start");
        startButton.addActionListener(this);

        progressBar = new JProgressBar(0,100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        area = new JTextArea(5,30);
        area.setMargin(new Insets(5,5,5,5));
        area.setEditable(false);

        JPanel jPanel = new JPanel();
        jPanel.add(startButton);
        jPanel.add(progressBar);

        add(jPanel,BorderLayout.PAGE_START);
        add(new JScrollPane(area),BorderLayout.CENTER);
        setBorder(BorderFactory.createTitledBorder("日志区"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        startButton.setEnabled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //javax.swing.SwingWorker的实例不可重复使用，我们根据需要创建新实例
        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
//        if ("state".equals(propertyName)) {
//            System.out.println("程序开始");
//        }
//        if ("progress".equals(propertyName)) {
//            int newValue = (Integer)evt.getNewValue();
//            progressBar.setValue(newValue);
//            area.append("已完成 %d%n".formatted(task.getProgress()));
////            area.append("已完成 %d%n".formatted(value));
//        }
        if (!done) {
            if ("progress".equals(propertyName)) {
                int progress = task.getProgress();
                progressBar.setValue(progress);
                area.append("已完成 %d%n".formatted(task.getProgress()));
            }else {
                System.out.println("开始程序");
            }
        }
    }

    protected class Task extends SwingWorker<Void,Void> {

        /**
         * 后台线程,在后台运行
         * @return {@code null}
         * @throws Exception 异常运营
         */
        @Override
        protected Void doInBackground()  {
            Random random = new Random();
            int progress = 0;
            //初始化属性
            setProgress(0);
            while (progress<100) {
                //将线程睡眠几秒
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException e) {
                    //使用随机线程

                }
                progress += random.nextInt(10);
                setProgress(Math.min(progress,100));
            }
            return null;
        }

        /**
         * 这是程序做完之后将要调用的东西
         */
        @Override
        protected void done() {
            Toolkit.getDefaultToolkit().beep();
            startButton.setEnabled(true);
            setCursor(null); //关闭等待鼠标
            progressBar.setValue(progressBar.getMinimum());
            area.append("做完!\n");
        }
    }


    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ProgressBarDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = new ProgressBarDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

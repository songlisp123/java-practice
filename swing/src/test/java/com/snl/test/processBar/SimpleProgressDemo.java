package com.snl.test.processBar;

import com.snl.swing.practice.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Random;


public class SimpleProgressDemo extends JFrame implements PropertyChangeListener, ActionListener {

    protected JProgressBar progressBar;
    protected CustomButton button;
    protected Task task;
    protected JLabel label;
    protected int total;

    public SimpleProgressDemo()  {
        initComponents();
    }

    public SimpleProgressDemo(String title)  {
        super(title);
        initComponents();
    }

    private void initComponents() {

        this.total = 300;

        button = new CustomButton("点击我");
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(this);

        label = new JLabel("任务进度",JLabel.CENTER);


        progressBar = new JProgressBar(new CustomBoundedRangeModel(0,total));
        label.setLabelFor(progressBar);

        progressBar.addPropertyChangeListener(this);
        progressBar.setForeground(Color.cyan);
        progressBar.setBackground(Color.black);
        progressBar.setStringPainted(true);

        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.add(progressBar,BorderLayout.CENTER);
        jPanel.add(label,BorderLayout.LINE_START);
        getContentPane().add(jPanel,BorderLayout.CENTER);
        getContentPane().add(button,BorderLayout.PAGE_START);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(new Rectangle(200,50,600,500));
        setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {



        Object newValue = evt.getNewValue();
        System.out.println("newValue = " + newValue);

        if (newValue instanceof SwingWorker.StateValue) {
            Thread currentThread = Thread.currentThread();
            System.out.println("currentThread = " + currentThread);
            var value = (SwingWorker.StateValue) newValue;
            if (value == SwingWorker.StateValue.STARTED) {
                System.out.println("开始后台任务");
            } else if (value == SwingWorker.StateValue.DONE) {
                System.out.println("后台处理成功！");
            }
        }



        if (newValue instanceof Integer) {
            int v = (Integer) newValue;
            System.out.println("v = " + v);
        }

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(SimpleProgressDemo::new);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        BoundedRangeModel model = progressBar.getModel();
        System.out.println("model = " + model);
        button.setEnabled(false);
        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
    }

    protected class Task extends SwingWorker<Void,Integer> {

        private int process;

        /**
         * 后台线程,在后台运行
         * @return {@code null}
         * @throws Exception 异常运营
         */
        @Override
        protected Void doInBackground()  {
            System.out.println(1);
            Thread currentThread = Thread.currentThread();
            System.out.println("当前线程 = " + currentThread);
            Random random = new Random();
            process = 0;
            //初始化属性
            setProgress(process);
            while (process<total) {
                //将线程睡眠几秒
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException e) {
                    //使用随机线程

                }
                process += random.nextInt(10);
                publish(Math.min(process,total));
            }
            return null;
        }

        /**
         * 这是程序做完之后将要调用的东西
         */
        @Override
        protected void done() {
            System.out.println(2);
            Thread currentThread = Thread.currentThread();
            System.out.println("线程 = " + currentThread);
            Toolkit.getDefaultToolkit().beep();
            button.setEnabled(true);
            setCursor(null); //关闭等待鼠标
            progressBar.setValue(progressBar.getMinimum());
        }

        /**
         * 处理中间结果的过程函数
         * @param chunks intermediate results to process
         *
         */
        @Override
        protected void process(List<Integer> chunks) {
            Thread thread = Thread.currentThread();
            System.out.println("thread = " + thread);
            int v = chunks.getLast();
            System.out.println("newValue=" + v + ", max=" + progressBar.getMaximum()
                    + ", percent=" + progressBar.getPercentComplete());

            progressBar.setValue(v);
        }
    }


}

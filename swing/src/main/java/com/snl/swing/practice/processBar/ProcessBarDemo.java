package com.snl.swing.practice.processBar;

import com.snl.swing.practice.CustomButton;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Random;

public class ProcessBarDemo extends JPanel implements ChangeListener , ActionListener , PropertyChangeListener {

    protected BoundedRangeModel rangeModel;
    protected JProgressBar bar;
    protected final int total = 500;
    protected GridBagLayout gridBagLayout;
    protected JTextArea area;
    protected CustomButton button;
    protected JLabel label;

    public ProcessBarDemo() {
        init();
    }

    public ProcessBarDemo(LayoutManager layout) {
        super(layout);
        init();
    }

    private void init() {
        rangeModel = new SimpleRangeBoundModelModel(0,total);
        rangeModel.addChangeListener(this);
        bar = new JProgressBar(rangeModel);
        bar.setStringPainted(true);
        bar.setForeground(Color.GREEN);
        area = new JTextArea(5,30);
        button = new CustomButton("点击运行");
        button.addActionListener(this);
        label = new JLabel("进度：");
        label.setLabelFor(bar);

        LayoutManager layout = getLayout();
        if (!(layout instanceof GridBagLayout)) {
            gridBagLayout = new GridBagLayout();
            setLayout(gridBagLayout);
        }
        GridBagConstraints constraints = new GridBagConstraints();
        alignComponentSpace(constraints);
    }

    private void alignComponentSpace(GridBagConstraints constraints) {
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0.0f;
        add(label,constraints);

        //排序进度条
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.weightx = 1.0f;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0,5,0,5);
        add(bar,constraints);

        //安装按钮
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = 1;
        constraints.weightx =0.5f;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0,5,0,5);
        add(button,constraints);

        //添加文本
        JPanel jPanel = new JPanel(new BorderLayout());
        jPanel.add(area);
        jPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("日志打印去"),
                BorderFactory.createEmptyBorder(5,5,5,5)
        ));
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(jPanel,constraints);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        System.out.println("模型数据发生变化的时候调用");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Toolkit.getDefaultToolkit().beep();
        button.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        button.setEnabled(false);
        Task task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        System.out.println("propertyName = " + propertyName);
        var newValue = evt.getNewValue();
        if (newValue instanceof SwingWorker.StateValue) {
            Thread thread = Thread.currentThread();
            System.out.println("thread = " + thread);
            if (newValue == SwingWorker.StateValue.STARTED) {
                System.out.println("后台任务开始");
            }else {
                System.out.println("后台任务结束");
            }
        }
    }

    class Task extends SwingWorker<Void,Integer> {

        protected int process;

        public Task() {
            process = 0;
        }

        @Override
        protected Void doInBackground() throws Exception {
            Random random = new Random();
            setProgress(process);
            while (process < total) {
                process += random.nextInt(10);
                try {
                    Thread.sleep(random.nextInt(1000));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                publish(Math.min(process,total));
            }
            return null;
        }

        @Override
        protected void done() {
            Toolkit.getDefaultToolkit().beep();
            button.setEnabled(true);
            setCursor(null);
            bar.setValue(bar.getMinimum());
        }

        @Override
        protected void process(List<Integer> chunks) {
            Integer last = chunks.getLast();
            System.out.println("newValue=" + last + ", max=" + bar.getMaximum()
                    + ", percent=" + bar.getPercentComplete());
            area.setText("已完成：%.2f%n".formatted(bar.getPercentComplete()));
            bar.setValue(last);
        }
    }
}

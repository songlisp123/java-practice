package com.snl.data.homework.charptor03.practice01;

import com.snl.data.homework.charptor03.practice01.CONSTANTS.GameConstants;
import com.snl.data.homework.charptor03.practice01.state.InputState;
import com.snl.data.homework.charptor03.practice01.text.Description;
import com.snl.data.homework.charptor03.practice01.text.Text;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.random.RandomGenerator;

public class BootPanel extends JPanel implements PropertyChangeListener {

    private JProgressBar bar;
    private Task task;
    private JLabel label;
    private final RandomGenerator generator =
            RandomGenerator.getDefault();
    private JFrame frame;
    private JDialog mainPanel;
    private JLabel statusLabel;
    private ImageCreator creator;
    private InputState state;

    /**
     * 操作的步骤
     */
    private int mode;

    private Text description;

    public BootPanel() {
        initDate();
    }

    public BootPanel(JFrame frame) {
        this(null,frame);
    }

    public BootPanel(InputState state, JFrame frame) {
        this.state = state;
        this.frame = frame;
        initDate();
    }

    private void initDate() {
        setBackground(Color.black);
        creator = new ImageCreator(this);
        label = new JLabel(creator.createIcon(null),JLabel.CENTER);
        label.setForeground(Color.GREEN);

        statusLabel = new JLabel("界面正在加载……",JLabel.LEFT);
        statusLabel.setForeground(Color.GREEN);
        statusLabel.setFont(new Font("隶书",Font.BOLD,20));

        bar = new JProgressBar(0,100);
        bar.setStringPainted(true);
        mode = -1;
        description = new Description();

        setLayout(new BorderLayout());
        add(bar,BorderLayout.SOUTH);
        add(label,BorderLayout.CENTER);
        add(statusLabel,BorderLayout.NORTH);
        doTask();
    }

    private void doTask() {
        if (task != null && !task.isDone()) {
            return;
        }
        task = new Task();
        task.addPropertyChangeListener(this);
        task.execute();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(GameConstants.Weight,GameConstants.Height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2 = (Graphics2D) g.create();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newValue = evt.getNewValue();
        if (newValue instanceof SwingWorker.StateValue) {
            var value = (SwingWorker.StateValue) newValue;
            if (value == SwingWorker.StateValue.STARTED) {
                System.out.println("开始后台任务");
            } else if (value == SwingWorker.StateValue.DONE) {
                System.out.println("后台任务完成");
                mainPanel = new ShowConfirmMessage(this.frame,state);
            }
        }
    }

    class Task extends SwingWorker<Void,Void> {

        int process;

        @Override
        protected Void doInBackground() throws Exception {
            process = 0;
            int r ;
            while (process < 100) {
                r = generator.nextInt(1,10);
                process += r;
                publish();
                Thread.sleep(500);
            }
            return null;
        }

        @Override
        protected void process(List<Void> chunks) {
            if (process > 100)
                process = 100;
            bar.setValue(process);
            if (process < 15) {
                if (mode == -1)
                    mode++;
                label.setIcon(creator.createIcon("images/赛朋博客1.png"));
            }
            else if (process < 30) {
                if (mode == 0)
                    mode++;
                label.setIcon(creator.createIcon("images/赛朋博客2.png"));
            }
            else if(process < 45) {
                if (mode == 1)
                    mode++;
                label.setIcon(creator.createIcon("images/赛朋博客3.png"));
            }
            else if(process < 60) {
                if (mode == 2)
                    mode++;
                label.setIcon(creator.createIcon("images/赛朋博客4.png"));
            }
            else if(process < 80) {
                if (mode == 3)
                    mode++;
                label.setIcon(creator.createIcon("images/赛朋博客5.png"));
            }
            else {
                if (mode == 4)
                    mode++;
                label.setIcon(creator.createIcon("images/赛朋博客6.png"));
            }
            statusLabel.setText(description.getString(mode));
        }

        @Override
        protected void done() {
            Music.beep();
        }
    }

}

package com.snl.test.music;

import audio.ChooseBestMixer;
import audio.ChooseSourceLine;
import com.snl.swing.practice.button.CustomButton;
import com.snl.swing.practice.filefilter.JFileChooserDemo;
import com.snl.test.music.processBar.RangeBoundModelModelImplement;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.random.RandomGenerator;
import java.util.stream.Stream;

public class MusicPanelDemo extends JPanel  {

    protected final List<Rectangle2D> rectangles =
            new ArrayList<>();
    protected final double GAP = 5;
    protected final int COUNT = 280;
    protected final RandomGenerator generator =
            RandomGenerator.getDefault();
    protected JPanel buttonPanel;
    protected JLabel label;
    protected JProgressBar bar;
    protected JButton playButton;
    protected JButton nextButton;
    protected JButton previousButton;
    protected JButton stopButton;
    /**
     * 定时任务
     */
    protected Task task;
    protected JFileChooser fileChooser;
    protected File selectFile;
    /**
     * 暂停或者播放
     */
    protected boolean playing = false;
    /**
     * 选择的按钮
     */
    protected JButton selectFileButton;
    /**
     * 旧的文件
     */
    protected File oldFile = selectFile;
    /**
     * 音乐时长
     */
    protected int musicLength;

    protected JPanel bmpPanel;

    protected final BlockingQueue<Short> blockingQueue =
            new LinkedBlockingQueue<>();

    protected final List<Path> musicPaths = new ArrayList<>();

    protected int currentIndex = -1;

    public MusicPanelDemo(LayoutManager layout) {
        super(layout);
        init();
    }

    public MusicPanelDemo() {
        init();
    }

    private void init() {
        setBackground(Color.black);
        //设置任务
        var taskToFindMusic = new FindMusicPathClass();
        taskToFindMusic.execute();

        for (int i = 0;i<=COUNT;i++) {
            var rec = new RectangleDemo(i*(RectangleDemo.WEIGHT + GAP),
                    120,generator.nextDouble(COUNT));
            rectangles.add(rec);
        }

        label = new JLabel("测试计划",JLabel.CENTER);
        label.setIcon(new ImageIcon("sound.gif"));
        label.setForeground(Color.YELLOW);

        buttonPanel = new MyButtonPanel();

        fileChooser = new JFileChooserDemo();

        bar = new JProgressBar(new RangeBoundModelModelImplement(0));
        bar.setForeground(Color.GREEN);
        bar.setStringPainted(true);

        bmpPanel = new BMPanel();

        alignSpace();
    }

    private void alignSpace() {
        LayoutManager layout = getLayout();
        if (!(layout instanceof GridBagConstraints)) {
            layout = new GridBagLayout();
            setLayout(layout);
        }
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.5f;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(20,0,0,20);
        add(label,constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.RELATIVE;;
        constraints.weightx = 0.1f;
        constraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel jLabel = new JLabel("进度：", JLabel.RIGHT);
        jLabel.setForeground(Color.YELLOW);
        add(jLabel,constraints);

        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1f;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        label.setLabelFor(bar);
        add(bar,constraints);

        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1f;
        constraints.weighty = 1.0f;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(20,0,0,0);
        add(bmpPanel,constraints);

        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 1f;
        constraints.weighty = 0.0f;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        constraints.fill = GridBagConstraints.BOTH;
        add(buttonPanel,constraints);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2 = (Graphics2D) g.create();
        g2.scale(1.5,1.5);
        if (playing) {
            g2.setColor(Color.green);
        }else {
            g2.setColor(Color.RED);
        }
        g2.fillOval(10,15,10,10);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600,500);
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");
        var panel = new MusicPanelDemo();
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(MusicPanelDemo::createUi);
    }

    class Task extends SwingWorker<Void,Integer> {

        protected int process;
        protected float seconds;

        public Task() {
            process = 0;
        }

        @Override
        protected Void doInBackground() throws Exception {
            setProgress(process);
            Mixer mixer = ChooseBestMixer.chooseMixer();
            if (mixer == null) {
                throw new RuntimeException("暂未找到该供应商的混音器!");
            }
            try(AudioInputStream stream = AudioSystem.getAudioInputStream(selectFile)) {
                AudioFileFormat audioFileFormat = AudioSystem.getAudioFileFormat(selectFile);
                AudioFormat format = stream.getFormat();
                int frameLength = audioFileFormat.getFrameLength();
                //获取音乐时长
                musicLength = (int) (frameLength / format.getFrameRate());
                bar.setMaximum(musicLength);
                //播放音乐
                SourceDataLine sourceDataLine = ChooseSourceLine.chooseLine(mixer);
                sourceDataLine.open(format);
                sourceDataLine.start();

                byte[] storeBytes = new byte[4096];
                int read = stream.read(storeBytes, 0, 4096);
                float gain = 0f;
                float fadeInStep = 1f / (44100 * 50); // 100秒淡入
                while (read != -1) {
                    if (task.isCancelled()) {
                        sourceDataLine.drain();
                        sourceDataLine.close();
                        break;
                    }
                    if (!playing) {
                        sourceDataLine.stop();
                        continue;
                    }else {
                        sourceDataLine.start();
                    }
                    for (int i = 0; i < read; i+=2) {
                        short sample = (short) ((storeBytes[i + 1] << 8) | (storeBytes[i] & 0xff));
                        float s = sample * gain;
                        short newSample = (short) s;
                        blockingQueue.put(newSample);
                        storeBytes[i] = (byte) (newSample & 0xff);
                        storeBytes[i+1] = (byte) ((newSample >> 8) & 0xff);
                        if (gain < 1f) gain += fadeInStep;
                    }
                    sourceDataLine.write(storeBytes,0,read);
                    int framePosition = sourceDataLine.getFramePosition();
                    seconds = framePosition / format.getFrameRate();
                    publish((int) seconds);
                    read = stream.read(storeBytes, 0, 4096);
                }
                sourceDataLine.drain();
                sourceDataLine.close();
            }
            return null;
        }

        @Override
        protected void process(List<Integer> chunks) {
            var last = chunks.getLast();
            bar.setValue(last);
        }

        @Override
        protected void done() {
            Toolkit.getDefaultToolkit().beep();
            playButton.setEnabled(true);
            setCursor(null);
            label.setText("播放完毕");
            playing = false;
            blockingQueue.clear();
            repaint();
            bar.setValue(bar.getMinimum());
        }
    }

    class MyButtonPanel extends JPanel {

        public MyButtonPanel() {
            setBackground(Color.BLACK);
            init();
        }

        public MyButtonPanel(LayoutManager layout) {
            super(layout);
            init();
        }

        private void init() {
            setBorder(BorderFactory.createTitledBorder("音频操作"));
            playButton = new CustomButton("播放");
            playButton.setEnabled(false);
            playButton.addActionListener((event)->{
                if (task == null || task.isDone() || task.isCancelled()) {
                    task = new Task();
                    task.execute();
                }
                playing = true;
                stopButton.setEnabled(true);
                playButton.setEnabled(false);
                label.setText("正在播放："+selectFile.getName()+"……");
            });

            stopButton = new CustomButton("暂停");
            stopButton.setEnabled(false);
            stopButton.addActionListener(e -> {
                if (playing) playing = false;
                stopButton.setEnabled(false);
                playButton.setEnabled(true);
                label.setText("暂停播放："+selectFile.getName()+"……");
            });

            selectFileButton = new CustomButton("挑选文件");
            selectFileButton.addActionListener(new PlayButtonImplement());

            previousButton = new CustomButton("上一个");
            previousButton.addActionListener(e -> {
                if (currentIndex == -1) {
                    System.out.println("当前尚未有选择");
                    return;
                }
                currentIndex--;
                if (currentIndex < 0) {
                    currentIndex = 0;
                }
                Path path = musicPaths.get(currentIndex);
                oldFile = selectFile;
                selectFile = path.toFile();
                updateButtonState();
                if (task != null && !task.isDone()) {
                    task.cancel(true);
                }
            });
            nextButton = new CustomButton("下一个");
            nextButton.addActionListener(e -> {
                if (currentIndex == -1) {
                    System.out.println("当前尚未有选择");
                    return;
                }
                currentIndex++;
                if (currentIndex > musicPaths.size() - 1) {
                    currentIndex = musicPaths.size() - 1;
                }
                Path path = musicPaths.get(currentIndex);
                oldFile = selectFile;
                selectFile = path.toFile();
                updateButtonState();
                if (task != null && !task.isDone()) {
                    task.cancel(true);
                }
            });

            alignSpace();
        }

        private void alignSpace() {
            LayoutManager layout = getLayout();
            if (!(layout instanceof GridBagConstraints)) {
                layout = new GridBagLayout();
                setLayout(layout);
            }

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = new Insets(10,10,10,10);
            constraints.anchor = GridBagConstraints.EAST;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.gridwidth = 1;
            constraints.weightx = 0.5f;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            add(previousButton,constraints);

            constraints.anchor = GridBagConstraints.CENTER;
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.gridwidth = 1;
            constraints.weightx = 1.0f;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            add(playButton,constraints);

            constraints.anchor = GridBagConstraints.CENTER;
            constraints.gridx = 2;
            constraints.gridy = 0;
            constraints.gridwidth = GridBagConstraints.RELATIVE;
            constraints.weightx = 1.0f;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            add(stopButton,constraints);

            constraints.anchor = GridBagConstraints.EAST;
            constraints.gridx = 3;
            constraints.gridy = 0;
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            constraints.weightx = 0.5f;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            add(nextButton,constraints);

            constraints.anchor = GridBagConstraints.CENTER;
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            constraints.weightx = 0.5f;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            add(selectFileButton,constraints);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(600,200);
        }
    }

    class PlayButtonImplement implements ActionListener {
        //播放和逻辑
        @Override
        public void actionPerformed(ActionEvent e) {

            int choice = fileChooser.showOpenDialog(null);
            if (choice == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (file != null && file.isFile()
                        && file.canRead()
                        && file.toString().endsWith("wav")) {
                    //逻辑
                    oldFile = selectFile;
                    selectFile = file;
                    updateButtonState();
                    System.out.println("selectFile = " + selectFile);
                }else {
                    System.err.println("文件不符合要求！请重试！");
                }
            }else {
                System.out.println("用户取消操作");
            }
        }
    }

    private void updateButtonState() {

        if (!Objects.equals(oldFile,selectFile) && task != null) {
            task.cancel(true);
        }

        if (selectFile != null) {
            //处理逻辑
            for (int i = 0;i<musicPaths.size();i++) {
                Path path = musicPaths.get(i);
                if (Objects.equals(path.toFile(),selectFile)) {
                    currentIndex = i;
                    selectFile = path.toFile();
                }
            }
            System.out.println("currentIndex = " + currentIndex);
            if (currentIndex == -1) {
                throw new RuntimeException("文件尚未找到！");
            }
            playButton.setEnabled(true);
            selectFileButton.setText("挑选文件："+selectFile.getPath());
        }else {
            playButton.setEnabled(false);
            selectFileButton.setText("挑选文件");
        }
    }

    class BMPanel extends JPanel {

        Timer timer;
        protected JLabel label;

        public BMPanel() {
            super(new BorderLayout());
            setBorder(BorderFactory.createTitledBorder("动感地带~"));
            setBackground(Color.black);
            label = new JLabel("制造人宋宁龙~~💗",JLabel.CENTER);
            label.setForeground(Color.YELLOW);
            add(label);
            timer = new Timer(16,e -> updatePanel());
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            var g2 = (Graphics2D) g.create();
            super.paintComponent(g2);
//            for (Rectangle2D rec : rectangles) {
//                var r = (RectangleDemo) rec;
//                if (r.getHeight() < 50) {
//                    g2.setColor(Color.CYAN);
//                }else if (r.getHeight() < 100){
//                    g2.setColor(Color.GREEN);
//                }else if (r.getHeight() < 150){
//                    g2.setColor(Color.PINK);
//                }else if (r.getHeight() < 250){
//                    g2.setColor(Color.red);
//                }else {
//                    g2.setColor(Color.ORANGE);
//                }
//                g2.fill(r);
//            }
            g2.dispose();
        }

        private void updatePanel() {
            int x = label.getX();
            int width = super.getWidth();
            if (x < -(width / 2 + 50) ) {
                x = width / 2 + 50;
            }
            x--;
            label.setLocation(x, label.getY());
//            for (Rectangle2D rec : rectangles) {
//                var r = (RectangleDemo) rec;
//                try {
//                    Short take = blockingQueue.poll();
//                    if (take != null) {
//                        r.setHeight(take);
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//
            repaint();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(600,500);
        }
    }

    class FindMusicPathClass extends SwingWorker<Void,Path> {

        @Override
        protected Void doInBackground() throws Exception {
            try(Stream<Path> stream = Files.walk(Path.of(System.getProperty("user.dir")))) {
                stream.filter(path -> path.toFile().isFile())
                        .filter(path -> path.toString().endsWith("wav"))
                        .forEach(musicPaths::add);
            }
            return null;
        }

        @Override
        protected void done() {
            Toolkit.getDefaultToolkit().beep();
            System.out.println("任务完成");
        }
    }
}

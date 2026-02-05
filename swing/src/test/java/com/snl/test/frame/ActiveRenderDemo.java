package com.snl.test.frame;

import com.snl.test.frame.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ActiveRenderDemo extends JFrame implements Runnable {

    FrameV2 v2;
    Thread gameThread;
    JLabel label;
    boolean running;

    public ActiveRenderDemo() throws HeadlessException {
        super("测试框架");
        initial();
    }

    public ActiveRenderDemo(String title) throws HeadlessException {
        super(title);
        initial();
    }

    private void initial() {
        v2 = new FrameV2(); //帧率计算
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                 Utils.showClosingDialog(null);
            }
        });
        createAndShowUi();
        gameThread = new Thread(this,"游戏线程");
        gameThread.start();
    }

    private void createAndShowUi() {
        GamePanel gp = new GamePanel();
        this.getContentPane().add(gp);
        Utils.centerContainer(this);
        this.pack();
        this.setVisible(true);
    }

    @Override
    public void run() {
        running = true;
        while (running)
        {
            v2.calculateFrameRate();
            label.setText(v2.getFrameRate());
            sleep(16);
        }
    }

    private void sleep(long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class GamePanel extends JPanel {

        public GamePanel() {
            setBackground(Color.black);
            label = new JLabel(v2.getFrameRate(),JLabel.CENTER);
            label.setForeground(Color.GREEN);
            add(label,NORMAL);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(400,300);
        }

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(ActiveRenderDemo::new);
    }

}

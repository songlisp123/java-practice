package wormsubject.game;

import wormsubject.util.CustomButton;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Objects;

public class setPanel extends SimpleFrame {
    private CustomButton startButton;
    private CustomButton resumeButton;
    private CustomButton setButton;
    private CustomButton aboutMNe;
    private JPanel panel;
    private AboutDialog dialog;
    private D dialog2;
    private jpanel<practice01> jpanel;
    private static final int GAP = 150;

    public setPanel(int width,int height) {
        super(width,height);
        this.setResizable(false);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // 垂直排列

        startButton = new CustomButton("开始游戏");
        setButton = new CustomButton("设置");
        aboutMNe = new CustomButton("关于我");

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        setButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutMNe.setAlignmentX(Component.CENTER_ALIGNMENT);

        //平均分配空间
        int gap = allocateSpace(height,startButton,setButton,aboutMNe);
        panel.add(Box.createVerticalGlue());    // 顶部留空白
        panel.add(startButton);
        panel.add(Box.createVerticalStrut(gap));  // 间隔
        panel.add(setButton);
        panel.add(Box.createVerticalStrut(gap));  // 间隔
        panel.add(aboutMNe);
        panel.add(Box.createVerticalGlue());     // 底部自动撑开
        panel.setBackground(new Color(30, 31, 34));
        add(panel);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setVisible(true);

        startButton.addActionListener(event->{
            practice01 practice01 = new practice01();
            if (Objects.isNull(jpanel)) {
                jpanel = new jpanel<>(40,40,800,800,practice01);
            }else {
                if (jpanel.getTimer() == null ) {
                    //程序暂停
                }
            }
            setVisible(false);

        });
        setButton.addActionListener(event->{
            if (dialog == null) {
                System.out.println("新建设置弹窗");
                dialog = new AboutDialog(this);
            }
            dialog.setVisible(true);
        });
        aboutMNe.addActionListener(event->{
            if (dialog2 == null) {
                System.out.println("新建关于弹窗");
                dialog2 = new D(this);
            }
            dialog2.setVisible(true);
        });

    }

    private int allocateSpace(int height, CustomButton...buttons) {
        int totalHeightOfButtons = 0;
        int gap = 0;
        if (Objects.isNull(buttons) || buttons.length == 0) {
            gap = GAP;
        }else {
            for (CustomButton button : buttons) {
                int buttonHeight = button.getDefaultHeight();
                totalHeightOfButtons += buttonHeight;
            }
            System.out.println("总高度="+totalHeightOfButtons);
            gap = (height - totalHeightOfButtons) / (buttons.length + 1);
        }
        System.out.println("间隔式："+gap);
        return gap;
    }


    private class AboutDialog extends JDialog {
        private JSlider audioSlider;
        private JSlider lightSlider;
        private JLabel audioLabel;
        private JLabel lightLabel;
        private CustomButton okButton;
        private JPanel panel;

        public AboutDialog(JFrame owner) {
            super(owner,"设置",true);
            setBounds(owner.getX(),owner.getY(),owner.getWidth(),owner.getWidth());
            audioSlider = new JSlider(0,100);
            lightSlider = new JSlider(0,100);
//            audioSlider.set
            var hashtable = new Hashtable<>();
            var hashtable02 = new Hashtable<>();
            audioSlider.setPaintLabels(true);
            lightSlider.setPaintLabels(true);
            hashtable.put(0,new JLabel("低"));
            hashtable.put(100,new JLabel("高"));

            hashtable02.put(0,new JLabel("暗"));
            hashtable02.put(100,new JLabel("亮"));

            audioSlider.setLabelTable(hashtable);
            lightSlider.setLabelTable(hashtable02);

            setLayout(new BorderLayout());

            audioLabel = new JLabel("音量",SwingConstants.CENTER);
            lightLabel = new JLabel("亮度",SwingConstants.CENTER);

            okButton = new CustomButton("确定");
            add(okButton,BorderLayout.SOUTH);
            okButton.addActionListener(event->{
                setVisible(false);
            });


            panel = new JPanel(new GridLayout(2,2,20,20));
            panel.add(audioLabel);
            panel.add(audioSlider);
            panel.add(lightLabel);
            panel.add(lightSlider);
            add(panel);

        }
    }

    private class D extends JDialog {
        private JPanel jPanel;
        private JLabel label;
        private CustomButton button;

        public D(JFrame owner) {
            super(owner,"关于我",true);
            setLayout(new BorderLayout());
            jPanel = new JPanel();
            label = new JLabel("""
                    <html>
                    <h1>
                    <i>
                    更多信息：
                    </i>
                    </h1>
                    <p>
                    <a href="https://5278.cc/forum.php?mod=viewthread&tid=1577440&extra=page%3D4%26filter%3Dtypeid%26typeid%3D1358">
                    点击我查看更多😄
                    </a>
                    </p>
                    <hr>
                    宋宁龙
                    </html>
                    """
                    , SwingConstants.CENTER);

            button = new CustomButton("确定");
            setBounds(owner.getX(),owner.getY(),owner.getWidth(),owner.getWidth());
            jPanel.add(label);
            add(jPanel);
            add(button,BorderLayout.SOUTH);
            button.addActionListener(event->{
                this.setVisible(false);
            });
        }
    }
}

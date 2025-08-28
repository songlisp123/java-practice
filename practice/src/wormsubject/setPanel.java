package wormsubject;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class setPanel extends SimpleFrame {
    private CustomButton startButton;
    private CustomButton setButton;
    private CustomButton aboutMNe;
    private JPanel panel;
    private AboutDialog dialog;
    private D dialog2;

    public setPanel(int width,int height) {
        super(width,height);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // 垂直排列

        startButton = new CustomButton("开始游戏");
        setButton = new CustomButton("设置");
        aboutMNe = new CustomButton("关于我");

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        setButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        aboutMNe.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(150));  // 顶部留空白
        panel.add(startButton);
        panel.add(Box.createVerticalStrut(150));  // 间隔
        panel.add(setButton);
        panel.add(Box.createVerticalStrut(150));  // 间隔
        panel.add(aboutMNe);
        panel.add(Box.createVerticalGlue());     // 底部自动撑开
        panel.setBackground(new Color(30, 31, 34));
        add(panel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        startButton.addActionListener(event->{
            var frame = new practice01();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            setVisible(false);
        });
        setButton.addActionListener(event->{
            if (dialog == null) {
                dialog = new AboutDialog(this);
            }
            dialog.setVisible(true);
        });
        aboutMNe.addActionListener(event->{
            if (dialog2 == null) {
                dialog2 = new D(this);
            }
            dialog2.setVisible(true);
        });

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
            setSize(500,400);
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
                    By Cay Horstmann
                    </html>
                    """
                    , SwingConstants.CENTER);

            button = new CustomButton("确定");
            setSize(500,400);
            jPanel.add(label);
            add(jPanel);
            add(button,BorderLayout.SOUTH);
            button.addActionListener(event->{
                setVisible(false);
            });
        }
    }
}

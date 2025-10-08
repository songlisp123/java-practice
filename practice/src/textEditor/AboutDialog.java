package textEditor;

import javax.swing.*;
import java.awt.*;

/**
 * 这个类继承swing弹窗系统，用来关闭当前应用程序
 */
public class AboutDialog extends JDialog {
    private JButton jButton;
    private JButton jButton2;
    private JPanel panel;
    private JLabel label;
//    private JFrame jFrame;
    public AboutDialog(JFrame owner) {

        super(owner,"退出",true);

        label = new JLabel("退出程序");
        add(label,BorderLayout.CENTER);
        jButton = new JButton("是");
        jButton2 = new JButton("否");
        jButton.addActionListener(event->{
            System.exit(0);
        });
        jButton2.addActionListener(event->{
            this.setVisible(false);
        });
        panel = new JPanel();
        panel.add(jButton);
        panel.add(jButton2);
        add(panel,BorderLayout.SOUTH);

        pack();
    }
}

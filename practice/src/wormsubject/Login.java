package wormsubject;

import javax.swing.*;
import java.awt.*;

public class Login extends JDialog {
    private JPanel panel;
    private CustomButton button;
    private JLabel usernameLabel;
    private JLabel passwordLabel;

    private JTextField userNameText;
    private JPasswordField passwordText;

    public Login(JFrame owner) {
        super(owner,"登录界面",true);
        setLayout(new BorderLayout());
        panel = new JPanel(new GridLayout(2,2));
        usernameLabel = new JLabel("用户名称：",SwingConstants.RIGHT);
        userNameText = new JTextField(20);
        passwordLabel = new JLabel("密码：",SwingConstants.RIGHT);
        passwordText = new JPasswordField(20);
        passwordText.setEchoChar('#');
        panel.add(usernameLabel);
        panel.add(userNameText);
        panel.add(passwordLabel);
        panel.add(passwordText);
        add(panel,BorderLayout.CENTER);
        setVisible(true);
        panel.setSize(600,500);
        button = new CustomButton("确定");
        button.addActionListener(event->{
            setVisible(false);
        });
        add(button,BorderLayout.SOUTH);
    }
}

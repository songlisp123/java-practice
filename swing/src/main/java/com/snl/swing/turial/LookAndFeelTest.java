package com.snl.swing.turial;

import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class LookAndFeelTest implements ActionListener {

    private static final String labelPrefix = "按钮点击了：";
    private int click = 0;
    private final JLabel jLabel = new JLabel(labelPrefix + click);
    private final String[] LOOKANDFEELS = new String[] {"default","Ocean","Test"};
    private static final  String THEME = "Test";
    private static final String LOOKANDFEEL = "Metal";

    @Override
    public void actionPerformed(ActionEvent e) {
        click++;
        jLabel.setText(labelPrefix + click);
    }

    public Component createComponent() {
        JButton button = new JButton("点击我，老登");
        button.setMnemonic(KeyEvent.VK_I);
        button.addActionListener(this);
        jLabel.setLabelFor(button);

        JPanel jPanel = new JPanel();
        jPanel.add(button);
        jPanel.add(jLabel);
        jPanel.setBorder(BorderFactory.createEmptyBorder(
                30,
                30,
                10,
                30
        ));
        return jPanel;
    }

    //初始化外观
    public static void initLookAndFeel() {
        String lookAndLabel = null;
        if (Objects.nonNull(LOOKANDFEEL)) {
            if (Objects.equals(LOOKANDFEEL,"Metal")) {
                lookAndLabel = UIManager.getCrossPlatformLookAndFeelClassName();
            } else if (Objects.equals(LOOKANDFEEL,"System")) {
                lookAndLabel = UIManager.getSystemLookAndFeelClassName();
            } else if (Objects.equals(LOOKANDFEEL,"Motif")) {
                lookAndLabel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
            } else if (Objects.equals(LOOKANDFEEL,"GTK")) {
                lookAndLabel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
            }else {
                System.err.println("未预料的值："+LOOKANDFEEL);
                lookAndLabel = UIManager.getCrossPlatformLookAndFeelClassName();
            }
            try {
                System.out.println("lookAndLabel = " + lookAndLabel);
                UIManager.setLookAndFeel(lookAndLabel);
                if (LOOKANDFEEL.equals("Metal")) {
                    if (THEME.equals("Ocean")) {
                        MetalLookAndFeel.setCurrentTheme(new OceanTheme());
                    } else if (THEME.equals("DefaultMetal")) {
                        MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
                    }else {
                        MetalLookAndFeel.setCurrentTheme(new ThemeDemo());
                    }
                    UIManager.setLookAndFeel(new MetalLookAndFeel());
                }
            } catch (UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void createAndShowUi() {
        //设置主题和外观
        initLookAndFeel();

        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("主题和外观");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        LookAndFeelTest test = new LookAndFeelTest();
        Component contents = test.createComponent();
        frame.getContentPane().add(contents,BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(LookAndFeelTest::createAndShowUi);
    }
}

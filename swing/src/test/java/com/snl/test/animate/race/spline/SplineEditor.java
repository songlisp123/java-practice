package com.snl.test.animate.race.spline;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.HeadlessException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SplineEditor extends JFrame {
    public SplineEditor() throws HeadlessException {
        super("Spline Editor");
        add(buildHeader(), BorderLayout.NORTH);
        add(buildControlPanel(), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private Component buildHeader() {
        ImageIcon icon = new ImageIcon("./images/simulator.png");
        HeaderPanel header = new HeaderPanel(icon,
                                             "样条曲线编辑器",
                                             "拖动控制点改变曲线形状",
                                             "点击复制生成形状.");
        return header;
    }

    private Component buildControlPanel() {
        return new SplineControlPanel();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException e) {
                } catch (InstantiationException e) {
                } catch (IllegalAccessException e) {
                } catch (UnsupportedLookAndFeelException e) {
                }
                
                new SplineEditor().setVisible(true);
            }
        });
    }
}

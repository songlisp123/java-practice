package com.snl.data.homework.charptor03.practice01;

import com.snl.data.homework.charptor03.practice01.button.CustomButton;
import com.snl.data.homework.charptor03.practice01.mainPanel.GameLoop;
import com.snl.data.homework.charptor03.practice01.state.InputState;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowConfirmMessage extends JDialog implements ActionListener {

    //自定义按钮
    private final String[] buttons = {"是的，进入","不，不需要"};
    private JButton yesButton;
    private JButton noButton;
    private JLabel label;
    private Container parent;
    private ImageCreator creator;

    private JPanel panel;

    private InputState state;

    /**
     * 操作模式:
     * -1:否
     * 1:是
     * 0-默认行为,无操作
     */
    private int opMode;

    public ShowConfirmMessage(Container parent,String title,boolean isModel,InputState state) {
        super((Frame) parent,title,isModel);
        this.parent = parent;
        this.state = state;
        setLocationRelativeTo(parent);
        initData();
    }

    public ShowConfirmMessage(Container parent,String title) {
        this(parent,title,true,null);
    }

    public ShowConfirmMessage(Container parent) {
        this(parent,"应用程序?");
    }

    public ShowConfirmMessage(Container parent,InputState state) {
        this(parent,"启动程序",true,state);
    }

    private void initData() {

        panel = new JPanel();
        panel.setBackground(Color.black);
        yesButton = new CustomButton(buttons[0]);
        yesButton.addActionListener(this);
        noButton = new CustomButton(buttons[1]);
        noButton.addActionListener(this);
        creator = new ImageCreator(this);

        label = new JLabel(creator.createIcon("ten.gif"),JLabel.CENTER);
        label.setForeground(Color.GREEN);
        label.setText("你正在进入🔞游戏,是否要继续??");
        alignSpace();

        pack();
        setVisible(true);
    }

    private void alignSpace() {
        LayoutManager layout = panel.getLayout();
        if (!(layout instanceof BorderLayout)) {
            layout = new BorderLayout();
            panel.setLayout(layout);
        }
//        var c = new GridBagConstraints();
//
//        c.anchor = GridBagConstraints.CENTER;
//        c.gridx = 0;
//        c.gridy = 1;
//        c.weightx = 1.0f;
//        c.weighty = 1.0f;
//        c.gridwidth = GridBagConstraints.REMAINDER;
//        c.fill = GridBagConstraints.VERTICAL;
//        panel.add(label,c);
//
//        c.anchor = GridBagConstraints.SOUTH;
//        c.gridx = 0;
//        c.gridy = 0;
//        c.weightx = 0.5f;
//        c.weighty = 1.0f;
//        c.gridwidth = GridBagConstraints.RELATIVE;
//        c.fill = GridBagConstraints.NONE;
//        c.insets = new Insets(0,10,30,0);
//        panel.add(yesButton,c);
//
//        c.gridx = 1;
//        c.gridwidth = GridBagConstraints.REMAINDER;
//        panel.add(noButton,c);

        JPanel jPanel = new JPanel(new GridBagLayout());

        var c = new GridBagConstraints();
        c.anchor = GridBagConstraints.SOUTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5f;
        c.weighty = 1.0f;
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0,10,30,0);
        jPanel.add(yesButton,c);

        c.gridx = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        jPanel.add(noButton,c);

        jPanel.setBackground(Color.black);
        panel.add(label,BorderLayout.CENTER);
        panel.add(jPanel,BorderLayout.SOUTH);
        getContentPane().add(panel);
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600,500);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == yesButton) {
            opMode = 1;
        }else
            opMode = -1;
        handleTask();
    }

    private void handleTask() {
        switch (opMode) {
            case -1:
                //否
                this.setVisible(false);
                break;
            case 1:
                //进入程序
                var p = new GameLoop(state,parent);
                ((JFrame)parent).getContentPane().add(p);
                Music.backGroundMusic();
                parent.revalidate();
                parent.repaint();
                this.setVisible(false);
                break;
            case 0:
            default:
                break;
        }
    }

}

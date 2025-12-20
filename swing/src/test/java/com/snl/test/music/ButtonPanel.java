package com.snl.test.music;

import com.snl.swing.practice.button.CustomButton;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel {
    protected JButton playButton;
    protected JButton nextButton;
    protected JButton previousButton;

    public ButtonPanel() {
        setBackground(Color.BLACK);
        init();
    }

    public ButtonPanel(LayoutManager layout) {
        super(layout);
        init();
    }

    private void init() {
        playButton = new CustomButton("播放");
        previousButton = new CustomButton("上一个");
        nextButton = new CustomButton("下一个");
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
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.weightx = 1.0f;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(playButton,constraints);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weightx = 0.5f;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(nextButton,constraints);
    }


}

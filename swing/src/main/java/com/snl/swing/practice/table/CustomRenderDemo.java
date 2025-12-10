package com.snl.swing.practice.table;

import com.snl.swing.practice.CustomButton;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CustomRenderDemo extends JLabel implements TableCellRenderer {
//    protected CustomButton button;
//    protected JProgressBar bar;
//    protected GridBagLayout layout;

    protected Icon icon;

    public CustomRenderDemo() {
        setOpaque(true);
        init();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        if (table == null) throw new IllegalArgumentException("表格不存在!");
        if (isSelected) {
            setBackground(new Color(184, 207, 229));
        }else {
            setBackground(null);
        }
        if (hasFocus) {
//            setFont(new Font(getFont().getFontName(),Font.BOLD,getFont().getSize() + 5));
            setBorder(BorderFactory.createMatteBorder(0,0,0,5,Color.green));
        }else {
            setBorder(null);
            setFont(null);
        }
        return this;
    }

    private void init() {
        icon = new ImageIcon("sound.gif");
//        button = new CustomButton("播放",new ImageIcon("sound.gif"));
//        bar = new JProgressBar(0,100);
//        bar.setStringPainted(true);
//        bar.setForeground(Color.GREEN);
//        label = new JLabel("播放/试听",JLabel.CENTER);
//        if (!(getLayout() instanceof GridBagLayout)) {
//            layout = new GridBagLayout();
//            setLayout(layout);
//        }else {
//            layout = (GridBagLayout) getLayout();
//        }
        setIcon(icon);
        alignSpace();
    }

    private void alignSpace() {
//        GridBagConstraints constraints = new GridBagConstraints();
//        constraints.gridx = 0;
//        constraints.gridy = 0;
//        constraints.gridwidth = GridBagConstraints.RELATIVE;
//        constraints.gridheight = 1;
//        constraints.weightx = 0.0f;
//        add(label,constraints);
//
//        constraints.gridx = 1;
//        constraints.gridy = 0;
//        constraints.gridwidth = GridBagConstraints.REMAINDER;
//        constraints.gridheight = 1;
//        constraints.weightx = 0.0f;
//        add(button,constraints);
//
//        constraints.gridx = 1;
//        constraints.gridy = 0;
//        constraints.gridwidth = GridBagConstraints.REMAINDER;
//        constraints.gridheight = 1;
//        constraints.weightx = 1.0f;
//        constraints.fill = GridBagConstraints.HORIZONTAL;
//        constraints.anchor = GridBagConstraints.WEST;
//        add(bar,constraints);
    }
}

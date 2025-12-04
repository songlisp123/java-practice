package com.snl.swing.practice;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class ColorChooserDemo extends JColorChooser implements ChangeListener, CaretListener {

    protected Color color;
    protected JTextPane pane;
    protected int start;
    protected int end;

    public JTextPane getPane() {
        return pane;
    }

    public void setPane(JTextPane pane) {
        this.pane = pane;
    }

    public ColorChooserDemo() {
        super(Color.RED);
        getSelectionModel().addChangeListener(this);
        setBorder(BorderFactory.createTitledBorder("颜色选择器"));
        setPreviewPanel(new JPanel()); //去掉预览面板
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        color = this.getColor();
        if (getPane() != null) {
            updateStyle(pane,color);
        }
    }

    private void updateStyle(JTextPane pane, Color color) {
        StyledDocument styledDocument = pane.getStyledDocument();
        Style style = styledDocument.getStyle("regular");
        if (style == null) {
            style = styledDocument.addStyle("regular",null);
        }
        StyleConstants.setForeground(style,color);
        styledDocument.setCharacterAttributes(start,end-start,style,true);
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        int mark = e.getMark();
        int dot = e.getDot();
        if (dot>=mark) {
            setStart(mark);
            setEnd(dot);
        }else {
            setStart(dot);
            setEnd(mark);
        }
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}

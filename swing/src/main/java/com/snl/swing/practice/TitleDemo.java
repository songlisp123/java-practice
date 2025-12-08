package com.snl.swing.practice;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class TitleDemo extends JComboBox<String> implements ItemListener, CaretListener {

    protected int start;
    protected int end;
    protected JTextPane pane;

    public TitleDemo() {
        addItem("默认无格式");
        addItem("H1");
        addItem("H2");
        addItem("H3");
        addItem("正文");
        setSelectedItem(0);
        addItemListener(this);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        String item = (String)e.getItem();
        if (e.getStateChange() == ItemEvent.SELECTED) {
            System.out.println("当前选项："+item);
            updateStyle(item);
        }else {
            System.out.println("上次选项 = " + item);
        }
    }

    private void updateStyle(String item) {
        if (getPane() != null) {
            StyledDocument styledDocument = pane.getStyledDocument();
            Style style = null;
            if ("H1".equals(item)) {
                style = styledDocument.getStyle("H1");
            }else if ("H2".equals(item)){
                style = styledDocument.getStyle("H2");
            } else if ("H3".equals(item)) {
                style = styledDocument.getStyle("H3");
            } else if ("正文".equals(item)) {
                style = styledDocument.getStyle("TEXT");
            } else {
                style = styledDocument.getStyle("regular");
            }
            styledDocument.setCharacterAttributes(start,end-start,style,true);
        }
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        if (getPane() != null) {
            int dot = e.getDot();
            int mark = e.getMark();
            if (dot >= mark) {
                setStart(mark);
                setEnd(dot);
            }else {
                setStart(dot);
                setEnd(mark);
            }
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

    public JTextPane getPane() {
        return pane;
    }

    public void setPane(JTextPane pane) {
        this.pane = pane;
    }
}

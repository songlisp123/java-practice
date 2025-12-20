package com.snl.swing.practice.combox.app;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class CustomBoxTest<T extends JTextComponent> extends JComboBox<String> implements ItemListener, CaretListener {

    protected T t;
    protected int start;
    protected int end;

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    protected static final String[] init = {
            "12",
            "14",
            "16",
            "18",
            "25",
            "30",
            "40"
    };

    public CustomBoxTest() {
        super(init);
        this.addItemListener(this);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        String item = (String) e.getItem();
        if (e.getStateChange() == ItemEvent.SELECTED) {
            System.out.println("当前选择的数字：" + item);
            //TODO 修改文本面板的逻辑
            if (getT() != null) {
                updateStyle(this.t,item);
            }
        }
        else {
            System.out.println("上一个数字："+item);
        }
    }

    protected <U extends String> void updateStyle(T t,U u) {
        if (t instanceof JTextPane) {
            //处理逻辑
//            System.out.println("你好，我是文本面板");
            System.out.println("改变文本大小："+u);
            StyledDocument styledDocument = ((JTextPane) t).getStyledDocument();
            Style regular = styledDocument.getStyle("regular");
            StyleConstants.setFontSize(regular,Integer.parseInt(u));
            //应用到整个文档
            styledDocument.setCharacterAttributes(start,end-start,regular,true);
        }else {
            System.err.println("改组件并不支持风格化文档！");
            throw new RuntimeException("❌：请改换适合风格化文档的文本组件");
        }
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        if (getT() != null) {
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
}

package com.snl.swing.practice.combox.app;

import com.snl.swing.practice.combox.model.FontComboxModel;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.JTextComponent;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class SimpleFontComBoxDemo<T extends JTextComponent> extends JComboBox<String> implements ItemListener , CaretListener, ListDataListener {

    protected T t;
    protected int start;
    protected FontComboxModel fontComboxModel;

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

    protected int end;

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    protected String[] fontArrays;
    protected final String DEFAULT_NAME = "楷体";

    public SimpleFontComBoxDemo() {
        fontComboxModel = new FontComboxModel();
        fontComboxModel.addListDataListener(this);
        setModel(fontComboxModel);
        this.addItemListener(this);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        //TODO
        String item = (String) e.getItem();
        if (e.getStateChange() == ItemEvent.SELECTED) {
            System.out.println("当前选择的字体类型是：" + item);
            //TODO 修改文本面板的逻辑有bug
            if (getT() != null) {
                updateStyle(this.t,item,null);
            }
        }
        else {
            System.out.println("⬆️："+item);
        }
    }

    protected <U extends String> void updateStyle(T t,U u,CaretEvent event) {
        if (t instanceof JTextPane) {
            //处理逻辑
//            System.out.println("你好，我是文本面板");
            System.out.println("开始处理文本风格化操作……"+u);
            //TODO 更改文字类型属性
            StyledDocument styledDocument = ((JTextPane) t).getStyledDocument();
            Style regular = styledDocument.getStyle("regular");
            StyleConstants.setFontFamily(regular,u);
            //应用到整个文档
            styledDocument.setCharacterAttributes(start,end-start,regular,false);
        }else {
            System.err.println("改组件并不支持风格化文档！");
            throw new RuntimeException("❌：请改换适合风格化文档的文本组件");
        }
    }

    @Override
    public void caretUpdate(CaretEvent e) {
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

    public void contentsChanged(ListDataEvent event) {
        System.out.println("变更事件");
        if (event.getType() == ListDataEvent.CONTENTS_CHANGED) {
            int index0 = event.getIndex0();
            System.out.println("index0 = " + index0);
            int index1 = event.getIndex1();
            System.out.println("index1 = " + index1);
            String elementAt = fontComboxModel.getElementAt(index0);
            System.out.println("取消选择:" + elementAt);
            String elementAt1 = fontComboxModel.getElementAt(index1);
            System.out.println("新选择:" + elementAt1);
        }
    }

}

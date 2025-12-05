package com.snl.swing.practice;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

public class FontComBoxDemo<T extends JTextComponent> extends JComboBox<String> implements ItemListener , CaretListener {

    protected T t;
    protected int start;

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

    public FontComBoxDemo() {
        //获取系统的所有字体？
        fontArrays = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();
        Arrays.stream(fontArrays).forEach(super::addItem);
//        获取默认的字体
        String fontName = Arrays.stream(fontArrays).filter(this::filterByName)
                .findFirst()
                .orElseGet(() -> DEFAULT_NAME);
        this.addItemListener(this);
        setSelectedItem(fontName);
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

    protected boolean filterByName(String name) {
        return DEFAULT_NAME.equals(name);
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
}

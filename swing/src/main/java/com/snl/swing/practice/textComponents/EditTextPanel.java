package com.snl.swing.practice.textComponents;

import com.snl.swing.practice.layUi.SpotLightLayerUiDemo;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

public class EditTextPanel extends JPanel {

    protected final String H1 = "H1";
    protected final String H2 = "H2";
    protected final String H3 = "H3";
    protected final String TEXT = "TEXT";
    protected HashMap<Object,Action> actions;


    protected JTextPane pane;

    public EditTextPanel() {
        super(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        pane = new JTextPane();

        //TODO 这是一个文档话的文本
        alignAttributes();
    }

    private void alignAttributes() {
        pane.setForeground(Color.WHITE);
        pane.setBackground(Color.black);
        pane.setEditable(true);
        pane.setEditorKit(new StyledEditorKit());
//        SpotLightLayerUiDemo layerUiDemo = new SpotLightLayerUiDemo();
//        JLayer<JComponent> layer = new JLayer<>(pane,layerUiDemo);
        add(pane,BorderLayout.CENTER);
        //初始化一级二级三级标题
        initTitle();
        actions = createActions();
    }

    private HashMap<Object, Action> createActions() {
        HashMap<Object,Action> map = new HashMap<>();
        Action[] paneActions = pane.getActions();
        Arrays.stream(paneActions).forEach(action -> map.put(action.getValue(Action.NAME),action));
        return map;
    }

    private void initTitle() {
        StyledDocument styledDocument = pane.getStyledDocument();
        Style style = styledDocument.getStyle(H1);
        if (style ==  null) {
            style = styledDocument.addStyle(H1,null);
        }
        StyleConstants.setFontFamily(style,"楷体");
        StyleConstants.setFontSize(style,32);
        StyleConstants.setBold(style,true);
        StyleConstants.setSpaceAbove(style,15);
        StyleConstants.setSpaceBelow(style,15);
        StyleConstants.setAlignment(style,StyleConstants.ALIGN_CENTER);

        style = styledDocument.addStyle(H2,null);
        StyleConstants.setFontFamily(style,"楷体");
        StyleConstants.setFontSize(style,26);
        StyleConstants.setBold(style,true);
        StyleConstants.setSpaceAbove(style,10);
        StyleConstants.setSpaceBelow(style,10);
        StyleConstants.setAlignment(style,StyleConstants.ALIGN_CENTER);

        style = styledDocument.addStyle(H3,null);
        StyleConstants.setFontFamily(style,"楷体");
        StyleConstants.setFontSize(style,18);
        StyleConstants.setBold(style,true);
        StyleConstants.setSpaceAbove(style,10);
        StyleConstants.setSpaceBelow(style,10);
        StyleConstants.setAlignment(style,StyleConstants.ALIGN_CENTER);

        style = styledDocument.addStyle(TEXT,null);
        StyleConstants.setFontFamily(style,"楷体");
        StyleConstants.setFontSize(style,18);
        StyleConstants.setBold(style,false);
        StyleConstants.setSpaceAbove(style,5);
        StyleConstants.setSpaceBelow(style,5);
        StyleConstants.setForeground(style,Color.cyan);
        StyleConstants.setAlignment(style,StyleConstants.ALIGN_CENTER);


        Style def = StyleContext.getDefaultStyleContext()
                .getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = styledDocument.addStyle("regular", null);
        StyleConstants.setFontFamily(regular,"楷体");
        StyleConstants.setForeground(regular,Color.cyan);
        StyleConstants.setFontSize(regular,18);
        StyleConstants.setSpaceBelow(regular,5);
        StyleConstants.setSpaceAbove(regular,5);

    }

    public JTextPane getPane() {
        return pane;
    }
}

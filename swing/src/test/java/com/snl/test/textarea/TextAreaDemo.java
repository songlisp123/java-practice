package com.snl.test.textarea;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextAreaDemo extends JFrame  implements DocumentListener {

    JLabel label;
    JScrollPane pane;
    JTextArea area;

    protected static final String COMMIT_COMMAND = "commit";


    private enum Model {
        INSERT,COMPLETION
    };

    private  List<String> words;

    private Model model = Model.INSERT;

    public TextAreaDemo(String title) throws HeadlessException  {
        super(title);
        initComponents();

        area.getDocument().addDocumentListener(this);

        //获取影射
        InputMap inputMap = area.getInputMap();
        ActionMap actionMap = area.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke("enter"),COMMIT_COMMAND);
        actionMap.put(COMMIT_COMMAND,new CommitAction());

        words = new ArrayList<>(5);
        words.add("speak");
        words.add("special");
        words.add("spectacles");
        words.add("spectacular");
        words.add("swing");
    }

    private void initComponents() {
        label = new JLabel("尝试按下xx");
        area = new JTextArea(5,30);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        pane = new JScrollPane(area);
        pane.setVerticalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        /*
        以下程序我没看懂，但是我还是把它写出来了
         */
        GroupLayout groupLayout = new GroupLayout(getContentPane());

        getContentPane().setLayout(groupLayout);

        //在水平轴上创建组合
        GroupLayout.ParallelGroup parallelGroup =
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
        //创建序列化和平行组
        GroupLayout.SequentialGroup h1 = groupLayout.createSequentialGroup();

        GroupLayout.ParallelGroup h2 =
                groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING);
        h2.addComponent(label,GroupLayout.Alignment.LEADING,GroupLayout.DEFAULT_SIZE,212,Short.MAX_VALUE);
        h2.addComponent(pane,GroupLayout.Alignment.LEADING,GroupLayout.DEFAULT_SIZE,212,Short.MAX_VALUE);

        h1.addContainerGap();

        h1.addGroup(h2);
        h1.addContainerGap();
        parallelGroup.addGroup(GroupLayout.Alignment.TRAILING,h1);
        groupLayout.setHorizontalGroup(parallelGroup);

        //在竖直方向上排列组件
        GroupLayout.ParallelGroup vGroup =
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
        GroupLayout.SequentialGroup v1 = groupLayout.createSequentialGroup();
        v1.addContainerGap();
        v1.addComponent(label);
        v1.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);

        v1.addComponent(pane,GroupLayout.DEFAULT_SIZE,100,Short.MAX_VALUE);
        v1.addContainerGap();

        vGroup.addGroup(v1);

        groupLayout.setVerticalGroup(vGroup);

        pack();


    }


    @Override
    public void insertUpdate(DocumentEvent e) {
        //TODO逻辑
        if (e.getLength() == 0) {
            //没有修改
            return;
        };
        int pos = e.getOffset();
        String content = null;
        try {
            area.getText(0,pos + 1) ;
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
        //查找字开始的位置
        int w;
        for (w = pos; w >=0;w--) {
            if (Character.isLetter(content.charAt(w))) {
                break;
            }
        }
        if (pos - w < 2) {
            return;
        }

        //前嘴
        String prefix = content.substring(w + 1).toLowerCase();
        int n = Collections.binarySearch(words, prefix);
        if (n < 0 && -n <= words.size()) {
            String match = words.get(-n-1);
            if (match.startsWith(prefix)) {
                //找到单词
                String completion = match.substring(pos - w);
                //不能再这个逻辑中更改
                //TODO 我实现不下去了
            }
        }

    }

    @Override
    public void removeUpdate(DocumentEvent e) {

    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    protected class CommitAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (model == Model.COMPLETION) {
                int selectionEnd = area.getSelectionEnd();
                area.insert(" ",selectionEnd);
                area.setCaretPosition(selectionEnd + 1);
                model = Model.INSERT;
            }else  {
                area.replaceSelection(System.lineSeparator());
            }
        }
    }
}


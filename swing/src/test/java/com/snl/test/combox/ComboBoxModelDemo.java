package com.snl.test.combox;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ComboBoxModelDemo  implements ComboBoxModel<String> , ActionListener , ItemListener {

    private final String[] items = {"1","2","3"};
    private final String[] items_2 = {"4","5","6"};
    private final List<String> data = new ArrayList<>();
    private final List<ListDataListener> listeners = new ArrayList<>();
    private String item;

    public ComboBoxModelDemo() {
//        Arrays.stream(items).forEach(data::add);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        item = anItem.toString();
    }

    @Override
    public Object getSelectedItem() {
        return item;
    }

    @Override
    public int getSize() {
//        return data.size();
        return items.length;
    }

    @Override
    public String getElementAt(int index) {
        return items[index];
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    public void addItem(String item) {
        data.add(item);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(()->{
            JFrame frame = new JFrame("测试");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            ComboBoxModelDemo modelDemo = new ComboBoxModelDemo();
            JComboBox<String> stringJComboBox = new JComboBox<>(modelDemo);
            stringJComboBox.addActionListener(modelDemo);
            stringJComboBox.addItemListener(modelDemo);

            JButton button = new JButton("添加");
//            button.addActionListener(e -> modelDemo.addItem("新项目"));
//            button.addActionListener(modelDemo);
            frame.add(stringJComboBox,BorderLayout.PAGE_START);
            frame.add(button,BorderLayout.PAGE_END);

            frame.pack();
            frame.setVisible(true);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("当前的选项是？===猜对了！"+item);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        String itemSelected =(String) e.getItem();
        System.out.println("itemSelected = " + itemSelected);
        System.out.println("item="+item);
        System.out.print("新选项是否等同于当前的item？答案是：");
        System.out.println(Objects.equals(itemSelected, item));
        if (e.getStateChange() == ItemEvent.SELECTED) {
            System.out.println("新选项："+itemSelected);
        }else {
            System.out.println("旧选项："+itemSelected);
        }
    }
}

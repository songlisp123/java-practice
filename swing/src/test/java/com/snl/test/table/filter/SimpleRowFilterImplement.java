package com.snl.test.table.filter;

import com.snl.test.table.model.SimpleTableModel;

import javax.swing.*;
import javax.swing.table.TableModel;

public class SimpleRowFilterImplement<M extends TableModel> extends RowFilter<M,Integer> {

    protected final boolean ACCEPTED = true;

    @Override
    public boolean include(Entry<? extends M, ? extends Integer> entry) {
        M model = entry.getModel();
        int columnCount = entry.getValueCount();
        Integer identifier = entry.getIdentifier();
        System.out.println("identifier = " + identifier);
        if (model instanceof SimpleTableModel) {
            SimpleTableModel tableModel = (SimpleTableModel) model;
            SimpleTableModel.User user = tableModel.getUser(entry.getIdentifier());
            if (user.getAge() > 50) return ACCEPTED;
            return !ACCEPTED;
        }
        return ACCEPTED;
    }
}

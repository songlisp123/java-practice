package com.snl.swing.practice.table;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.List;

public class SimpleRoeSorterDemo<M extends TableModel> extends RowSorter<M> {

    protected M model;

    public SimpleRoeSorterDemo(M model) {
        this.model = model;
    }

    @Override
    public M getModel() {
        return model;
    }

    /**
     *
     * @param column the column to toggle the sort ordering of, in
     *        terms of the underlying model
     */
    @Override
    public void toggleSortOrder(int column) {

    }

    @Override
    public int convertRowIndexToModel(int index) {
        return 0;
    }

    @Override
    public int convertRowIndexToView(int index) {
        return 0;
    }

    @Override
    public void setSortKeys(List<? extends SortKey> keys) {

    }

    @Override
    public List<? extends SortKey> getSortKeys() {
        return List.of();
    }

    @Override
    public int getViewRowCount() {
        return 0;
    }

    @Override
    public int getModelRowCount() {
        return 0;
    }

    @Override
    public void modelStructureChanged() {

    }

    @Override
    public void allRowsChanged() {

    }

    @Override
    public void rowsInserted(int firstRow, int endRow) {

    }

    @Override
    public void rowsDeleted(int firstRow, int endRow) {

    }

    @Override
    public void rowsUpdated(int firstRow, int endRow) {

    }

    @Override
    public void rowsUpdated(int firstRow, int endRow, int column) {

    }
}

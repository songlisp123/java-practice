package com.snl.swing.tank;

public class PaoTai extends CircleObject {

    public PaoTai() {
        super();
    }

    public PaoTai(int edge, double radius) {
        super(edge,radius);
    }

    @Override
    protected void update(Component parentComponent) {
        parentForm = parentComponent.transFrom;
    }
}

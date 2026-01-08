package com.snl.data.homework.charptor03.practice01.text;

public final class Charpter implements Text {

    private int size;

    private final String[] data = {
            "第一关：初出茅庐",
            "第二关：天火流星",
            "第二关：草船借箭",
    };

    public Charpter() {
        size = data.length;
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public String getString(int index) {
        checkIndex(index);
        return data[index];
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("索引超出位置，发生在索引[%d]处".formatted(index));
    }
}

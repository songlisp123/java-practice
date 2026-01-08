package com.snl.data.homework.charptor03.practice01.text;

public final class Description implements Text {

    private int size;

    private final String[] data = {
            "一位少女穿越到赛朋博客的世界中,她站在霓虹闪光中,目光闪烁……",
            "双马尾随风飘荡，少女此时心中只想找到家乡的位置……",
            "机甲汽车在空气中穿梭行驶，少女突然看到一个庞然大物在地平线移动……",
            "庞然大物布满红色物质的管道，它的身材高大伟岸，高不可及，它行动缓慢，像个神秘的巨兽，像少男女走来",
            "少女不知道什么，心念之间，手中出现青色纹路的长剑，而与此同时，庞然大物忽然临近，它的脚步没落下一次，就掀起一阵狂中……",
            "庞然大物念叨着，‘入侵者’……’，少女摆出战斗架势，她的未来是什么……",
    };

    public Description() {
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

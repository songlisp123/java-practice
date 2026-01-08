package com.snl.data.tree;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * 此程序使用数组模拟二叉搜索树,默认不能实现相等项
 * 在这个练习中，你应该注重如何将index转换为swing中的x坐标和y坐标
 */
class personGroup {
    /**
     * 屏幕宽度
     */
    private final int appletWidth = 440;
    /**
     * 屏幕高度
     */
    private final int appletHeight = 300;
    /**
     * 节点之间最大高度
     */
    private final int maxHeight = 200;
    /**
     * 上部渲染区域
     */
    private final int topMargin = 70;
    /**
     * 左边区域
     */
    private final int leftMargin = 10;
    /**
     * 文本高度
     */
    private final int textHeight = 13;
    /**
     * 节点直径
     */
    private final int nodeDiameter = 20;
    /**
     * 层级之间间距
     */
    private final int levelSeparation = 40;
    /**
     * 节点之间水平间距
     */
    private final int horizSeparation = 26;
    /**
     * 文本盒子上边距
     */
    private final int noteBoxTop = 45;
    /**
     * 文本高度盒子高度
     */
    private final int noteBoxHeight = 25;
    /**
     * 文本盒子宽度
     */
    private final int noteBoxWidth = 200;
    /**
     * 该字段访问盒子上部区域，距离上边框距离
     */
    private final int visitBoxTop = 280;
    /**
     * 绘制文本高度
     */
    private final int visitBoxHeight = 25;
    /**
     * 绘制文本宽度
     */
    private final int visitBoxWidth = 430;
    /**
     * 树所包含的最多节点
     */
    private final int ASIZE = 31;
    /**
     * 树中所包含的最大键值
     */
    private final int MAX_KEY = 99;
    /**
     * 树节点模型
     */
    private person[] treeArray = new person[31];
    /**
     *  用于中序遍历，主要用于树traverse操作
     */
    stack theStack = new stack(20);
    /**
     * 填充节点数量，主要用于fill方法中，用以判断
     * 已填充节点数是否与预定义节点数相同，请注意
     * @implNote 该字段表示实际填充的节点数量，可能并不会与给定的数量相同
     */
    private int filledNodes;
    /**
     * 提示信息
     */
    private String note;
    /**
     * 这是随机选择，并没有作用
     */
    private boolean isRand = true;
    /**
     * 这个字段的意思是：插入数据需要保存的数据
     */
    private int value;
    /**
     * 每个操作的顺序范围是1-10
     * 越低表示操作越靠前
     * 比如对于插入：
     * 1 -> 输入值
     * 2 - > 校验值
     * 3 -> 查找
     * 4 -> 插入
     * 5 -> 回复状态到1
     * 这个字段绑定了按钮，每按一次按钮就会进行一次状态更新
     */
    private int codePart = 1;

    /**
     * 操作状态，
     * 1表示重新填充
     * 2表示查询
     * 3表示插入
     * 4表示移除
     * 5表示遍历
     */
    private int opMode;

    /**
     * 当前节点的数组索引下标
     */
    private int curIn;
    /**
     * 上一步节点的数组索引下标
     */
    private int curInOld;
    /**
     * 上一步箭头，在示例程序中这是灰色的箭头
     */
    private int oldArrow;

    /**
     * 用于树遍历，按照中序排列保存树中节点的值
     */
    private int[] visitArray;

    /**
     * 当前访问的节点索引
     */
    private int visitIndex;

    /**
     * 当前节点的下一个节点
     */
    private int successor;

    /**
     * 是否全部绘制？
     * true表示全部绘制
     * false表示不绘制完整的部分
     */
    private boolean drawAll;

    /**
     * 绘制模式：
     * 0、只画访问路劲
     * 1、画新插入节点
     * 2、全部重新绘制
     */
    private int drawMode = 2;

    /**
     * 记录指针的遍历路径
     */
    private final List<Integer> records =
            new ArrayList<>();

    public personGroup() {
        for(int i = 0; i < 31; ++i) {
            this.treeArray[i] = null;
        }
        this.filledNodes = 0;
        this.note = "按下按钮";
        this.visitArray = new int[31];
        this.visitIndex = 0;
    }

    /**
     * 以指定key值创建节点模型
     * @param key 指定键值
     * @return 新创建的 {@link person} 对象
     */
    public person makePerson(int key) {
        int r = 100 + (int)(Math.random() * (double)154.0F);
        int g = 100 + (int)(Math.random() * (double)154.0F);
        int b = 100 + (int)(Math.random() * (double)154.0F);
        Color color = new Color(r, g, b);
        return new person(key, color);
    }

    /**
     * 设置绘制模式，有关说明请参考字段 <b>drawAll</b>
     * @param b 布尔变量，{@code false}表示不绘制全部<br />
     *          {@code true} 表示绘制全部节点
     * @see <a href="https://www.jd.com">京东</a>
     */
    public void setDrawAll(boolean b) {
        this.drawAll = b;
    }

    /**
     * 填充给定的节点数据到数据模型中
     * @param isNumber 是否是数字
     * @param fillNumber 填充的节点数量，可能不准确
     */
    public void fill(boolean isNumber, int fillNumber) {
        if (this.opMode != 1) {
            this.opMode = 1;
            this.codePart = 1;
        }

        switch (this.codePart) {
            case 1:
                //当前访问节点索引
                this.visitIndex = 0;
                this.note = "输入数字（1-99）:";
                //当前数组索引
                this.curIn = 0;
                //判断上一个箭头的位置
                this.oldArrow = this.curInOld;
                //上一个节点的索引
                this.curInOld = 0;
                //绘制模式，0表示全部绘制
                this.drawMode = 0;
                //状态走到下一步
                this.codePart = 2;
                //清空旧记录
                records.clear();
                return;
            case 2:
                if (isNumber && fillNumber >= 0 && fillNumber <= 31) {
                    //如果当前字段是数字并且填充的数字在索引【0-31】处
                    this.note = "将会创建" + fillNumber + "节点";
                    //跳到下一步
                    this.codePart = 3;
                    return;
                }

                this.note = "填充节点超出目标数组容量，请重试！";
                //回到第一步
                this.codePart = 1;
                return;
            case 3:
                //填充节点
                this.doFill(fillNumber);
                //绘制模式为2，表示全部绘制
                this.drawMode = 2;
                this.note = "请按按钮";

                this.oldArrow = this.curInOld;
                //回复上一个节点为根节点
                this.curInOld = 0;
                //恢复状态为1
                this.codePart = 1;
                return;
            default:
        }
    }

    /**
     * 初始化填充所有数据列表
     * @param sum 预设的节点数量
     */
    public void doFill(int sum) {
        //这可能达不到给定的节点
        int count = 0;//为了防止程序陷入卡死状态，采用计数器，当计数器达到一百的时候，退出程序

        for(int i = 0; i < 31; ++i) {
            this.treeArray[i] = null;
        }

        this.filledNodes = 0; //已经填充的节点数量
        boolean[] booleans = new boolean[100]; //判断该随机值是否使用

        for(int i = 0; i < 100; ++i) {
            booleans[i] = false;
        }

        label41:
        while(this.filledNodes < sum && count < 100) {
            int random = (int)(Math.random() * (double)99.0F);
            if (!booleans[random]) {
                //如果当前随机值没有使用，创建person对象
                person person = this.makePerson(random);
                //初始化为根节点索引
                this.curIn = 0;

                while(this.curIn <= 30) {
                    if (this.treeArray[this.curIn] == null) {
                        //如果当前索引的树的模型数据为null，插入它
                        this.treeArray[this.curIn] = person;
                        //这个什么意思？
                        ++this.filledNodes;
                        //将当前随机值赋值为true
                        booleans[random] = true;
                        continue label41;
                    }

                    if (random < this.treeArray[this.curIn].getHeight()) {
                        //跑去左节点索引处，相当于2*index + 1
                        this.curIn = 2 * this.curIn + 1;
                    } else {
                        //跑去右节点索引处，默认为2*index + 2
                        this.curIn = 2 * this.curIn + 2;
                    }
                }
                ++count;
            }
        }

    }

    /**
     * 查找键为{@code key}的节点
     * @param isNumber 布尔变量
     * @param key 要查找的键值，可以是{@code number} 或者 {@code class}
     */
    public void find(boolean isNumber, int key) {
        if (this.opMode != 2) {
            this.opMode = 2;
            this.codePart = 1;
        }

        switch (this.codePart) {
            case 1:
                //初始化当前访问的索引
                this.visitIndex = 0;
                this.note = "输入要查找的键";
                //当前索引
                this.curIn = 0;
                //将旧箭头的位置移动到前一个访问的节点
                this.oldArrow = this.curInOld;
                //初始化上一个节点
                this.curInOld = 0;
                //绘制模式，0表示只绘制访问路径
                this.drawMode = 0;
                //前往下一步
                this.codePart = 2;
                //清空列表
                this.records.clear();
                return;
            case 2:
                if (isNumber && key >= 0 && key <= 99) {
                    //查找的key值必须在【0-99】区间
                    this.note = "正在查找有关的键" + key;
                    this.codePart = 3;
                    return;
                }

                this.note = "键必须位于0-99范围";
                this.codePart = 1;
                return;
            case 3:
                if (this.treeArray[this.curIn] == null) {
                    this.note = "暂未有该节点";
                    this.codePart = 1;
                    return;
                } else if (key == this.treeArray[this.curIn].getHeight()) {
                    this.note = "已经找到该节点" + key;
                    records.add(this.oldArrow);
                    this.oldArrow = this.curInOld;
                    records.add(this.oldArrow);
                    this.curInOld = this.curIn;
                    this.codePart = 4;
                    return;
                } else {
                    records.add(this.oldArrow);
                    this.oldArrow = this.curInOld;
                    records.add(this.oldArrow);
                    this.curInOld = this.curIn;
                    if (key < this.treeArray[this.curIn].getHeight()) {
                        this.curIn = 2 * this.curIn + 1;
                        this.note = "去往左节点";
                    } else {
                        this.curIn = 2 * this.curIn + 2;
                        this.note = "去往右节点";
                    }
                    System.out.println("curIn = " + curIn);
                    this.codePart = 3;
                    if (this.curIn > 30) {
                        this.note = "不能找到具有改键的节点";
                        this.codePart = 1;
                        return;
                    }
                }
            default:
                return;
            case 4:
                this.note = "查找完成";
                this.codePart = 5;
                return;
            case 5:
                this.note = "请按下按钮";
                this.oldArrow = this.curInOld;
                this.curInOld = 0;
                this.codePart = 1;
                records.clear();
        }
    }

    /**
     * 插入键为key的节点
     * @param isNumber 布尔标志
     * @param key 键值
     */
    public void insert(boolean isNumber, int key) {
        if (this.opMode != 3) {
            this.opMode = 3;
            this.codePart = 1;
        }

        switch (this.codePart) {
            case 1:
                this.visitIndex = 0;
                this.note = "输入要插入的节点值";
                this.curIn = 0;
                this.oldArrow = this.curInOld;
                this.curInOld = 0;
                this.drawMode = 0;
                this.codePart = 2;
                return;
            case 2:
                this.value = key;
                if (isNumber && key >= 0 && key <= 99) {
                    //如果插入的是数字且范围在0-99
                    this.note = "即将插入带有键的节点" + this.value;
                    this.codePart = 3;
                    return;
                }

                //否则报错返回第一步
                this.note = "节点的取值范围是0 ~ 99";
                this.codePart = 1;
                return;
            case 3:
                this.oldArrow = this.curInOld;
                this.curInOld = this.curIn;
                if (this.curIn > 30) {
                    this.note = "层级太高";
                    this.codePart = 4;
                    return;
                } else {
                    if (this.treeArray[this.curIn] == null) {
                        this.treeArray[this.curIn] = this.makePerson(this.value);
                        this.value = this.treeArray[this.curIn].getHeight();
                        this.note = "插入键:" + this.value+"节点";
                        ++this.filledNodes;
                        this.curInOld = this.curIn;
                        this.drawMode = 1;
                        this.codePart = 4;
                        return;
                    }

                    if (this.value < this.treeArray[this.curIn].getHeight()) {
                        this.curIn = 2 * this.curIn + 1;
                        this.note = "去往左节点";
                    } else {
                        this.curIn = 2 * this.curIn + 2;
                        this.note = "去往右节点";
                    }

                    this.codePart = 3;
                    if (this.curIn > 30) {
                        this.note = "不能插入：层级太高";
                        this.codePart = 1;
                    }

                    this.drawMode = 0;
                    return;
                }
            case 4:
                this.note = "插入成功";
                this.drawMode = 0;
                this.codePart = 5;
                return;
            case 5:
                this.note = "请点击按钮";
                this.oldArrow = this.curInOld;
                this.curInOld = 0;
                this.codePart = 1;
                return;
            default:
        }
    }

    public void remove(boolean var1, int var2) {
        if (this.opMode != 4) {
            this.opMode = 4;
            this.codePart = 1;
        }

        switch (this.codePart) {
            case 1:
                this.visitIndex = 0;
                this.note = "输入要删除的键的节点";
                this.codePart = 2;
                this.curIn = 0;
                this.oldArrow = this.curInOld;
                this.curInOld = 0;
                this.drawMode = 0;
                return;
            case 2:
                if (var1 && var2 >= 0 && var2 <= 99) {
                    this.note = "Will try to delete node " + var2;
                    this.codePart = 3;
                } else {
                    this.note = "Nodes have values from 0 to 99";
                    this.codePart = 1;
                }

                this.curIn = 0;
                this.oldArrow = this.curInOld;
                this.curInOld = 0;
                return;
            case 3:
                if (this.treeArray[this.curIn] == null) {
                    this.note = "Can't find node with that value";
                    this.codePart = 1;
                    return;
                } else if (var2 == this.treeArray[this.curIn].getHeight()) {
                    this.note = "Have found node to delete";
                    this.oldArrow = this.curInOld;
                    this.curInOld = this.curIn;
                    this.codePart = 4;
                    return;
                } else {
                    this.oldArrow = this.curInOld;
                    this.curInOld = this.curIn;
                    if (var2 < this.treeArray[this.curIn].getHeight()) {
                        this.curIn = 2 * this.curIn + 1;
                        this.note = "Going to left child";
                    } else {
                        this.curIn = 2 * this.curIn + 2;
                        this.note = "Going to right child";
                    }

                    this.codePart = 3;
                    if (this.curIn > 30) {
                        this.note = "Can't find node with that value";
                        this.codePart = 1;
                        return;
                    }
                }
            default:
                return;
            case 4:
                if (this.curIn <= 14 && (this.treeArray[2 * this.curIn + 1] != null || this.treeArray[2 * this.curIn + 2] != null)) {
                    if (this.treeArray[2 * this.curIn + 1] == null) {
                        this.note = "Will replace node with its right subtree";
                        this.codePart = 6;
                        return;
                    }

                    if (this.treeArray[2 * this.curIn + 2] == null) {
                        this.note = "Will replace node with its left subtree";
                        this.codePart = 7;
                        return;
                    }

                    this.successor = this.inorderSuccessor(this.curIn);
                    this.note = "Will replace node with " + this.treeArray[this.successor].getHeight();
                    this.codePart = 8;
                    return;
                }

                this.note = "Will delete node without complication";
                this.codePart = 5;
                return;
            case 5:
                this.treeArray[this.curIn] = null;
                this.note = "Node was deleted";
                this.drawMode = 2;
                this.codePart = 10;
                return;
            case 6:
                this.treeArray[this.curIn] = null;
                this.moveUpSubTree(1, this.curIn);
                this.note = "Node was replaced by its right subtree";
                this.drawMode = 2;
                this.codePart = 10;
                return;
            case 7:
                this.treeArray[this.curIn] = null;
                this.moveUpSubTree(0, this.curIn);
                this.note = "Node was replaced by its left subtree";
                this.drawMode = 2;
                this.codePart = 10;
                return;
            case 8:
                this.treeArray[this.curIn] = this.treeArray[this.successor];
                int var3 = 2 * this.successor + 2;
                if (this.successor < 15 && this.treeArray[var3] != null) {
                    int var4 = this.treeArray[this.successor].getHeight();
                    this.note = "and replace " + var4 + " with its right subtree";
                    this.drawMode = 0;
                    this.codePart = 9;
                    return;
                }

                this.treeArray[this.successor] = null;
                this.note = "Node was replaced by successor";
                this.drawMode = 2;
                this.codePart = 10;
                return;
            case 9:
                this.moveUpSubTree(1, this.successor);
                this.note = "Removed node in 2-step process";
                this.drawMode = 2;
                this.codePart = 10;
                return;
            case 10:
                this.note = "Press any button";
                this.oldArrow = this.curInOld;
                this.curInOld = 0;
                this.codePart = 1;
        }
    }

    public void moveUpSubTree(int var1, int var2) {
        if (var2 <= 14 && var2 >= 0) {
            int var3;
            if (var1 == 1) {
                var3 = 2 * var2 + 2;
            } else {
                var3 = 2 * var2 + 1;
            }

            byte var6;
            if (var3 > 0 && var3 < 3) {
                var6 = 1;
            } else if (var3 > 2 && var3 < 7) {
                var6 = 2;
            } else if (var3 > 6 && var3 < 15) {
                var6 = 3;
            } else {
                var6 = 4;
            }

            int var7 = var3;
            int var4 = var3;
            int var5 = 1;

            for(int var11 = var6; var11 < 5; ++var11) {
                for(int var12 = 0; var12 < var5; ++var12) {
                    int var9 = (var4 - 1) / 2;
                    int var8 = var5 - var12 - 1;
                    int var10;
                    if (var1 == 1) {
                        var10 = var9 - (var8 + 1) / 2;
                    } else {
                        var10 = var9 + (var12 + 1) / 2;
                    }

                    this.treeArray[var10] = this.treeArray[var4];
                    if (var11 == 4) {
                        this.treeArray[var4] = null;
                    }

                    ++var4;
                }

                var7 = 2 * var7 + 1;
                var4 = var7;
                var5 *= 2;
            }

        }
    }

    public int inorderSuccessor(int var1) {
        int var2 = var1;

        for(int var3 = 2 * var1 + 2; var3 < 31 && this.treeArray[var3] != null; var3 = 2 * var3 + 1) {
            var2 = var3;
        }
        return var2;
    }

    /**
     * 以中序方式遍历树，一般来说，从左子树到右子树
     */
    public void traverse() {
        if (this.opMode != 5) {
            this.opMode = 5;
            this.codePart = 1;
        }

        switch (this.codePart) {
            case 1:
                this.visitIndex = 0;
                this.note = "开始以中序方式遍历树";
                this.curIn = 0;
                this.oldArrow = this.curInOld;
                this.curInOld = 0;
                this.drawMode = 0;
                this.codePart = 2;
                this.records.clear();
                return;
            case 2:
                this.note = "检查左节点";
                this.codePart = 3;
                return;
            case 3:
                if (this.curIn <= 14 && this.treeArray[2 * this.curIn + 1] != null) {
                    //如果当前索引小于14并且该当前节点存在左节点，压入栈中
                    this.theStack.push(this.curIn);
                    //前往左子节点
                    this.curIn = 2 * this.curIn + 1;
                    records.add(this.oldArrow);
                    this.oldArrow = this.curInOld;
                    records.add(this.oldArrow);
                    this.curInOld = this.curIn;
                    this.note = "将会检查左节点";
                    this.codePart = 3;
                    return;
                }
                /*
                如果当前索引大于14，那就表示，当前节点没有左子节点此时我们需要：
                1、访问该节点
                2、向右子节点进发
                 */
                this.note = "将会访问该节点";
                this.codePart = 4;
                return;
            case 4:
                this.visitArray[this.visitIndex++] = this.treeArray[this.curIn].getHeight();
                this.note = "将会检查右节点";
                this.codePart = 5;
                return;
            case 5:
                //右侧节点
                if (this.curIn <= 14 && this.treeArray[2 * this.curIn + 2] != null) {
                    this.curIn = 2 * this.curIn + 2;
                    records.add(this.oldArrow);
                    this.oldArrow = this.curInOld;
                    records.add(this.oldArrow);
                    this.curInOld = this.curIn;
                    this.note = "将会检查右节点";
                    this.codePart = 3;
                    return;
                }

                this.note = "将会去根节点右子树";
                this.codePart = 6;
                return;
            case 6:
                if (this.theStack.isEmpty()) {
                    this.note = "遍历完成";
                    this.codePart = 7;
                    return;
                }

                this.curIn = this.theStack.pop();
                records.add(this.oldArrow);
                this.oldArrow = this.curInOld;
                records.add(this.oldArrow);
                this.curInOld = this.curIn;
                this.note = "访问该节点";
                this.codePart = 4;
                return;
            case 7:
                this.note = "按压按钮";
                this.oldArrow = this.curInOld;
                this.curInOld = 0;
                this.codePart = 1;
                records.clear();
                return;
            default:
        }
    }

    public void drawOneNode(Graphics g, int index) {
        if (this.treeArray[index] != null) {
            int height = this.treeArray[index].getHeight();
            Color color = this.treeArray[index].getColor();
            /*
            左孩子还是右孩子
            var5 == 1 右孩子
            var5 == 0 左孩子
             */
            int isLeft = index % 2; //奇数或者偶数（左孩子或右孩子）？
            /*
            当前层中的横向变量，代表着该节点在该层属于第几个位置
            层越深，间距越小
             */
            int speration = 15; //何意味
            /*
            当前是第几层？
            0  - root
            1 - 第一层
            ……
            代表着该层y坐标
             */
            byte level = 0; //当前是第几层
            /*
            代表子节点与父节点之间的横向距离
            节点越深，距离越小
             */
            int offset = -1;
            if (index > 0 && index < 3) {
                //第一层
                speration = 7 + (index - 1) * 16;
                level = 1;
                offset = isLeft == 1 ? speration + 8 : speration - 8;
            } else if (index > 2 && index < 7) {
                speration = 3 + (index - 3) * 8;
                level = 2;
                offset = isLeft == 1 ? speration + 4 : speration - 4;
            } else if (index > 6 && index < 15) {
                speration = 1 + (index - 7) * 4;
                level = 3;
                offset = isLeft == 1 ? speration + 2 : speration - 2;
            } else if (index > 14 && index < 31) {
                speration = (index - 15) * 2;
                level = 4;
                offset = isLeft == 1 ? speration + 1 : speration - 1;
            }

            int xPos = 10 + speration * 26 / 2;
            int yPos = 70 + level * 40;
            int fatherXPos = 10 + offset * 26 / 2;
            int fatherYPos = 70 + (level - 1) * 40;

            if (level > 0) {
                //如果有层级，那么绘制父节点和子节点之间的线段
                g.setColor(Color.black);
                g.drawLine(xPos + 10, yPos + 10, fatherXPos + 10, fatherYPos + 10);
            }

            //绘制当前节点
            g.setColor(color);
            g.fillOval(xPos, yPos, 20, 20);

            //绘制边框
            g.setColor(Color.black);
            g.drawOval(xPos, yPos, 20, 20);
            //手动调整居中文本
            if (height < 10) {
                g.drawString(String.valueOf(height), xPos + 7, yPos + 20 - 5);
            } else {
                g.drawString(String.valueOf(height), xPos + 4, yPos + 20 - 5);
            }
            //绘制节点颜色

            g.setColor(Color.GREEN);
            g.fillOval(xPos,yPos,5,5);

            g.fillOval(xPos+20-4,yPos,4,4);
            g.fillOval(xPos,yPos+20-4,4,4);
            g.fillOval(xPos+20-4,yPos+20-4,4,4);
        }
    }

    public void draw(Graphics g) {
        int choice = this.drawAll ? 2 : this.drawMode;
        switch (choice) {
            case 0:
            case 1:
                for(int i = this.curInOld; i > 0; i = (i - 1) / 2) {
                    this.drawOneNode(g, i);
                }

                this.drawOneNode(g, 0);
                break;
            case 2:
                g.setColor(Color.lightGray);
                g.fillRect(0, 0, 500, 300);

                for(int i = 30; i >= 0; --i) {
                    this.drawOneNode(g, i);
                }
            default:
                break;
        }

        g.setColor(Color.lightGray);
        g.fillRect(10, 45, 200, 25);
        g.setColor(Color.black);
        g.drawString(this.note, 16, 64);
        g.setColor(Color.lightGray);
        g.fillRect(10, 280, 430, 25);
        g.setColor(Color.black);
        String s = "";

        for(int i = 0; i < this.visitIndex; ++i) {
            s = s + this.visitArray[i] + " ";
        }

        g.drawString(s, 16, 296);
//        this.drawArrow(g, this.oldArrow, false);
        this.drawPath(g);
        this.drawArrow(g, this.curInOld, true);
        this.drawAll = true;
    }

    private void drawPath(Graphics g) {
        for (Integer index : records) {
            this.drawArrow(g,index,false);
        }
    }


    public void drawArrow(Graphics g, int index, boolean colored) {
        if (this.treeArray[index] != null) {
            //当前节点存在数据
            int gap = 15;
            byte level = 0;
            if (index > 0 && index < 3) {
                gap = 7 + (index - 1) * 16;
                level = 1;
            } else if (index > 2 && index < 7) {
                gap = 3 + (index - 3) * 8;
                level = 2;
            } else if (index > 6 && index < 15) {
                gap = 1 + (index - 7) * 4;
                level = 3;
            } else if (index > 14 && index < 31) {
                gap = (index - 15) * 2;
                level = 4;
            }

            int xPos = 10 + gap * 26 / 2;
            int yPos = 70 + level * 40;
            if (colored) {
                g.setColor(Color.red);
            } else {
                g.setColor(Color.blue);
            }

            int centerXPos = xPos + 10;
            int centerYPos = yPos - 2;
            byte high = 20;
            g.drawLine(centerXPos, centerYPos, centerXPos, centerYPos - high);
            g.drawLine(centerXPos - 1, centerYPos, centerXPos - 1, centerYPos - high);
            g.drawLine(centerXPos, centerYPos, centerXPos - 3, centerYPos - 6);
            g.drawLine(centerXPos - 1, centerYPos, centerXPos - 4, centerYPos - 6);
            g.drawLine(centerXPos, centerYPos, centerXPos + 3, centerYPos - 6);
            g.drawLine(centerXPos - 1, centerYPos, centerXPos + 2, centerYPos - 6);
        }
    }
}

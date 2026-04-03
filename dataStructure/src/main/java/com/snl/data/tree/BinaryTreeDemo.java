package com.snl.data.tree;

import com.snl.swing.practice.button.CustomButton;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.random.RandomGenerator;

public class BinaryTreeDemo extends JPanel implements ActionListener, MouseMotionListener , MouseListener {
    private JButton fillButton;
    private JButton insertButton;
    private JButton searchButton;
    /**
     * 此按钮将会以下拉框一起合作，按照遍历模式遍历树
     */
    private JButton travelButton;
    private JButton delButton;
    private JButton minButton;
    private JButton maxButton;
    private JButton cancelButton;
    private JButton clearButton;
    private ComboBoxModel<String> model;
    private JComboBox<String> comboBox;
    private JLabel label;
    private JLabel statusLabel;
    private JTextField textField;

    /**
     * 用户选择值
     */
    private int answer;
    /**
     * 随机值
     */
    private final int MAX_NUMBER = 1000;

    /**
     * 随机数生成器
     */
    private final RandomGenerator generator =
            RandomGenerator.getDefault();

    /**
     * 遍历模式：
     * 0-中序遍历
     * 1-前序遍历
     * 2-后序遍历
     */
    private int travelMode;

    private Color nodeColor;

    @Override
    public void mouseClicked(MouseEvent e) {
        //着重
        if (currentShape != null) {
            nodeColor = JColorChooser.showDialog(this, "选择一种颜色", Color.CYAN);
            repaint(currentShape.getBounds());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //空方法
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //空方法体
    }

    @Override
    public void mouseEntered(MouseEvent e) {
            //空方法体
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //空方法体
    }

    /**
     * 用户选择模式
     */
    private enum Choice {
        FILL,SEARCH,INSERT,TRAVEL,DELETION,MIN,CANCEL,MAX,CLEAR
    }

    /**
     * 当前选项
     */
    private Choice choice;

    /**
     * 数据模型
     */
    private Group group;

    /**
     * 每一个节点的几何表示
     */
    private final List<DrawInfo> shapes =
            new ArrayList<>();

    /**
     * 鼠标指向的当前形状
     */
    private Shape currentShape;

    /**
     * 搜索的几何节点
     */
    private Shape searchShape;

    /**
     * 这个字段与最小节点对应
     */
    private Shape minNode;

    /**
     * 这个字段与最大节点对应
     */
    private Shape maxNode;

    /**
     * 耗时后台任务
     */
    private Task task;

    /**
     * 遍历时正在访问的树节点
     */
    private Shape travelNode;

    /**
     * 是否遍历该树的操作？
     */
    private boolean isTravel;

    /**
     * 操作模式，简要介绍一下：
     * -1 ： 当前无操作
     * 1 ： 搜索
     * 2 ： 插入
     * 3 ： 删除
     */
    private int opMode;

    public BinaryTreeDemo() {
        init();
    }

    public BinaryTreeDemo(LayoutManager layout) {
        super(layout);
        init();
    }

    private void init() {
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        fillButton = new CustomButton("填充");
        searchButton = new CustomButton("搜索");
        insertButton = new CustomButton("插入");
        travelButton = new CustomButton("遍历");
        delButton = new CustomButton("删除");
        minButton = new CustomButton("最小节点");
        maxButton = new CustomButton("最大节点");
        cancelButton = new CustomButton("取消");
        clearButton = new CustomButton("清空");
        textField = new JTextField(10);
        group = new Group<Integer>();
        model = new ComboxModel();
        comboBox = new JComboBox<>(model);
        label = new JLabel("选择遍历模式：",JLabel.CENTER);
        label.setLabelFor(comboBox);
        label.setForeground(Color.GREEN);

        statusLabel = new JLabel("状态栏测试文本",JLabel.CENTER);
        statusLabel.setForeground(Color.YELLOW);

        fillButton.addActionListener(this);
        searchButton.addActionListener(this);
        insertButton.addActionListener(this);
        travelButton.addActionListener(this);
        delButton.addActionListener(this);
        minButton.addActionListener(this);
        maxButton.addActionListener(this);
        cancelButton.addActionListener(this);
        clearButton.addActionListener(this);

        alignSpace();
    }

    private void alignSpace() {
        LayoutManager layout = getLayout();
        if (!(layout instanceof FlowLayout)) {
            layout = new FlowLayout();
            setLayout(layout);
        }

        add(fillButton);
        add(searchButton);
        add(insertButton);
        add(travelButton);
        add(delButton);
        add(minButton);
        add(maxButton);
        add(cancelButton);
        add(clearButton);
        add(clearButton);
        add(label);
        add(comboBox);

        add(statusLabel);
    }

    private static void createUi() {
        JFrame frame = new JFrame("测试");
        var test = new BinaryTreeDemo();
        var layUi = new SpotLightLayerUiDemo();
        var layer = new JLayer<>(test, layUi);
        frame.add(test);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(BinaryTreeDemo::createUi);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var source = e.getSource();
        if (source == fillButton) {
            choice = Choice.FILL;
        } else if (source == searchButton) {
            choice = Choice.SEARCH;
        } else if (source == insertButton) {
            choice = Choice.INSERT;
        } else if (source == travelButton) {
            choice = Choice.TRAVEL;
        } else if (source == delButton) {
            choice = Choice.DELETION;
        } else if (source == minButton) {
            choice = Choice.MIN;
        } else if (source == cancelButton) {
            choice = Choice.CANCEL;
        } else if (source == maxButton) {
            choice = Choice.MAX;
        } else if (source == clearButton) {
            choice = Choice.CLEAR;
        }

        doHandle();
    }

    private void doHandle() {

        switch (choice) {
            case FILL :
                choice();
                cancel();
                fill(answer);
                break;
            case SEARCH:
                choice();
                this.opMode = 1;
                break;
            case INSERT:
                cancel();
                choice();
                opMode = 2;
                break;
            case TRAVEL:
                travelTree();
                isTravel = true;
                break;
            case DELETION:
                cancel();
                choice();
                opMode = 3;
                break;
            case MIN:
                var min = findMin();
                DrawInfo minShape = findNodeIn2D(min);
                if (minShape != null)
                    minNode = minShape.shape;
                break;
            case MAX:
                var max = group.findMax();
                DrawInfo drawInfo = findNodeIn2D(max);
                if (drawInfo != null) {
                    maxNode = drawInfo.shape;
                }
                break;
            case CLEAR:
                cancel();
                group.clear();
                shapes.clear();
                break;
            case CANCEL:
                cancel();
                break;
            default :
                opMode = -1;
                isTravel = false;
                break;
        }
        repaint();
    }

    private void travelTree() {
        doTask();
    }

    private void doTask() {
        if (task != null && !task.isDone())
            task.cancel(true);
        task = new Task();
        task.execute();
    }

    private void cancel() {
        if (searchShape != null)
            searchShape = null;
        if (currentShape != null)
            currentShape = null;
        if (minNode != null)
            minNode = null;
        if (maxNode != null)
            maxNode = null;
        if (travelNode != null)
            travelNode = null;
        if (task != null && !task.isDone())
            task.cancel(true);
        if (isTravel)
            isTravel = false;
    }

    /**
     * 用户选择
     */
    private void choice() {
        var s = (String)JOptionPane.showInputDialog(this, "请选择一个数字：",
                "选择", JOptionPane.QUESTION_MESSAGE,
                null, null, null);
        if (s == null)
            return;
        try {
            answer = Integer.parseInt(s);
        }catch (NumberFormatException ex) {
            answer = 0;
        }
        doTask();
    }

    private DrawInfo findNodeIn2D(Group.BinaryNode node) {
        if (shapes.isEmpty()) return null;
        for (DrawInfo info : shapes) {
            if (info.element.equals(node.element))
                return info;
        }
        return null;
    }

    private Group.BinaryNode findMin() {
        return group.findMin();
    }

    public void fill(int size) {
        //初始化数组
        shapes.clear();
        group.clear();
        boolean[] used = new boolean[MAX_NUMBER];
        Arrays.fill(used,false);
        int count = 0;

        flag:
        while (count < size) {
            int i = generator.nextInt(0, MAX_NUMBER);
            if (!used[i]) {
                used[i] = false;
                group.insert(i);
                count++;
                continue flag;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        group.draw(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.CYAN);
        if (!shapes.isEmpty()) {
            for (DrawInfo info : shapes) {
                var shape = info.shape.getBounds();
                g2.draw(info.shape);
                g2.drawString(info.element.toString(), shape.x + 5, shape.y + 20);
            }
            if (currentShape != null) {
                double centerX = currentShape.getBounds().getCenterX();
                double centerY = currentShape.getBounds().getCenterY();
                Point2D center = new Point2D.Double(centerX,centerY);
                float radius = 20;
                float[] dist = {0.0f,1.0f};
                nodeColor = (nodeColor == null) ? Color.CYAN : nodeColor;
                Color[] colors = {
                        nodeColor,Color.BLACK
                };
                RadialGradientPaint paint = new RadialGradientPaint(center, radius, dist, colors);
                g2.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER,0.6f
                ));
                g2.setPaint(paint);
                g2.fill(currentShape);
                g2.draw(currentShape);
            }

            if (searchShape != null) {
                double centerX = searchShape.getBounds().getCenterX();
                double centerY = searchShape.getBounds().getCenterY();

                g2.setColor(Color.red);
                g2.drawString("founded", (int) centerX , (int) centerY + 30);

                Point2D center = new Point2D.Double(centerX,centerY);
                float radius = 15;
                float[] dist = {0.0f,1.0f};
                Color[] colors = {
                        Color.MAGENTA,Color.BLACK
                };
                RadialGradientPaint paint = new RadialGradientPaint(center, radius, dist, colors);
                g2.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER,0.6f
                ));
                g2.setPaint(paint);
                g2.fill(searchShape);
                g2.draw(searchShape);
            }
            if(minNode != null) {
                double centerX = minNode.getBounds().getCenterX();
                double centerY = minNode.getBounds().getCenterY();

                g2.setColor(Color.red);
                g2.drawString("MIN", (int) centerX , (int) centerY + 30);

                Point2D center = new Point2D.Double(centerX,centerY);
                float radius = 15;
                float[] dist = {0.0f,1.0f};
                Color[] colors = {
                        Color.GREEN,Color.BLACK
                };
                RadialGradientPaint paint = new RadialGradientPaint(center, radius, dist, colors);
                g2.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER,0.6f
                ));
                g2.setPaint(paint);
                g2.fill(minNode);
                g2.draw(minNode);
            }
            if (maxNode != null) {
                double centerX = maxNode.getBounds().getCenterX();
                double centerY = maxNode.getBounds().getCenterY();

                g2.setColor(Color.red);
                g2.drawString("MAX", (int) centerX , (int) centerY + 30);

                Point2D center = new Point2D.Double(centerX,centerY);
                float radius = 15;
                float[] dist = {0.0f,1.0f};
                Color[] colors = {
                        Color.RED,Color.BLACK
                };
                RadialGradientPaint paint = new RadialGradientPaint(center, radius, dist, colors);
                g2.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER,0.6f
                ));
                g2.setPaint(paint);
                g2.fill(maxNode);
                g2.draw(maxNode);
            }
            if (travelNode != null) {
                double centerX = travelNode.getBounds().getCenterX();
                double centerY = travelNode.getBounds().getCenterY();

                g2.setColor(Color.red);
                g2.drawString("Travel", (int) centerX , (int) centerY + 30);

                Point2D center = new Point2D.Double(centerX,centerY);
                float radius = 15;
                float[] dist = {0.0f,1.0f};
                Color[] colors = {
                        Color.YELLOW,Color.GREEN
                };
                RadialGradientPaint paint = new RadialGradientPaint(center, radius, dist, colors);
                g2.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER,0.6f
                ));
                g2.setPaint(paint);
                g2.fill(travelNode);
                g2.draw(travelNode);
            }
        }
        g2.dispose();

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800,700);
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Point point = e.getPoint();
        for(DrawInfo info : shapes) {
            Shape shape = info.shape;
            if (shape.contains(point))
            {
                currentShape = shape;
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                repaint();
                return;
            }else {
                currentShape = null;
                setCursor(null);
                repaint();
            }
        }
    }

    class ComboxModel implements ComboBoxModel<String> , ListDataListener {

        private String[] data;
        /**
         * 这是下拉框中的当前数据模型
         */
        private String currentItem;
        /**
         * 下拉框上一次选择
         */
        private String oldItem;
        /**
         * 上一次选择的索引
         */
        int index0;
        /**
         *当前项目的索引
         */
        int index1;
        private final List<ListDataListener> listeners =
                new ArrayList<>();

        public ComboxModel() {
            data = new String[]{
                    "中序遍历","前序遍历","后序遍历"
            };
            setSelectedItem(getElementAt(0));
            this.addListDataListener(this);
        }

        @Override
        public void setSelectedItem(Object anItem) {
            String item = (String) anItem;
            if (item.equals(currentItem))
                return;//什么也不做
            oldItem = currentItem;
            if (oldItem != null)
                index0 = getIndex(currentItem);
            currentItem = item;
            index1 = getIndex(currentItem);

            changeEvent(currentItem,index0,index1);
        }

        private void changeEvent(String currentItem, int index0, int index1) {
            ListDataEvent event =
                    new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index0, index1);
            for (ListDataListener listener : listeners) {
                listener.contentsChanged(event);
            }
        }

        private int getIndex(String item) {
            for (int i = 0;i<data.length;i++) {
                if (data[i].equals(item))
                    return i;
            }
            return  -1;
        }

        @Override
        public String getSelectedItem() {
            return currentItem;
        }

        @Override
        public int getSize() {
            return data.length;
        }

        @Override
        public String getElementAt(int index) {
            return data[index];
        }

        @Override
        public void addListDataListener(ListDataListener l) {
            listeners.add(l);
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
            listeners.remove(l);
        }

        @Override
        public void intervalAdded(ListDataEvent e) {
            //什么也不做
        }

        @Override
        public void intervalRemoved(ListDataEvent e) {
            //什么也不做
        }

        @Override
        public void contentsChanged(ListDataEvent e) {
            //判断
            travelMode = index1;
            System.out.println("travelMode = " + travelMode);
        }
    }

    class Group<T extends Comparable<? super T>>  {

        /**
         * 二分节点，left引用其左节点
         * right引用其右节点
         */
        public class BinaryNode {
            private T element;
            private BinaryNode left;
            private BinaryNode right;

            public BinaryNode(T element, BinaryNode left, BinaryNode right) {
                this.element = element;
                this.left = left;
                this.right = right;
            }

            public BinaryNode(T element) {
                this(element,null,null);
            }

            /**
             * 判断该节点是否为叶节点
             * @return 如果该节点为叶节点，返回 {@code true};否则返回{@code false}
             */
            public boolean isLeaf() {
                return left == null && right == null;
            }

            @Override
            public String toString() {
                return "BinaryNode{" +
                        "element=" + element +
                        '}';
            }
        }

        /**
         * 根节点
         */
        private BinaryNode root;

        /**
         * 相同层级兄弟节点的间距
         */
        private int xCounter;

        /**
         * 该节点的层级，在ui中这代表着y坐标
         */
        private int level;

        /**
         * 这一个字段表示在每一次迭代的时候水平轴之间的距离
         */
        private int xSpace = 8;

        public Group() {
            clear();
            setBackground(Color.black);
        }

        /**
         * 判断该树是否是一个空树
         * @return 如果树为空树，返回 {@code true};否则返回{@code false}
         */
        public boolean isEmpty() {
            return root == null;
        }
        /**
         * 清空改树
         */
        public void clear() {
            root = null;
        }
        /**
         * 插入节点
         * @param t 插入节点数据
         */
        public void insert(T t) {
            root = insert(t,root);
        }

        /**
         * 内部方法插入节点
         * @param t 节点的数据
         * @param node 根的子树
         * @return 返回新子树的根
         */
        private BinaryNode insert(T t, BinaryNode node) {
            if (node == null) {
                return new BinaryNode(t,null,null);
            }
            int result = t.compareTo(node.element);
            if (result < 0)
                node.left = insert(t,node.left);
            else if (result > 0)
                node.right = insert(t,node.right);
            else
                ;//重复，什么也不做
            return node;
        }

        /**
         * 查找最小的节点
         * @return 最小的节点
         */
        public BinaryNode findMin() {
            return findMin(root);
        }

        /**
         * 私有方法，查询最小节点，请注意最后的递归调用
         * @param node 查询的当前节点，如果为null，则返回null，如果左节点为null则返回当前节点
         * @return 最小节点
         */
        public BinaryNode findMin(BinaryNode node) {
            if (node == null) return null;
            if (node.left == null) return node;
            return findMin(node.left);
        }

        /**
         * 查找最大节点
         * @return 查询节点
         */
        public BinaryNode findMax() {
            return findMax(root);
        }

        /**
         * 查找最大的节点内部方法
         * @param node 当前访问的节点
         * @return 最大的节点
         */
        private BinaryNode findMax(BinaryNode node) {
            if (node == null) return null;
            if (node.right == null) return node;
            return findMax(node.right);
        }

        /**
         * 共共接口
         * @param t 要搜索的值
         * @return 匹配的节点
         */
        public BinaryNode search(T t) {
            return search(t,root);
        }

        /**
         * 内部方法搜索
         * @param t 要搜索的值
         * @param node 搜索的当前节点
         * @return 匹配的节点
         */
        private BinaryNode search(T t, BinaryNode node) {
            if (node == null) return null;
            int result = t.compareTo(node.element);
            if (result < 0)
                return search(t,node.left);
            else if (result > 0) {
                return search(t,node.right);
            }else
                return node;
        }

        /**
         * 中序打印
         */
        public void inorder() {
            inorder(root);
        }

        private void inorder(BinaryNode node) {
            if (node == null) return;
            //打印左子树
            inorder(node.left);
            System.out.println(node);
            inorder(node.right);
        }

        /**
         * 前序遍历
         */
        public void frontOrder() {
            frontOrder(root);
        }

        private void frontOrder(BinaryNode node) {
            if (node == null) return;
            System.out.println(node);
            frontOrder(node.left);
            frontOrder(node.right);
        }

        /**
         * 后序遍历
         */
        public void backOrder() {
            backOrder(root);
        }

        private void backOrder(BinaryNode node) {
            if (node == null) return;
            backOrder(node.left);
            backOrder(node.right);
            System.out.println(node);
        }

        /**
         * 这是二叉树最难的也是最应该讨论的点：
         * 删除情况,有三种基本情况
         * 1-删除的节点是叶子节点，只需要简单的删除就行
         * 2-删除的节点有一个子节点，那么情况有点复杂，但还算不上坏，我们只需要将要删除的节点
         * 与子节点断开联系，然后将父节点与子节点相连，在程序中，这通过：
         * {@code node = (node.left != null) ? node.left : node.right;}这一行表明
         * 3-最复杂的情况当存在两个子节点的时候，我们必须找到删除节点右子树中最小的节点替换掉，这不算
         * 困难，因为我们有{@code findMin}方法，然后使用该节点替换掉删除节点，我们需要遍历删除该节点，
         * 总而言之，这代表着第三种分支
         */
        public void remove(T t) {
            root  = remove(t,root);
        }

        private BinaryNode remove(T t, BinaryNode node) {
            if (node == null) return null;
            int result = t.compareTo(node.element);
            if (result < 0)
                node.left = remove(t,node.left);
            else if (result > 0)
                node.right = remove(t,node.right);
            else if (node.left != null && node.right != null) {
                node.element = findMin(node.right).element;
                node.right = remove(node.element,node.right);
            }
            else
                node = (node.left != null) ? node.left : node.right;
            return node;
        }

        /**
         * 中序遍历数，调用内部方法
         */
        public void showTree() {
            showTree(root);
        }

        /**
         * 采用中序遍历方式打印该树
         * @param node 当前访问的节点
         */
        private void showTree(BinaryNode node) {
            if (node == null) return;
            showTree(node.left);
            System.out.print(node.element);
            showTree(node.right);
        }

        /**
         * 难死了……
         * @param g 绘制工具
         */
        public void draw(Graphics g) {
            xCounter = 0;
            drawNode(g,root,0,50);
        }
//    private void drawNode(
//            Graphics g2,
//            BinaryNode node,
//            int depth,
//            DrawInfo parent,
//            int space,
//            int offset,
//            boolean isLeft)
//    {
//        if (node == null) return;
//        //我该怎么办？
//        int x = 0, y = 0;
//        if (parent == null) {
//            x = 250;
//            y = 50;
//        }else {
//            y = 50 + depth * 50;
//            space = 8;
//            offset = isLeft ? space - 16 + depth: space - depth;
//            x = parent.x + offset *26 / 2;
//            g2.drawLine(parent.x + 15,  parent.y + 15,x + 15,y +15);
//        }
//
//        DrawInfo current = new DrawInfo(x,y,node);
//        xCounter++;
//        //绘制连线
//        g2.setColor(Color.CYAN);
//        g2.fillOval(x, y, 30, 30);
//        g2.setColor(Color.BLACK);
//        g2.drawOval(x, y, 30, 30);
//        g2.drawString(node.element.toString(), x + 10, y + 20);
//        drawNode(g2,node.left,depth + 1,current,space,offset,true);
//        drawNode(g2,node.right,depth + 1,current,space,offset,false);
//    }

        class LayoutInfo {
            int x;        // 当前节点的 x
            int width;    // 整个子树占用的宽度
        }

        /**
         * 绘制节点
         * @param g2 绘图工具
         * @param node 当前节点
         * @param depth 节点深度，从0开始
         * @param leftBound 左子树区域
         * @return 布局信息
         */
        private LayoutInfo drawNode(
                Graphics g2,
                BinaryNode node,
                int depth,
                int leftBound
        ) {
            if (node == null) return null;

            int y = 50 + depth * 70;

            LayoutInfo left = drawNode(g2, node.left, depth + 1, leftBound);
            int leftWidth = left == null ? 0 : left.width;

            LayoutInfo right = drawNode(g2, node.right, depth + 1, leftBound + leftWidth);
            int rightWidth = right == null ? 0 : right.width;

            int nodeWidth = 60;

            int subtreeWidth = Math.max(nodeWidth, leftWidth + rightWidth);

            int x;
            if (left != null && right != null) {
                x = leftBound + leftWidth;
            } else if (left != null) {
                x = left.x;
            } else if (right != null) {
                x = right.x;
            } else {
                x = leftBound;
            }

            // 画连线
            int centerX = x + 15;
            int centerY = y + 15;

            g2.setColor(Color.green);
            if (left != null) {
                g2.drawLine(centerX, centerY,
                        left.x + 15,
                        y + 70 + 15);
            }

            if (right != null) {
                g2.drawLine(centerX, centerY,
                        right.x + 15,
                        y + 70 + 15);
            }

            /**
             * 封装节点信息
             */
            var aDouble = new Ellipse2D.Double(x, y, 30, 30);
            DrawInfo drawInfo = new DrawInfo(aDouble, node.element);
            if (hasDuplicate(drawInfo)) {
                shapes.remove(drawInfo);
            }
            shapes.add(drawInfo);

            LayoutInfo info = new LayoutInfo();
            info.x = x;
            info.width = subtreeWidth;
            return info;
        }

    }

    /**
     * 内部类，封装节点的数据和几何信息
     */
    class DrawInfo {
        Shape shape;
        Object element;

        public DrawInfo(Shape shape, Object element) {
            this.shape = shape;
            this.element = element;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof DrawInfo))
                return false;
            var other = (DrawInfo) obj;
            if (Objects.equals(this.element,other.element)) return true;
            return false;
        }
    }

    /**
     * 是否有重复？这一方法检查{@code shapes}字段所包含的封装值
     * @param info 封装信息
     * @return 布尔值。 {@code true}表示存在重复项，{@code false} 表
     *示不存在重复项，请注意<b>存在项的判断基于节点数据</b>
     * @implNote 该实现只检查节点的数据是否一致，与其他无关
     */
    private boolean hasDuplicate(DrawInfo info) {
        for (DrawInfo drawInfo : shapes)
            return drawInfo.equals(info);
        return false;
    }

    class Task extends SwingWorker<Void,Void> {

        @Override
        protected Void doInBackground() throws Exception {
            if (isTravel) {
                switch (travelMode) {
                    case 1:
                        frontOrder(group.root);
                        break;
                    case 2:
                        backOrder(group.root);
                        break;
                    case 0:
                    default:
                        inorder(group.root);
                        break;
                }
                isTravel = false;
            }else {
                switch (opMode) {
                    case 1:
                        //搜索节点
                        var searched = search(group.root);
                        if (searched == null)
                            break;
                        DrawInfo info = findNodeIn2D(searched);
                        assert info != null;
                        searchShape = info.shape;
                        break;
                    case 2 :
                        //插入节点
                        var node = insertNode(group.root);
                        group.insert(answer,node);
                        break;
                    case 3 :
                        remove(group.root);
                        shapes.clear();
                        break;
                        //删除操作
                    default:
                        break;
                }
                travelNode = null;
            }
            repaint();
            return null;
        }

        private Group.BinaryNode remove(Group.BinaryNode node) throws InterruptedException {
            if (node == null) return null;
            DrawInfo nodeIn2D = findNodeIn2D(node);
            travelNode = nodeIn2D.shape;
            repaint();
            Thread.sleep(500);
            int result = node.element.compareTo(answer);
            if (result < 0)
                node.right = remove(node.right);
            else if(result > 0)
                node.left = remove(node.left);
            else if (node.left != null && node.right != null) {
               node.element =  group.findMin(node.right).element;
               node.right = group.remove(node.element,node.right);
            }
            else
                node = node.left != null ? node.left : node.right;
            return node;

        }

        private Group.BinaryNode insertNode(Group.BinaryNode node) throws InterruptedException {
            if (node == null) {
                return null;
            }
            DrawInfo nodeIn2D = findNodeIn2D(node);
            travelNode = nodeIn2D.shape;
            repaint();
            Thread.sleep(500);
            int result = node.element.compareTo(answer);
            if (result < 0)
                node.right  = insertNode(node.right);
            else if (result > 0) {
                node.left =  insertNode(node.left);
            }
            else
                ;//什么也不做
            return node;
        }

        private Group.BinaryNode search(Group.BinaryNode node) throws InterruptedException {
            if (node == null)
                return null;
            DrawInfo nodeIn2D = findNodeIn2D(node);
            travelNode = nodeIn2D.shape;
            repaint();
            Thread.sleep(500);
            int result = node.element.compareTo(answer);
            if (result < 0)
                return search(node.right);
            else if (result > 0) {
                return search(node.left);
            }
            else
                return node;

        }

        private void inorder(Group.BinaryNode node) throws InterruptedException {
            if (node == null)
                return;
            inorder(node.left);
            System.out.println(node);
            DrawInfo nodeIn2D = findNodeIn2D(node);
            travelNode = nodeIn2D.shape;
            repaint();
            Thread.sleep(500);
            inorder(node.right);
        }

        private void frontOrder(Group.BinaryNode node) throws InterruptedException {
            if (node == null)
                return;
            DrawInfo nodeIn2D = findNodeIn2D(node);
            travelNode = nodeIn2D.shape;
            repaint();
            Thread.sleep(500);
            frontOrder(node.left);
            frontOrder(node.right);
        }

        private void backOrder(Group.BinaryNode node) throws InterruptedException {
            if (node == null) return;
            backOrder(node.left);
            backOrder(node.right);
            DrawInfo nodeIn2D = findNodeIn2D(node);
            travelNode = nodeIn2D.shape;
            repaint();
            Thread.sleep(500);
        }

        @Override
        protected void done() {
            System.out.println("遍历完成");
            beep();
        }
    }

    private void beep() {
        Toolkit.getDefaultToolkit().beep();
    }

}

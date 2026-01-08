package com.snl.data.tree;

public class BinarySearchTreeDemo<T extends Comparable<? super T>> {

    class BinaryNode {
        private T element;
        private BinaryNode left;
        private BinaryNode right;

        public BinaryNode(T element) {
            this(element,null,null);
        }

        public BinaryNode(T element, BinaryNode left, BinaryNode right) {
            this.element = element;
            this.left = left;
            this.right = right;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }
    }

    private BinaryNode root;

    public BinarySearchTreeDemo() {
        clear();
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void clear() {
        root = null;
    }

    public boolean contains(T t) {
        //TODO
        return contains(t,root);
    }

    public void insert(T t) {
        root = insert(t,root);
    }

    public void remove(T t) {
        root = remove(t,root);
    }

    private boolean contains(T t, BinaryNode node) {
        if (node == null) return false;
        int result = t.compareTo(node.element);
        if (result < 0)
            return contains(t,node.left);
        else if (result > 0)
            return contains(t,node.right);
        else
            return true;
    }

    public BinaryNode findMin() {
        return findMin(root);
    }

    private BinaryNode findMin(BinaryNode node) {
        if (node == null)
            return null;
        else if (node.left == null)
            return node;
        else
            return findMin(node.left);
    }

    public BinaryNode findMax() {
        return findMax(root);
    }

    private BinaryNode findMax(BinaryNode node) {
        if (node == null) return null;
        else if(node.right == null)
            return node;
        return findMax(node.right);
    }

    private BinaryNode insert(T t,BinaryNode node) {
        if (node == null)
            return new BinaryNode(t,null,null);
        int result = t.compareTo(node.element);
        if (result < 0)
            node.left =  insert(t,node.left);
        else if(result > 0)
            node.right =  insert(t,node.right);
        else
            ;
        return node;
    }

    private BinaryNode remove(T t, BinaryNode node) {
        if (node == null) return null;
        int result = t.compareTo(node.element);
        if (result < 0)
            node.left = remove(t,node.left);
        else if(result > 0)
            node.right = remove(t,node.right);
        else if (node.left != null && node.right != null) {
            node.element = findMin(node.right).element;
            node.right = remove(node.element, node.right);
        }
        else
            node = (node.left != null) ? node.left : node.right;
        return node;
    }
}

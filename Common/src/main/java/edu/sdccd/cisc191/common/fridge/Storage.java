package edu.sdccd.cisc191.common.fridge;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;


@Entity
public class Storage<T extends Comparable<T>> implements Set<T> {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Transient
    private TreeNode<T> root;


    @Embeddable
    public static class TreeNode<T extends Comparable<T>> {

        private T item;

        private int color;
        private TreeNode<T> left;
        private TreeNode<T> right;

        public TreeNode() {}

        public TreeNode(T item) {
            this.item = item;
            this.color = 1;
            this.left = null;
            this.right = null;
        }
    }

    public Storage() {
        this.root = null;
    }

    @Override
    public int size() {
        return sizeRecursive(root);
    }

    private int sizeRecursive(TreeNode<T> node) {
        if (node == null) {
            return 0;
        }

        return 1 + sizeRecursive(node.left) + sizeRecursive(node.right);
    }

    @Override
    public boolean add(T item) {
        root = addFood(root, item);
        return true;
    }

    private TreeNode<T> addFood(TreeNode<T> node, T item) {
        if (node == null) {
            return new TreeNode<>(item);
        }

        int comparison = item.compareTo(node.item);
        if (comparison < 0) {
            node.left = addFood(node.left, item);
        } else if (comparison > 0) {
            node.right = addFood(node.right, item);
        } else {
            return node;
        }

        return node;
    }

    public T recursiveSearch(T item) {
        return recursiveSearch(root, item);
    }

    private T recursiveSearch(TreeNode<T> node, T item) {
        if (node == null) {
            return null;
        }

        int comparison = item.compareTo(node.item);
        if (comparison < 0) {
            return recursiveSearch(node.left, item);
        } else if (comparison > 0) {
            return recursiveSearch(node.right, item);
        } else {
            return node.item;
        }
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Comparable)) return false;
        T item = (T) o;
        return recursiveSearch(root, item) != null;
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof Comparable)) return false;
        T item = (T) o;
        if (contains(item)) {
            root = removeRecursive(root, item);
            return true;
        }
        return false;
    }

    private TreeNode<T> removeRecursive(TreeNode<T> node, T item) {
        if (node == null) return null;

        int cmp = item.compareTo(node.item);
        if (cmp < 0) {
            node.left = removeRecursive(node.left, item);
        } else if (cmp > 0) {
            node.right = removeRecursive(node.right, item);
        } else {
            // Node to delete found
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // Two children: find in-order successor (smallest in right subtree)
            TreeNode<T> minNode = findMin(node.right);
            node.item = minNode.item;
            node.right = removeRecursive(node.right, minNode.item);
        }
        return node;
    }

    private TreeNode<T> findMin(TreeNode<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        root = null;
    }

}
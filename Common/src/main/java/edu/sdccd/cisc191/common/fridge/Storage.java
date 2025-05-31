package edu.sdccd.cisc191.common.fridge;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * This class defines the behaviors of the storage for fooditems.
 * Storage is a binary search tree using recursive search to add and remove foods from the data structure
 * Implements the set interface with complying overriding methods
 * SpringBoot Component as it is syncing with the database
 * @param <T> the object type in which the storage will store (FoodItem)
 */
//test
@Component
public class Storage<T extends Comparable<T>> implements Set<T> {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Transient
    private TreeNode<T> root;


    /**
     * Defines a node of the binary search tree which will be traversed. Each node represents a FoodItem
     * @param <T> the object type in which the storage will store (FoodItem)
     */
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

    /**
     * recursively calls sizeRecursive() method to count the number of nodes/items in storage
     * @return the size of storage
     */
    @Override
    public int size() {
        return sizeRecursive(root);
    }

    //method called recursively to count nodes
    //empty nodes = 0, nonempty nodes = 1
    private int sizeRecursive(TreeNode<T> node) {
        if (node == null) {
            return 0;
        }

        return 1 + sizeRecursive(node.left) + sizeRecursive(node.right);
    }

    /**
     * adds a fooditem recursively to the storage binary search tree
     * @param item element whose presence in this collection is to be ensured
     * @return true when the operation is completed
     */
    @Override
    public boolean add(T item) {
        root = addFood(root, item);
        return true;
    }

    //recursively adds a new node/food to the tree
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

    /**
     * Method to recursively search for a fooditem
     * @param item the fooditem that needs to be searched
     * @return the item searched for
     */
    public T recursiveSearch(T item) {
        return recursiveSearch(root, item);
    }

    //recurively serach for food in binary search tree
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

    /**
     * method to determine if storage is empty or not
     * @return true or false depending on if the root is null or not
     */
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Method to determine if a food is in storage
     * @param o element whose presence in this set is to be tested
     * @return if a food is in the storage
     */
    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Comparable)) return false;
        T item = (T) o;
        return recursiveSearch(root, item) != null;
    }

    //methods to override as part of the set interface
    @Override
    public Iterator<T> iterator() {
        List<T> items = new ArrayList<>();
        inOrderTraversal(root, items);
        return items.iterator();
    }

    private void inOrderTraversal(TreeNode<T> node, List<T> items) {
        if (node != null) {
            inOrderTraversal(node.left, items);
            items.add(node.item);
            inOrderTraversal(node.right, items);
        }
    }


    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    /**
     * method to remove a node/food from storage
     * @param o object to be removed from this set, if present
     * @return true or false if the item is present and has been deleted
     */
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

    //recursively remove a fooditem
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


    //more methods to override as part of the set interface
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

    /**
     * clears the storage
     */
    @Override
    public void clear() {
        root = null;
    }

}
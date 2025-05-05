package edu.sdccd.cisc191.common.fridge;

import edu.sdccd.cisc191.common.model.FoodItem;
import java.util.TreeSet;

public class Storage <T extends Comparable<T>> extends TreeSet<T> {
    private final TreeSet<T> foodItemTreeSet;

    public Storage() {
        this.foodItemTreeSet = new TreeSet<>();
    }

    public int getItemCount() {
        return foodItemTreeSet.size();
    }

    public void addFood(T item) {
        foodItemTreeSet.add(item);
    }

    public boolean removeFood(T item) {
        return foodItemTreeSet.remove(item);
    }

    public T findFoodByName(String name) {
        return recursiveSearch(name, foodItemTreeSet.toArray((T[])new Comparable[0]), 0);
    }

    public T recursiveSearch(String name, T[] items, int index) {
        if (index >= items.length) {
            return null;
        }

        if(items[index] instanceof FoodItem foodItem && foodItem.getName().equalsIgnoreCase(name)) {
            return items[index];

        }

        return recursiveSearch(name, items, index + 1);
    }

    public void clear() {
        foodItemTreeSet.clear();
    }

    public TreeSet<T> getAll() {
        return foodItemTreeSet;
    }
}
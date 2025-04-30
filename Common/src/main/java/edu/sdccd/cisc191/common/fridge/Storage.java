package edu.sdccd.cisc191.common.fridge;

import edu.sdccd.cisc191.common.model.FoodItem;
import java.util.TreeSet;

/**
 * Describes the attributes, methods, and behavior of a storage class
 * FoodItem[][] fridge = the 2D array that will store all the FoodItem and Drink objects like a fridge
 * int fridgeSize = the amount of food items the fridge can store
 * int itemCount = the number of food items in the fridge (initially set to 0)
 */
public class Storage extends TreeSet<FoodItem>{
    private final TreeSet<FoodItem> foodItemTreeSet;

    public Storage() {
        foodItemTreeSet = new TreeSet<FoodItem>();
    }

    public int getItemCount() {
        return foodItemTreeSet.size();
    }

    public void addFood(FoodItem item) {
        foodItemTreeSet.add(item);
    }

    public boolean removeFood(FoodItem item) {
        return foodItemTreeSet.remove(item);
    }

    public FoodItem findFoodByName(String name) {
        return recursiveSearch(name, foodItemTreeSet.toArray(new FoodItem[0]), 0);
    }

    public FoodItem recursiveSearch(String name, FoodItem[] items, int index) {
        if (index >= items.length) {
            return null;
        }

        if(items[index].getName().equalsIgnoreCase(name)) {
            return items[index];
        }

        return recursiveSearch(name, items, index + 1);
    }
}
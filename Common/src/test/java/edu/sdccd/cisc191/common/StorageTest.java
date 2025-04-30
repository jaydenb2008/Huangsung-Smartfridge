package edu.sdccd.cisc191.common;

import edu.sdccd.cisc191.common.fridge.Storage;
import edu.sdccd.cisc191.common.model.Drink;
import edu.sdccd.cisc191.common.model.FoodItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StorageTest {
    private Storage testFridge;
    FoodItem tomatoes, eggs;
    Drink milk, lemonade;

    @BeforeEach
    void setUpStorage() {
        testFridge = new Storage();

        tomatoes = new FoodItem("Tomatoes", "Vegetable", 3.0f, FoodItem.convertToDate("03-23-2025"));
        eggs = new FoodItem("Eggs", "Poultry", 12.0f, FoodItem.convertToDate("03-16-2025"));
        milk = new Drink("Milk", "Dairy", 0.5f, Drink.convertToDate("04-16-2025"), true);
        lemonade = new Drink("Lemonade", "Juice", 1.0f, Drink.convertToDate("05-02-2025"), false);
    }

    @Test
    void testAddFindFood() throws Exception {
        testFridge.addFood(tomatoes);

        FoodItem result = testFridge.findFoodByName("Tomatoes");
        assertNotNull(result);
        assertEquals("Tomatoes", result.getName());
    }

    @Test
    void testRemoveFood() {
        testFridge.addFood(milk);
        assertTrue(testFridge.removeFood(milk));
        assertNull(testFridge.findFoodByName("Milk"));
    }

    @Test
    void testFindNonexistentFood() {
        assertNull(testFridge.findFoodByName("Strawberries"));
    }
}

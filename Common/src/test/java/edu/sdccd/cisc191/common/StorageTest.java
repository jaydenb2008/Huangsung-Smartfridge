package edu.sdccd.cisc191.common;

import edu.sdccd.cisc191.common.fridge.Storage;
import edu.sdccd.cisc191.common.model.FoodItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class StorageTest {
    private Storage<FoodItem> testFridge;
    FoodItem tomatoes, eggs;


    @BeforeEach
    void setUpStorage() {
        testFridge = new Storage();

        tomatoes = new FoodItem("Tomatoes", "Vegetable", 3.0f, LocalDate.now(), true);
        eggs = new FoodItem("Eggs", "Poultry", 12.0f, LocalDate.now(), false);

    }

    @Test
    void testAddFindFood() {
        testFridge.add(tomatoes);

        assertEquals(1, testFridge.size());
        FoodItem foundItem = testFridge.recursiveSearch(tomatoes);
        assertNotNull(foundItem);
        assertEquals("Tomatoes", foundItem.getName());
    }

    @Test
    void testRemoveFood() {
        testFridge.add(eggs);
        assertTrue(testFridge.contains(eggs));

        assertTrue(testFridge.remove(eggs));
        assertFalse(testFridge.contains(eggs));

    }
}

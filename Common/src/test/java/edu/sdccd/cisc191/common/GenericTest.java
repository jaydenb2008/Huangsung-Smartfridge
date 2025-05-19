package edu.sdccd.cisc191.common;

import edu.sdccd.cisc191.common.fridge.Storage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenericTest {

    @Test
    void testGenericFridge() {
        Storage<String> stringStorage = new Storage<>();

        stringStorage.add("Ketchup");
        stringStorage.add("Chicken");

        assertEquals(2, stringStorage.size());
    }

}

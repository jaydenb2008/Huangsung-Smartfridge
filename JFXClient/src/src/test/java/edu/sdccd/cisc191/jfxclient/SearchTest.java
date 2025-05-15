package edu.sdccd.cisc191.jfxclient;

import edu.sdccd.cisc191.common.fridge.Storage;
import edu.sdccd.cisc191.common.model.FoodItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

public class SearchTest {
    private Storage<FoodItem> fridge;

    @BeforeEach
    void setup() {
        fridge = new Storage<>();
        fridge.add(new FoodItem("bread", "grains", 1.0f, new Date()));
        fridge.add(new FoodItem("broccoli", "vegetable", 3.0f, new Date()));
    }

    @Test
    void testOneItemSearch() {
        String query = "br"; // Should match "Broccoli" and "Bread"

        Set<FoodItem> result = fridge.stream()
                .filter(item -> item.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toSet());

        assertEquals(2, result.size());

        boolean containsBroccoli = result.stream().anyMatch(f -> f.getName().equalsIgnoreCase("Broccoli"));
        boolean containsBread = result.stream().anyMatch(f -> f.getName().equalsIgnoreCase("Bread"));

        assertTrue(containsBroccoli);
        assertTrue(containsBread);
    }
}

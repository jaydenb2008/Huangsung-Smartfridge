package edu.sdccd.cisc191.server;

import edu.sdccd.cisc191.common.fridge.Storage;
import edu.sdccd.cisc191.common.model.FoodItem;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NetworkingTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private Storage<FoodItem> storage;

    private final String baseUrl = "/api/food/foods";

    private FoodItem testItem = new FoodItem("Network Apple", "Fruit", 1.5f, LocalDate.of(2025, 6, 1), true);

    @Test
    @Order(1)
    void testCreateFoodItem() {
        ResponseEntity<FoodItem> response = restTemplate.postForEntity(baseUrl, testItem, FoodItem.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        FoodItem saved = response.getBody();
        assertNotNull(saved);
        assertEquals("Network Apple", saved.getName());

        // Because we can’t use getId(), check that the object is now in storage
        assertTrue(storage.contains(saved), "Storage should contain the created item");
    }

    @Test
    @Order(2)
    void testUpdateFoodItem() {
        // First create an item
        FoodItem saved = restTemplate.postForObject(baseUrl, testItem, FoodItem.class);
        saved.setName("Updated Apple");

        // Update it
        restTemplate.put(baseUrl + "/" + saved.getId(), saved);

        // Re-fetch and check
        FoodItem updated = restTemplate.getForObject(baseUrl + "/" + saved.getId(), FoodItem.class);
        assertEquals("Updated Apple", updated.getName());
    }

    @Test
    @Order(3)
    void testDeleteFoodItem() {
        // Create
        FoodItem saved = restTemplate.postForObject(baseUrl, testItem, FoodItem.class);

        // Delete
        restTemplate.delete(baseUrl + "/" + saved.getId());

        // Confirm it's gone via GET
        ResponseEntity<FoodItem> response = restTemplate.getForEntity(baseUrl + "/" + saved.getId(), FoodItem.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(4)
    void testGetAllFoodItems() {
        restTemplate.postForObject(baseUrl, testItem, FoodItem.class);

        ResponseEntity<FoodItem[]> response = restTemplate.getForEntity(baseUrl, FoodItem[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        List<FoodItem> items = Arrays.asList(response.getBody());
        assertTrue(items.stream().anyMatch(f -> f.getName().equals("Network Apple")));
    }
}

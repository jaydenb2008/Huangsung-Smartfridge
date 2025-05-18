package edu.sdccd.cisc191.server;

import edu.sdccd.cisc191.common.fridge.Storage;
import edu.sdccd.cisc191.common.model.FoodItem;
import edu.sdccd.cisc191.server.repositories.FoodRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseTest {
    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private Storage<FoodItem> storage;

    private FoodItem item;
    private FoodItem savedItem;

    @BeforeEach
    void setup() {
        item = new FoodItem("steak", "meat", 2.0f, LocalDate.now(), false);

    }

    @Test
    @Order(1)
    void testCreateAndSync() {
        savedItem = foodRepository.save(item);
        assertNotNull(savedItem.getId(), "saved items should have an associated id");

        //storage sync
        assertTrue(storage.contains(savedItem));
    }

    @Test
    @Order(2)
    void testRead() {
        Optional<FoodItem> found = foodRepository.findById(savedItem.getId());
        assertTrue(found.isPresent(), "Item should be found by id");
        assertEquals("steak", found.get().getName());
        assertEquals("meat", found.get().getFoodType());
        assertEquals(2.0f, found.get().getQuantityLeft());
        assertEquals(LocalDate.now(), found.get().getExpirationDate());
        assertFalse(found.get().getOpened());

        //storage sync
    }

    @Test
    @Order(3)
    void testUpdateAndSync() {
        savedItem.setQuantityLeft(1.0f);
        savedItem.setOpened(true);

        foodRepository.save(savedItem);

        Optional<FoodItem> updatedItem = foodRepository.findById(savedItem.getId());
        assertTrue(updatedItem.isPresent(), "item should still be in database after update");

        assertEquals(1.0f, updatedItem.get().getQuantityLeft());
        assertTrue(updatedItem.get().getOpened());

        //storage sync
    }

    @Test
    @Order(4)
    void testDeleteAndSync() {
        foodRepository.deleteById(savedItem.getId());
        assertFalse(foodRepository.findById(savedItem.getId()).isPresent(), "Item should be deleted from database");

        //storage sync
    }
}

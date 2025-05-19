package edu.sdccd.cisc191.server;

import edu.sdccd.cisc191.common.fridge.Storage;
import edu.sdccd.cisc191.common.model.FoodItem;
import edu.sdccd.cisc191.server.repositories.FoodRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseTest {
    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private Storage<FoodItem> storage;

    private FoodItem item;
    private FoodItem saved;

    @BeforeEach
    void setup() {
        item = new FoodItem("steak", "meat", 2.0f, LocalDate.now(), false);
        foodRepository.deleteAll();
        storage.clear();
    }

    @Test
    @Order(1)
    void testCreateAndSync() {
        saved = foodRepository.save(item);
        storage.add(saved);

        Optional<FoodItem> foundInDB = foodRepository.findById(saved.getId());
        assertTrue(foundInDB.isPresent(), "Item should be found by id");

        //check storage
        assertTrue(storage.contains(saved));
    }

    @Test
    @Order(2)
    void testRead() {
        saved = foodRepository.save(item);
        storage.add(saved);

        Optional<FoodItem> found = foodRepository.findById(saved.getId());
        assertTrue(found.isPresent(), "Item should be found by id");
        assertEquals("steak", found.get().getName());
        assertEquals("meat", found.get().getFoodType());
        assertEquals(2.0f, found.get().getQuantityLeft());
        assertEquals(LocalDate.now(), found.get().getExpirationDate());
        assertFalse(found.get().getOpened());

        //storage sync
        FoodItem foundInStorage = storage.recursiveSearch(saved);
        assertTrue(storage.contains(saved));
        assertEquals("steak", foundInStorage.getName());
        assertEquals("meat", foundInStorage.getFoodType());
        assertEquals(2.0f, foundInStorage.getQuantityLeft());
        assertEquals(LocalDate.now(), foundInStorage.getExpirationDate());
        assertFalse(foundInStorage.getOpened());
    }

    @Test
    @Order(3)
    void testUpdateAndSync() {
        saved = foodRepository.save(item);
        storage.add(saved);

        saved.setQuantityLeft(1.0f);
        saved.setOpened(true);

        foodRepository.save(saved);
        storage.add(saved);

        Optional<FoodItem> updatedItem = foodRepository.findById(saved.getId());
        assertTrue(updatedItem.isPresent(), "item should still be in database after update");

        assertEquals(1.0f, updatedItem.get().getQuantityLeft());
        assertTrue(updatedItem.get().getOpened());

        //storage sync
        FoodItem found = storage.recursiveSearch(saved);
        assertNotNull(found);
        assertEquals(1.0f, found.getQuantityLeft());
        assertTrue(found.getOpened());
    }

    @Test
    @Order(4)
    void testDeleteAndSync() {
        saved = foodRepository.save(item);
        storage.add(saved);

        foodRepository.deleteById(saved.getId());
        assertFalse(foodRepository.findById(saved.getId()).isPresent(), "Item should be deleted from database");

        //storage sync
        storage.remove(saved);
        assertFalse(storage.contains(saved));
    }
}

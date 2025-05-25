package edu.sdccd.cisc191.server.config;

import edu.sdccd.cisc191.common.fridge.Storage;
import edu.sdccd.cisc191.common.model.FoodItem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class configures a storage object for CRUD in the sever submodule
 */

@Configuration
public class StorageConfig {

    @Bean
    public Storage<FoodItem> storage() {

        // TODO: Replace in-memory storage with database-backed repository for persistent storage[2].

        Storage<FoodItem> storage = new Storage<>();

        // Example: Add a sample FoodItem to demonstrate initialization.
        FoodItem sampleItem = new FoodItem("Milk", "2025-06-01", 2);
        storage.add(sampleItem);

        return new Storage<>();
    }
}

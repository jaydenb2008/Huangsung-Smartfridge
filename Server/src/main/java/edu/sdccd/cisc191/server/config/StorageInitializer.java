package edu.sdccd.cisc191.server.config;

import edu.sdccd.cisc191.common.fridge.Storage;
import edu.sdccd.cisc191.common.model.FoodItem;
import edu.sdccd.cisc191.server.repositories.FoodRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * This class initializes a food item storage and a jpa food repository
 */

@Component
public class StorageInitializer {
    private final FoodRepository foodRepository;
    private final Storage<FoodItem> storage;


    public StorageInitializer(FoodRepository foodRepository, Storage<FoodItem> storage) {
        this.foodRepository = foodRepository;
        this.storage = storage;
    }

    @PostConstruct
    public void init() {
        storage.clear();
        foodRepository.findAll().forEach(storage::add);
    }
}

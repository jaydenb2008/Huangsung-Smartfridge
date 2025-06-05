package edu.sdccd.cisc191.server.controllers;

import java.util.List;

import edu.sdccd.cisc191.common.fridge.Storage;
import edu.sdccd.cisc191.common.model.FoodItem;
import edu.sdccd.cisc191.server.exceptions.FoodNotFoundException;
import edu.sdccd.cisc191.server.repositories.FoodRepository;
import org.springframework.web.bind.annotation.*;

/**
 * This class allows the API to interface with storage and the database and perform
 * CRUD operations.
 */

@RestController
@RequestMapping("/api/food")
class FoodController {

    private final FoodRepository foodRepository;
    private final Storage<FoodItem> storage;

    FoodController(FoodRepository foodRepository, Storage<FoodItem> storage) {
        this.foodRepository = foodRepository;
        this.storage = storage;
    }


    // READ ALL
    @GetMapping("/foods")
    List<FoodItem> all() {
        return foodRepository.findAll();
    }

    //CREATE food
    @PostMapping("/foods")
    FoodItem newFood(@RequestBody FoodItem newFoodItem) {
        FoodItem saved = foodRepository.save(newFoodItem);
        storage.add(saved);
        return saved;
    }

    //READ one food
    @GetMapping("/foods/{id}")
    FoodItem one(@PathVariable Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new FoodNotFoundException(id));
    }

    //UPDATE food
    @PutMapping("/foods/{id}")
    FoodItem updateFood(@RequestBody FoodItem newFoodItem, @PathVariable Long id) {
        return foodRepository.findById(id)
                .map(food -> {
                    storage.remove(food);
                    food.setName(newFoodItem.getName());
                    food.setFoodType(newFoodItem.getFoodType());
                    food.setQuantityLeft(newFoodItem.getQuantityLeft());
                    food.setExpirationDate(newFoodItem.getExpirationDate());
                    food.setOpened(newFoodItem.getOpened());

                    FoodItem updated = foodRepository.save(food);
                    storage.add(updated);
                    return updated;
                })
                .orElseGet(() -> {
                    FoodItem saved = foodRepository.save(newFoodItem);
                    storage.add(saved);
                    return saved;
                    //TODO: set newFoodItem id for clarity
                });
    }

    @DeleteMapping("/foods/{id}")
    void deleteFood(@PathVariable Long id) {
        FoodItem item = foodRepository.findById(id)
                .orElseThrow(() -> new FoodNotFoundException(id));
        storage.remove(item);
        foodRepository.deleteById(id);
    }
}
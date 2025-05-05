package edu.sdccd.cisc191.server.controllers;

import java.util.List;

import edu.sdccd.cisc191.common.fridge.Storage;
import edu.sdccd.cisc191.common.model.Drink;
import edu.sdccd.cisc191.common.model.FoodItem;
import edu.sdccd.cisc191.server.exceptions.FoodNotFoundException;
import edu.sdccd.cisc191.server.repositories.FoodRepository;
import org.springframework.web.bind.annotation.*;

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
        storage.addFood(saved);
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
    FoodItem replaceFood(@RequestBody FoodItem newFoodItem, @PathVariable Long id) {
        return foodRepository.findById(id)
                .map(food -> {
                    storage.removeFood(food);
                    food.setName(newFoodItem.getName());
                    food.setFoodType(newFoodItem.getFoodType());
                    food.setQuantityLeft(newFoodItem.getQuantityLeft());
                    food.setExpirationDate(newFoodItem.getExpirationDate());

                    if (food instanceof Drink && newFoodItem instanceof Drink) {
                        ((Drink) food).setOpened(((Drink) newFoodItem).isOpened());
                    }

                    FoodItem updated = foodRepository.save(food);
                    storage.addFood(updated);
                    return updated;
                })
                .orElseGet(() -> {
                    FoodItem saved = foodRepository.save(newFoodItem);
                    storage.addFood(saved);
                    return saved;
                });
    }

    @DeleteMapping("/foods/{id}")
    void deleteFood(@PathVariable Long id) {
        foodRepository.findById(id).ifPresent(food -> {
            storage.removeFood(food);
            foodRepository.deleteById(id);
        });
    }
}
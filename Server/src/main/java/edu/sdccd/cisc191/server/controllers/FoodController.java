package edu.sdccd.cisc191.server.controllers;

import java.util.List;

import edu.sdccd.cisc191.common.model.FoodItem;
import edu.sdccd.cisc191.server.exceptions.FoodNotFoundException;
import edu.sdccd.cisc191.server.repositories.FoodRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/food")
class FoodController {

    private final FoodRepository repository;

    FoodController(FoodRepository repository) {
        this.repository = repository;
    }


    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/foods")
    List<FoodItem> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/foods")
    FoodItem newUser(@RequestBody FoodItem newFoodItem) {
        return repository.save(newFoodItem);
    }

    // Single item

    @GetMapping("/foods/{id}")
    FoodItem one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new FoodNotFoundException(id));
    }

    @PutMapping("/foods/{id}")
    FoodItem replaceUser(@RequestBody FoodItem newFoodItem, @PathVariable Long id) {

        return repository.findById(id)
                .map(user -> {
                    user.setName(newFoodItem.getName());
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    return repository.save(newFoodItem);
                });
    }

    @DeleteMapping("/foods/{id}")
    void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
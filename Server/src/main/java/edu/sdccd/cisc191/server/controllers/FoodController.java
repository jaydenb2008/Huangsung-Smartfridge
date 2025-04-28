package edu.sdccd.cisc191.server.controllers;

import java.util.List;

import edu.sdccd.cisc191.common.model.FoodItem;
import edu.sdccd.cisc191.server.exceptions.FoodNotFoundException;
import edu.sdccd.cisc191.server.repositories.FoodRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class FoodController {

    private final FoodRepository repository;

    FoodController(FoodRepository repository) {
        this.repository = repository;
    }


    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/users")
    List<FoodItem> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/users")
    FoodItem newUser(@RequestBody FoodItem newFoodItem) {
        return repository.save(newFoodItem);
    }

    // Single item

    @GetMapping("/users/{id}")
    FoodItem one(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new FoodNotFoundException(id));
    }

    @PutMapping("/users/{id}")
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

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
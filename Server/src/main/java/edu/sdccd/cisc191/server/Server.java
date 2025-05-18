package edu.sdccd.cisc191.server;

import edu.sdccd.cisc191.common.fridge.Storage;
import edu.sdccd.cisc191.common.model.FoodItem;
import edu.sdccd.cisc191.server.repositories.FoodRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDate;

/**
 * This class starts up the Spring Boot Application which
 * adds food items to the repository
 */
@SpringBootApplication
@EntityScan("edu.sdccd.cisc191.common.model")
public class Server implements CommandLineRunner {
    private final FoodRepository foodRepository;
    private final Storage<FoodItem> storage;

    public Server(FoodRepository foodRepository, Storage<FoodItem> storage) {
        this.foodRepository = foodRepository;
        this.storage = storage;
    }

    //start up the server
    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        FoodItem apple = new FoodItem();
        apple.setName("apple");
        apple.setFoodType("fruit");
        apple.setExpirationDate(LocalDate.of(2025, 7, 24));
        apple.setQuantityLeft(5.0f);
        apple.setOpened(false);
        storage.add(apple);
        foodRepository.save(apple);

    }
}

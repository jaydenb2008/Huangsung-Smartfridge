package edu.sdccd.cisc191.server;

import edu.sdccd.cisc191.common.model.Drink;
import edu.sdccd.cisc191.common.model.FoodItem;
import edu.sdccd.cisc191.server.repositories.FoodRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/**
 * This class starts up the Spring Boot Application which
 * adds food items to the repository
 */
@SpringBootApplication
@EntityScan("edu.sdccd.cisc191.common.model")
public class Server implements CommandLineRunner {
    private final FoodRepository foodRepository;

    public Server(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
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
        apple.setExpirationDate(FoodItem.convertToDate("06-04-2026"));
        apple.setQuantityLeft(5.0f);
        foodRepository.save(apple);

        Drink milk = new Drink();
        milk.setName("cheese");
        milk.setFoodType("dairy");
        milk.setExpirationDate(FoodItem.convertToDate("09-26-2026"));
        milk.setQuantityLeft(0.75f);
        foodRepository.save(milk);
    }
}

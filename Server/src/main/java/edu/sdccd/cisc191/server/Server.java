package edu.sdccd.cisc191.server;

import edu.sdccd.cisc191.common.model.FoodItem;
import edu.sdccd.cisc191.server.repositories.FoodRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("edu.sdccd.cisc191.common.model")
public class Server implements CommandLineRunner {
    private final FoodRepository foodRepository;

    public Server(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        FoodItem apple = new FoodItem();
        apple.setName("apple");
        foodRepository.save(apple);

        FoodItem cheese = new FoodItem();
        cheese.setName("cheese");
        foodRepository.save(cheese);
    }
}

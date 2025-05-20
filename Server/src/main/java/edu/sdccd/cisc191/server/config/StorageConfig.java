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
        return new Storage<>();
    }
}

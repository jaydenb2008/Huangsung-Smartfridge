package edu.sdccd.cisc191.server.repositories;

import edu.sdccd.cisc191.common.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * This is the database where FoodItems will be persisted
 */

public interface FoodRepository extends JpaRepository<FoodItem, Long> {

    // Example public method: find all FoodItems by food type
    List<FoodItem> findByFoodType(String foodType);

    // TODO (11.1 Databases): Add a query method to find expired FoodItems.
    // TODO (11.1 Databases): Optimize queries with appropriate indexes for frequent search fields.
}

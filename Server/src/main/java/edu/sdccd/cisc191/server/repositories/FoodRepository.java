package edu.sdccd.cisc191.server.repositories;

import edu.sdccd.cisc191.common.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * This is the database where FoodItems will be persisted
 */

public interface FoodRepository extends JpaRepository<FoodItem, Long> {
}

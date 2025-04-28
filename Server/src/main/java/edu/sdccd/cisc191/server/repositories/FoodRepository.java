package edu.sdccd.cisc191.server.repositories;

import edu.sdccd.cisc191.common.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<FoodItem, Long> {
}

package edu.sdccd.cisc191.server.repositories;

import edu.sdccd.cisc191.common.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

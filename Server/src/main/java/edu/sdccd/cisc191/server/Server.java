package edu.sdccd.cisc191.server;

import edu.sdccd.cisc191.common.model.User;
import edu.sdccd.cisc191.server.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("edu.sdccd.cisc191.common.model")
public class Server implements CommandLineRunner {
    private final UserRepository userRepository;

    public Server(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        User alice = new User();
        alice.setName("Alice");
        alice.setEmail("alice@smith.com");
        userRepository.save(alice);

        User bob = new User();
        bob.setName("Bob");
        bob.setEmail("bob@smith.com");
        userRepository.save(bob);
    }
}

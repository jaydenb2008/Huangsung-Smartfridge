package edu.sdccd.cisc191.webclient.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import edu.sdccd.cisc191.common.model.User;

import java.util.List;

@Service
public class UserService {
    private final RestTemplate restTemplate;

    @Value("${spring.data.rest.base-path}")
    private String baseUrl;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<User> getAllUsers() {
        return restTemplate.getForObject(baseUrl + "/users", List.class);
    }

}

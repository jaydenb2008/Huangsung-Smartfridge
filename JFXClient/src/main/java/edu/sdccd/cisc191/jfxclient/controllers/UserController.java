package edu.sdccd.cisc191.jfxclient.controllers;

import edu.sdccd.cisc191.common.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class UserController implements Initializable {

    @FXML
    private TableView<User> personTable;
    @FXML
    private TableColumn<User, Long> idColumn;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;

    private final RestTemplate restTemplate;
    @Value("${spring.data.rest.base-path}")
    private String basePath;
    public UserController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadUserData();
    }

    private void loadUserData() {

        String apiUrl = basePath + "/users";
        User[] users = restTemplate.getForObject(apiUrl, User[].class);

        if (users != null) {
            List<User> personList = Arrays.asList(users);
            ObservableList<User> observableList = FXCollections.observableArrayList(personList);
            personTable.setItems(observableList);
        } else {
            // Handle the case where the API request fails or returns null
            System.err.println("Failed to load person data from the API.");
        }
    }
}
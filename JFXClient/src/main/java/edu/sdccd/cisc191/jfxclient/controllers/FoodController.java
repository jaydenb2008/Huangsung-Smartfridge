package edu.sdccd.cisc191.jfxclient.controllers;

import edu.sdccd.cisc191.common.model.FoodItem;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
public class FoodController implements Initializable {

    @FXML
    private TableView<FoodItem> foodTable;
    @FXML
    private TableColumn<FoodItem, Long> idColumn;
    @FXML
    private TableColumn<FoodItem, String> nameColumn;
    @FXML
    private TableColumn<FoodItem, String> foodTypeColumn;
    @FXML
    private TableColumn<FoodItem, Float> quantityColumn;
    @FXML
    private TableColumn<FoodItem, Date> expirationColumn;
    @FXML
    private TextField searchField;
    private final ObservableList<FoodItem> fullFoodList = FXCollections.observableArrayList();

    private final RestTemplate restTemplate;
    @Value("${spring.data.rest.base-path}")
    private String basePath;
    public FoodController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        foodTypeColumn.setCellValueFactory(new PropertyValueFactory<>("foodType"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantityLeft"));
        expirationColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterList(newValue));

        loadFoodData();
    }

    private void filterList(String query) {
        if (query == null || query.isEmpty()) {
            foodTable.setItems(fullFoodList);
        } else {
            ObservableList<FoodItem> filtered = fullFoodList.stream()
                    .filter(item -> item.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            foodTable.setItems(filtered);
        }
    }

    private void loadFoodData() {
        String apiUrl = basePath + "/api/food/foods";

        try {
            FoodItem[] foodItems = restTemplate.getForObject(apiUrl, FoodItem[].class);
            if (foodItems != null) {
                List<FoodItem> foodList = Arrays.asList(foodItems);
                fullFoodList.setAll(foodList); // ensures filtering works
                foodTable.setItems(fullFoodList);
                checkExpirations(foodList);
            } else {
                // Handle the case where the API request fails or returns null
                System.err.println("Failed to load food data from the API.");
            }
        } catch (ResourceAccessException e) {
            System.err.println("Could not reach server: " + e.getMessage());
        }
    }

    private void checkExpirations(List<FoodItem> foodList) {
        StringBuilder upcoming = new StringBuilder();
        StringBuilder expired = new StringBuilder();

        for (FoodItem item : foodList) {
            Date now = new Date();
            long diff = item.getExpirationDate().getTime() - now.getTime();
            long days = diff / (1000 * 60 * 60 * 24);

            if (days < 0) {
                expired.append(item.getName()).append(" has expired!\n");
            } else if (days <= 3) {
                upcoming.append(item.getName()).append(" will expire in ").append(days).append(" day(s).\n");
            }
        }

        StringBuilder message = new StringBuilder();
        if (!expired.isEmpty()) {
            message.append("❗ Expired Items:\n").append(expired);
        }
        if (!upcoming.isEmpty()) {
            message.append("\n⏳ Upcoming Expirations:\n").append(upcoming);
        }

        if (!message.isEmpty()) {
            showExpirationAlert(message.toString());
        }
    }

    private void showExpirationAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Expiration Alert");
            alert.setHeaderText("Fridge Notification");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    @FXML
    private void handleAdd() {
        FoodItem newItem = new FoodItem( "New Food", "Snack", 1.0f, new Date());
        FoodItem savedItem = restTemplate.postForObject(basePath + "/foods", newItem, FoodItem.class);
        if (savedItem != null) {
            fullFoodList.add(savedItem);
            foodTable.setItems(fullFoodList);
        }
    }

    @FXML
    private void handleRemove() {
        FoodItem selected = foodTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            restTemplate.delete(basePath + "/foods/" + selected.getId());
            fullFoodList.remove(selected);
            foodTable.setItems(fullFoodList);
        }
    }

    @FXML
    private void handleUpdate() {
        FoodItem selected = foodTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setName("Updated food");


            restTemplate.put(basePath + "/foods/" + selected.getId(), selected);
            foodTable.refresh();
        }
    }

}
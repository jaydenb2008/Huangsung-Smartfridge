package edu.sdccd.cisc191.jfxclient.controllers;

import edu.sdccd.cisc191.common.model.FoodItem;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

@Component
public class FoodController implements Initializable {

    @FXML private TableView<FoodItem> foodTable;
    @FXML private TableColumn<FoodItem, Long> idColumn;
    @FXML private TableColumn<FoodItem, String> nameColumn;
    @FXML private TableColumn<FoodItem, String> foodTypeColumn;
    @FXML private TableColumn<FoodItem, Float> quantityColumn;
    @FXML private TableColumn<FoodItem, Date> expirationColumn;
    @FXML private TextField searchField;

    private final ObservableList<FoodItem> fullFoodList = FXCollections.observableArrayList();
    private final RestTemplate restTemplate;

    @Value("${spring.data.rest.base-path}")
    private String basePath;

    public FoodController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        foodTypeColumn.setCellValueFactory(new PropertyValueFactory<>("foodType"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantityLeft"));
        expirationColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));

        // Set up search
        searchField.textProperty().addListener((obs, oldVal, newVal) ->
                foodTable.setItems(filterFoods(newVal))
        );

        loadUserData();
    }

    /**
     * Filters foods by name (case-insensitive partial match)
     */
    private ObservableList<FoodItem> filterFoods(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            return fullFoodList;
        }
        String lowercaseQuery = searchText.toLowerCase();
        return fullFoodList.filtered(item ->
                item.getName().toLowerCase().contains(lowercaseQuery)
        );
    }

    private void loadUserData() {
        String apiUrl = basePath + "/foods";
        try {
            FoodItem[] foodItems = restTemplate.getForObject(apiUrl, FoodItem[].class);
            if (foodItems != null) {
                fullFoodList.setAll(Arrays.asList(foodItems));
                foodTable.setItems(fullFoodList);
                checkExpirations(fullFoodList);
            }
        } catch (ResourceAccessException e) {
            System.err.println("Server connection error: " + e.getMessage());
        }
    }

    private void checkExpirations(List<FoodItem> foodList) {
        StringBuilder upcoming = new StringBuilder();
        StringBuilder expired = new StringBuilder();

        for (FoodItem item : foodList) {
            long daysUntilExpiry = (item.getExpirationDate().getTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24);

            if (daysUntilExpiry < 0) {
                expired.append(item.getName()).append("\n");
            } else if (daysUntilExpiry <= 3) {
                upcoming.append(item.getName()).append(" (").append(daysUntilExpiry).append(" days)\n");
            }
        }

        if (expired.length() > 0 || upcoming.length() > 0) {
            showExpirationAlert(
                    (expired.length() > 0 ? "❗ Expired:\n" + expired : "") +
                            (upcoming.length() > 0 ? "\n⚠️ Expiring soon:\n" + upcoming : "")
            );
        }
    }

    private void showExpirationAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Expiration Alert");
            alert.setHeaderText("Check your items!");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    @FXML
    private void handleAdd() {
        FoodItem newItem = new FoodItem("New Food", "Snack", 1.0f, new Date());
        FoodItem savedItem = restTemplate.postForObject(basePath + "/foods", newItem, FoodItem.class);
        if (savedItem != null) {
            fullFoodList.add(savedItem);
        }
    }

    @FXML
    private void handleRemove() {
        FoodItem selected = foodTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            restTemplate.delete(basePath + "/foods/" + selected.getId());
            fullFoodList.remove(selected);
        }
    }
}
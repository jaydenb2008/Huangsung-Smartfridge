package edu.sdccd.cisc191.jfxclient.controllers;

import edu.sdccd.cisc191.common.model.FoodItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.converter.BooleanStringConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.LocalDateStringConverter;


import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
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
    private TableColumn<FoodItem, LocalDate> expirationColumn;
    @FXML
    private TableColumn<FoodItem, Boolean> openedColumn;
    @FXML
    private TextField searchField;
    private final ObservableList<FoodItem> fullFoodList = FXCollections.observableArrayList();

    private final RestTemplate restTemplate;
    @Value("${spring.data.rest.base-path}")
    private String basePath;
    private String apiUrl;
    public FoodController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        apiUrl = basePath + "/api/food/foods";

        foodTable.setEditable(true);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        foodTypeColumn.setCellValueFactory(new PropertyValueFactory<>("foodType"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantityLeft"));
        expirationColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
        openedColumn.setCellValueFactory(new PropertyValueFactory<>("opened"));

        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        foodTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        expirationColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        openedColumn.setCellFactory(TextFieldTableCell.forTableColumn(new BooleanStringConverter()));

        nameColumn.setOnEditCommit(event -> {
            FoodItem item = event.getRowValue();
            item.setName(event.getNewValue());
            restTemplate.put(apiUrl + "/" + item.getId(), item);
        });

        foodTypeColumn.setOnEditCommit(event -> {
            FoodItem item = event.getRowValue();
            item.setFoodType(event.getNewValue());
            restTemplate.put(apiUrl + "/" + item.getId(), item);
        });

        quantityColumn.setOnEditCommit(event -> {
            FoodItem item = event.getRowValue();
            item.setQuantityLeft(event.getNewValue());
            restTemplate.put(apiUrl + "/" + item.getId(), item);
        });

        expirationColumn.setOnEditCommit(event -> {
            FoodItem item = event.getRowValue();
            item.setExpirationDate(event.getNewValue());
            restTemplate.put(apiUrl + "/" + item.getId(), item);
        });

        openedColumn.setOnEditCommit(event -> {
            FoodItem item = event.getRowValue();
            item.setOpened(event.getNewValue());
            restTemplate.put(apiUrl + "/"+ item.getId(), item);
        });


        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterList(newValue));

        loadFoodData();
    }

    //Search for items using Stream API
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
        try {
            FoodItem[] foodItems = restTemplate.getForObject(apiUrl, FoodItem[].class);
            if (foodItems != null) {
                List<FoodItem> foodList = Arrays.asList(foodItems);
                fullFoodList.setAll(foodList); // ensures filtering works
                foodTable.setItems(fullFoodList);
            } else {
                // Handle the case where the API request fails or returns null
                System.err.println("Failed to load food data from the API.");
            }
        } catch (ResourceAccessException e) {
            System.err.println("Could not reach server: " + e.getMessage());
        }
    }


    @FXML
    private void handleAdd() {
        FoodItem newItem = new FoodItem( "New Food", "FoodItem", 1.0f, FoodItem.convertToDate("06-01-2026"), false);
        FoodItem savedItem = restTemplate.postForObject(apiUrl, newItem, FoodItem.class);
        if (savedItem != null) {
            fullFoodList.add(savedItem);
            foodTable.setItems(fullFoodList);
        }
    }

    @FXML
    private void handleRemove() {
        FoodItem selected = foodTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            restTemplate.delete(apiUrl + "/" + selected.getId());
            fullFoodList.remove(selected);
            foodTable.setItems(fullFoodList);
        }
    }

    @FXML
    private void handleUpdate() {
        FoodItem selected = foodTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            restTemplate.put(apiUrl + "/" + selected.getId(), selected);
            foodTable.refresh();
        }
    }

}
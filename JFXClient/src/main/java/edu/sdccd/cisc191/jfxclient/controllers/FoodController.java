package edu.sdccd.cisc191.jfxclient.controllers;

import edu.sdccd.cisc191.common.model.FoodItem;
import edu.sdccd.cisc191.jfxclient.controllers.FoodService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.CheckBoxTableCell;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;


public class FoodController {

    @FXML private TableView<FoodItem> tableView;
    @FXML private TableColumn<FoodItem, Long> idColumn;
    @FXML private TableColumn<FoodItem, String> nameColumn;
    @FXML private TableColumn<FoodItem, String> foodTypeColumn;
    @FXML private TableColumn<FoodItem, Double> quantityLeftColumn;
    @FXML private TableColumn<FoodItem, Date> expirationDateColumn;
    @FXML private TableColumn<FoodItem, Boolean> openedColumn;

    @FXML private TextField nameField;
    @FXML private TextField foodTypeField;
    @FXML private TextField quantityLeftField;
    @FXML private DatePicker expirationDatePicker;
    @FXML private CheckBox openedCheckBox;

    private final FoodService foodService = new FoodService();
    private final ObservableList<FoodItem> foodItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        foodTypeColumn.setCellValueFactory(new PropertyValueFactory<>("foodType"));
        quantityLeftColumn.setCellValueFactory(new PropertyValueFactory<>("quantityLeft"));
        expirationDateColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
        openedColumn.setCellValueFactory(new PropertyValueFactory<>("opened"));
        openedColumn.setCellFactory(tc -> new TableCell<FoodItem, Boolean>() {
            private final CheckBox checkBox = new CheckBox();

            @Override
            protected void updateItem(Boolean opened, boolean empty) {
                super.updateItem(opened, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(Boolean.TRUE.equals(opened));

                    checkBox.setDisable(true);
                    setGraphic(checkBox);
                }

            }
        });

        expirationDateColumn.setCellFactory(column -> new TableCell<FoodItem, Date>() {
            private final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        refreshTable();
    }

    @FXML
    public void addFoodItem(ActionEvent event) {
        String name = nameField.getText().trim();
        String foodType = foodTypeField.getText().trim();
        String qtyStr = quantityLeftField.getText().trim();
        LocalDate expiration = expirationDatePicker.getValue();

        if (name.isEmpty() || foodType.isEmpty() || qtyStr.isEmpty() || expiration == null) {
            showAlert("Error", "All fields are required.");
            return;
        }
        double quantity;
        try {
            quantity = Double.parseDouble(qtyStr);
        } catch (NumberFormatException ex) {
            showAlert("Error", "Quantity must be a valid number.");
            return;
        }

        Boolean opened = openedCheckBox.isSelected();
        FoodItem foodItem = new FoodItem(null, name, foodType, quantity, expiration, opened);

        // Using background thread to avoid UI freeze
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                foodService.addFoodItem(foodItem);
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            refreshTable();
            nameField.clear();
            foodTypeField.clear();
            quantityLeftField.clear();
            expirationDatePicker.setValue(null);
            openedCheckBox.setSelected(false);
        });
        task.setOnFailed(e -> showAlert("Error", "Failed to add food item: " + task.getException().getMessage()));
        new Thread(task).start();
    }


    @FXML
    private void handleClearSearch(ActionEvent event) {
        // TODO: clear your search field, reset your table, etc.
        // For now, you can leave it empty or put a System.out.println
        System.out.println("Clear Search clicked!");
    }

    @FXML
    public void handleAdd(ActionEvent event) {
        String name = nameField.getText().trim();
        String foodType = foodTypeField.getText().trim();
        String qtyStr = quantityLeftField.getText().trim();
        LocalDate pickedDate = expirationDatePicker.getValue();
        if (pickedDate == null) {
            showAlert("Error", "Expiration date is required.");
            return;
        }
        Date expiration = java.sql.Date.valueOf(pickedDate);


        if (name.isEmpty() || foodType.isEmpty() || qtyStr.isEmpty() || expiration == null) {
            showAlert("Error", "All fields are required.");
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(qtyStr);
        } catch (NumberFormatException ex) {
            showAlert("Error", "Quantity must be a valid number.");
            return;
        }

        // For now, opened can be left as null or false for regular FoodItem.
        FoodItem foodItem = new FoodItem(null, name, foodType, (float)quantity, expiration);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                foodService.addFoodItem(foodItem);
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            refreshTable();
            nameField.clear();
            foodTypeField.clear();
            quantityLeftField.clear();
            expirationDatePicker.setValue(null);
        });
        task.setOnFailed(e -> showAlert("Error", "Failed to add food item: " + task.getException().getMessage()));
        new Thread(task).start();
    }

    @FXML
    public void handleTableClick() {
        FoodItem selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            nameField.setText(selected.getName());
            foodTypeField.setText(selected.getFoodType());
            quantityLeftField.setText(String.valueOf(selected.getQuantityLeft()));
            expirationDatePicker.setValue(
                    selected.getExpirationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            );
        }
    }


    @FXML
    public void handleUpdate(ActionEvent event) {
        FoodItem selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Select an item to update.");
            return;
        }

        String name = nameField.getText().trim();
        String foodType = foodTypeField.getText().trim();
        String qtyStr = quantityLeftField.getText().trim();
        Date expiration = java.sql.Date.valueOf(expirationDatePicker.getValue());

        if (name.isEmpty() || foodType.isEmpty() || qtyStr.isEmpty() || expiration == null) {
            showAlert("Error", "All fields are required.");
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(qtyStr);
        } catch (NumberFormatException ex) {
            showAlert("Error", "Quantity must be a valid number.");
            return;
        }

        FoodItem foodItem = new FoodItem(selected.getId(), name, foodType, (float)quantity, expiration);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                foodService.updateFoodItem(foodItem);
                return null;
            }
        };
        task.setOnSucceeded(e -> {
            refreshTable();
            nameField.clear();
            foodTypeField.clear();
            quantityLeftField.clear();
            expirationDatePicker.setValue(null);
        });
        task.setOnFailed(e -> showAlert("Error", "Failed to update food item: " + task.getException().getMessage()));
        new Thread(task).start();
    }





    @FXML
    private void handleRemove(javafx.event.ActionEvent event) {
        // Your add logic here
        System.out.println("Add button clicked!");
        // (You can call your actual addFoodItem code here.)
    }


    @FXML
    public void removeFoodItem(ActionEvent event) {
        FoodItem selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Select an item to remove.");
            return;
        }
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                foodService.removeFoodItem(selected.getId());
                return null;
            }
        };
        task.setOnSucceeded(e -> refreshTable());
        task.setOnFailed(e -> showAlert("Error", "Failed to remove food item: " + task.getException().getMessage()));
        new Thread(task).start();
    }

    public void refreshTable() {
        Task<List<FoodItem>> task = new Task<>() {
            @Override
            protected List<FoodItem> call() throws Exception {
                List<FoodItem> items = foodService.getAllFoodItems();
                System.out.println("Fetched from API: " + items);
                for (FoodItem item : items) {
                    System.out.println("Row: id=" + item.getId()
                            + ", name=" + item.getName()
                            + ", type=" + item.getClass().getSimpleName()
                            + ", opened=" + item.getOpened());
                }
                return items;
            }
        };
        task.setOnSucceeded(e -> {
            foodItems.setAll(task.getValue());
            tableView.setItems(foodItems);
        });
        task.setOnFailed(e -> {
            Throwable ex = task.getException();
            ex.printStackTrace();
            showAlert("Error", "Could not load food items: " + ex.getMessage());
        });
        new Thread(task).start();
    }


    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

package edu.sdccd.cisc191.jfxclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.net.URL;

public class JFXClient extends Application {
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 700;
    private Stage window;

    @Override
    public void start(Stage primaryStage) {
        createStartScreen(primaryStage);
    }

    public void createStartScreen(Stage primaryStage) {
        this.window = primaryStage;
        window.setTitle("Huangsung Smart-Fridge");
        window.setWidth(WINDOW_WIDTH);
        window.setHeight(WINDOW_HEIGHT);
        window.setResizable(false);

        Pane root = new Pane();
        root.setStyle("-fx-background-color: black;");

        Text floatingText = new Text(80, 200, "Lehuang Smart Fridge");
        floatingText.setFont(Font.font("Times New Roman", 50));
        floatingText.setFill(Color.DARKRED);

        Button startButton = new Button("Start");
        startButton.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 24px; -fx-background-color: white;");
        startButton.setLayoutX((WINDOW_WIDTH - 150) / 2.0);
        startButton.setLayoutY(400);

        startButton.setOnAction(e -> loadMainFXML());

        root.getChildren().addAll(floatingText, startButton);
        window.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        window.show();
    }

    private void loadMainFXML() {
        try {
            URL fxmlUrl = getClass().getResource("/fxml/foods.fxml");
            if (fxmlUrl == null) {
                throw new FileNotFoundException("FXML file not found at /fxml/foods.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Scene foodsScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
            window.setScene(foodsScene);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Loading Error");
            alert.setHeaderText("Failed to load foods screen");
            alert.setContentText("Error: " + e.getMessage());
            alert.showAndWait();

            createFallbackUI();
        }
    }

    private void createFallbackUI() {
        VBox fallbackRoot = new VBox(20);
        fallbackRoot.setAlignment(Pos.CENTER);
        fallbackRoot.setPadding(new Insets(20));

        Label errorLabel = new Label("Could not load food items.");
        errorLabel.setStyle("-fx-font-size: 16px;");

        Button retryButton = new Button("Retry");
        retryButton.setOnAction(e -> loadMainFXML());

        Button backButton = new Button("Back to Start");
        backButton.setOnAction(e -> createStartScreen(window));

        fallbackRoot.getChildren().addAll(errorLabel, retryButton, backButton);
        window.setScene(new Scene(fallbackRoot, WINDOW_WIDTH, WINDOW_HEIGHT));
    }

    public static void main(String[] args) {
        launch(args);
    }
}

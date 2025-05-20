package edu.sdccd.cisc191.jfxclient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

/**
 * This class defines the javafx application and loads to FXML to be used
 */

@SpringBootApplication
public class JFXClient extends Application {
    private ConfigurableApplicationContext applicationContext;
    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(JFXClient.class).run();
    }
    @Override
    public void start(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/foods.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Food Data");
        stage.setScene(scene);
        stage.show();

        applicationContext.publishEvent(new StageReadyEvent(stage));
    }
    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }
    static class StageReadyEvent extends ApplicationEvent {
        public StageReadyEvent(Stage stage) {
            super(stage);
        }
    }
}

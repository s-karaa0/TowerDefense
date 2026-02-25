package org.example.towerdefense;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    /*@Override

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }*/

    @Override
    public void start(Stage stage) {

        HelloController controller = new HelloController();

        Scene scene = new Scene(controller.getRoot(), 1000, 800);
        stage.setScene(scene);
        stage.setTitle("Tower Defense Game");
        stage.show();

        controller.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package org.example.towerdefense;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartController {

    public void onStartButtonClicked(ActionEvent actionEvent){
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        HelloController controller = new HelloController();

        Scene scene = new Scene(controller.getRoot(), 1000, 800);
        stage.setScene(scene);
        stage.setTitle("Tower Defense Game");
        stage.show();
        controller.start();
    }

    public void onSettingsButtonClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        SettingsController controller = new SettingsController();

        Scene scene = new Scene(controller.getRoot(), 1000, 800);
        stage.setScene(scene);
        stage.setTitle("Tower Defense Game");
        stage.show();

        controller.start();
    }
}

package org.example.towerdefense;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SettingsController {

    private final AnchorPane root = new AnchorPane();
    public Button closeButton;

    public Parent getRoot() {
        return root;
    }

    public void start() {
        start();
    }
    public void onMusicButtonClicked(ActionEvent actionEvent) {
    }

    public void onSaveClicked(ActionEvent actionEvent) {
    }

    public void onContinueClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        HelloController controller = new HelloController();
        Scene scene = new Scene(controller.getRoot(), 1000, 800);
        stage.setScene(scene);
        stage.setTitle("Tower Defense Game");
        stage.show();
        controller.start();
    }

    public void onExitClicked(ActionEvent actionEvent) {
        Stage stage=(Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}

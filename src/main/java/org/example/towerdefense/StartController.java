package org.example.towerdefense;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class StartController {
    @FXML
    public ChoiceBox<String> fieldChoiceBox;
    GameApplication gameApp;

    public void setApplication(GameApplication gameApp) {
        this.gameApp = gameApp;
    }

    public void onStartButtonClicked(ActionEvent actionEvent) throws IOException {
        String difficulty = fieldChoiceBox.getValue();
        gameApp.startGameWithDifficulty(difficulty);
    }

    public void onSettingsButtonClicked(ActionEvent actionEvent) throws IOException {
        gameApp.switchScene("/settings-view.fxml", "settings");
    }

    @FXML
    public void initialize() {
        fieldChoiceBox.getItems().addAll("easy", "normal", "hard");
        fieldChoiceBox.setValue("easy");
    }

}

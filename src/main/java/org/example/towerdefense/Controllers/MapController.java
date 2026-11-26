package org.example.towerdefense.Controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.towerdefense.GameApplication;

import java.io.IOException;

public class MapController {
    public Button startButton;
    private GameApplication gameApp;
    private String difficulty;


    public void setApplication(GameApplication gameApp) {
        this.gameApp = gameApp;
    }

    public void mediumSelected(MouseEvent mouseEvent) {
        difficulty="normal";
    }

    public void easySelected(MouseEvent mouseEvent) {
        difficulty="easy";
    }

    public void hardSelected(MouseEvent mouseEvent) {
        difficulty="hard";
    }

    public void start(ActionEvent actionEvent) throws IOException {
        gameApp.setDifficulty(difficulty);
        gameApp.switchScene("game", "game");
        closeWindow();
    }
    private void closeWindow() {
        Stage stage = (Stage) startButton.getScene().getWindow();
        stage.close();
    }
}

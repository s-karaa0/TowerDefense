package org.example.towerdefense;

import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class StartController {


    GameApplication gameApp;
    public void setApplication(GameApplication gameApp) {
        this.gameApp = gameApp;
    }

    public void onStartButtonClicked(ActionEvent actionEvent) throws IOException {
        gameApp.switchScene("game", "game");
    }

    public void onSettingsButtonClicked(ActionEvent actionEvent) throws IOException {
        gameApp.switchScene("/settings-view.fxml", "settings");
    }
}

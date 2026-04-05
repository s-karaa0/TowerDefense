package org.example.towerdefense;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {

    private final AnchorPane root = new AnchorPane();
    public Button skipButton;
    private GameApplication gameApp;
    @FXML
    public Button closeButton;
    @FXML
    public Button continueButton;

    private static boolean musicOn = true;

    public void setApplication(GameApplication gameApp) {
        this.gameApp = gameApp;
        if(gameApp != null && gameApp.isStartState()) {
            continueButton.setDisable(true);
            continueButton.setVisible(false);
        } else {
            continueButton.setDisable(false);
            continueButton.setVisible(true);
        }
    }

    public void onMusicButtonClicked(ActionEvent actionEvent) {
        if (musicOn) {
            MusicManager.stop();
            ((Button) actionEvent.getSource()).setText("Musik: Off");
            musicOn = false;
        } else {
            // Musik wieder starten mit aktuellem Context
            MusicManager.resumeContext();
            ((Button) actionEvent.getSource()).setText("Musik: On");
            musicOn = true;
        }
    }

    public void onSaveClicked(ActionEvent actionEvent) {
    }

    public void onContinueClicked(ActionEvent actionEvent) throws IOException {
        gameApp.switchScene("game", "game");
        gameApp.resumeGame();
        Stage stage = (Stage) continueButton.getScene().getWindow();
        stage.close();

    }

    public void onExitClicked(ActionEvent actionEvent) throws IOException {
        gameApp.switchScene("/start-game-view.fxml", "start");
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();

    }

    public void onSkipClicked(ActionEvent actionEvent) {
        MusicManager.skipTrack();
    }
}

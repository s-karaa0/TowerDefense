package org.example.towerdefense;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {

    private GameApplication gameApp;

    @FXML
    private Button closeButton;
    @FXML
    private Button continueButton;
    @FXML
    private Button musicButton;

    public void setApplication(GameApplication gameApp) {
        this.gameApp = gameApp;
        boolean inStartMenu = gameApp != null && gameApp.isStartState();
        if (inStartMenu) {
            continueButton.setDisable(true);
            continueButton.setVisible(false);
            closeButton.setText("ZURÜCK ZUM MENÜ");
        } else {
            continueButton.setDisable(false);
            continueButton.setVisible(true);
            closeButton.setText("ZUM HAUPTMENÜ");
        }
        updateMusicButton();
    }

    public void onMusicButtonClicked(ActionEvent actionEvent) {
        MusicManager.setMusicEnabled(!MusicManager.isMusicEnabled());
        updateMusicButton();
    }

    public void onContinueClicked(ActionEvent actionEvent) throws IOException {
        gameApp.switchScene("game", "game");
        gameApp.resumeGame();
        closeSettingsWindow();
    }

    public void onExitClicked(ActionEvent actionEvent) throws IOException {
        if (gameApp == null) {
            throw new IllegalStateException("GameApplication is not set");
        }
        if (!gameApp.isStartState()) {
            gameApp.switchScene("/start-game-view.fxml", "start");
        }
        closeSettingsWindow();
    }

    public void onSkipClicked(ActionEvent actionEvent) {
        MusicManager.skipTrack();
    }

    private void updateMusicButton() {
        musicButton.setText(MusicManager.isMusicEnabled() ? "MUSIK: AN" : "MUSIK: AUS");
    }

    private void closeSettingsWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}

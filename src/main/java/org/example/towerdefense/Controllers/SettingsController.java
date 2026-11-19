package org.example.towerdefense.Controllers;

import javafx.event.ActionEvent;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.towerdefense.GameApplication;
import org.example.towerdefense.MusicManager;
import org.example.towerdefense.SaveState;
import org.example.towerdefense.Wave.WaveManager;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SettingsController {

    public Button saveButton;
    private GameApplication gameApp;

    @FXML
    private Button closeButton;
    @FXML
    private Button continueButton;
    @FXML
    private Button musicButton;
    @FXML
    private Label currentTrackLabel;

    SaveState state = SaveState.getInstance();

    public void setApplication(GameApplication gameApp) {
        this.gameApp = gameApp;
        currentTrackLabel.textProperty().bind(
                Bindings.concat("Die Barden spielen: ", MusicManager.currentTrackTitleProperty()));
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

    public void onSaveClicked(ActionEvent actionEvent) throws IOException {
        state.saveStateOfGame(WaveManager.getInstance(), GameController.getInstance());
    }

    public void onLoadClick(ActionEvent actionEvent) throws IOException {
        SaveState.getInstance().loadState(WaveManager.getInstance(),GameController.getInstance());

        // switch back to the game scene after loading

        //@ToDO Error Meldung, wenn savestate.txt leer ist
        gameApp.switchScene("game", "game");
        gameApp.resumeGame();
        closeSettingsWindow();
    }
}

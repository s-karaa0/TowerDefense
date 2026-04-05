package org.example.towerdefense;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {

    private GameApplication gameApp;

    @FXML public Button closeButton;
    @FXML public Button continueButton;
    @FXML public Button soundEffectsButton; // Muss in der FXML fx:id="soundEffectsButton" heißen!

    public void setApplication(GameApplication gameApp) {
        this.gameApp = gameApp;
        if (gameApp != null && gameApp.isStartState()) {
            continueButton.setDisable(true);
            continueButton.setVisible(false);
        } else {
            continueButton.setDisable(false);
            continueButton.setVisible(true);
        }


        updateSoundButtonText();
    }

    @FXML
    public void onSoundEffectsClicked(ActionEvent actionEvent) {
        if (gameApp != null && gameApp.getGame() != null) {
            Game game = gameApp.getGame();

            boolean currentState = game.isSoundEffectsEnabled();
            game.setSoundEffectsEnabled(!currentState);


            updateSoundButtonText();
        }
    }

    private void updateSoundButtonText() {
        if (soundEffectsButton != null && gameApp != null && gameApp.getGame() != null) {
            boolean isEnabled = gameApp.getGame().isSoundEffectsEnabled();
            soundEffectsButton.setText(isEnabled ? "Schuss-Sound: An" : "Schuss-Sound: Aus");
        }
    }

    public void onMusicButtonClicked(ActionEvent actionEvent) {
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
}
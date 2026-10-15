package org.example.towerdefense;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class StartController {
    @FXML
    public ChoiceBox<String> fieldChoiceBox;
    public String difficulty;
    public GameApplication gameApp;

    @FXML
    private void onCloseButtonClicked(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(15), e -> {
                    double currentOpacity = stage.getOpacity();
                    if (currentOpacity > 0.05) {
                        stage.setOpacity(currentOpacity - 0.05);
                    } else {
                        stage.setOpacity(0.0);
                    }
                })
        );

        timeline.setCycleCount(20); // 20 Durchläufe (20 * 15ms = 300ms)

        // Wenn die Animation fertig ist, das Fenster komplett schließen
        timeline.setOnFinished(e -> stage.close());

        timeline.play();
    }

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    public void getApplication(GameApplication gameApp) {
        this.gameApp = gameApp;
    }

    public void initialize() {
        fieldChoiceBox.getItems().addAll("easy", "normal", "hard");
        fieldChoiceBox.setValue("easy");
    }

    public void choiceBoxClicked(ActionEvent actionEvent) {
        if (gameApp != null) {
            difficulty = fieldChoiceBox.getValue();
            gameApp.setDifficulty(difficulty);
        }
    }

    public void onStartButtonClicked(ActionEvent actionEvent) throws IOException {
        difficulty = fieldChoiceBox.getValue();
        gameApp.setDifficulty(difficulty);

        System.out.println(difficulty);
        gameApp.switchScene("game", "game");
    }

    public void onSettingsButtonClicked(ActionEvent actionEvent) throws IOException {
        gameApp.switchScene("/settings-view.fxml", "settings");
    }

}

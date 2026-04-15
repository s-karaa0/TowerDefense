package org.example.towerdefense;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ConclusionController {
    private GameApplication gameApp;
    private boolean conclusion;
    private String difficulty;

    @FXML
    private Button homeButton;
    @FXML
    private Button restartButton;
    @FXML
    private Label win;
    @FXML
    private Label lose;

    public void setApplication(GameApplication gameApp) {
        this.gameApp = gameApp;
        conclusion = gameApp.getConclusion();
        if (conclusion) {
            win.setVisible(true);
            lose.setVisible(false);
        } else if (!conclusion) {
            win.setVisible(false);
            lose.setVisible(true);
        }
    }

    public void onExitClicked(ActionEvent actionEvent) throws IOException {
        gameApp.switchScene("/start-game-view.fxml", "start");
        Stage stage = (Stage) homeButton.getScene().getWindow();
        stage.close();
    }

    // not in use
    public void onRestartClicked(ActionEvent actionEvent) throws IOException {
        difficulty = gameApp.getDifficulty();
        gameApp.setDifficulty(difficulty);
        gameApp.switchScene("game", "game");
        gameApp.resumeGame();
        Stage stage = (Stage) homeButton.getScene().getWindow();
        stage.close();
    }

    public void getConclusion(boolean conclusion){
        this.conclusion = conclusion;
    }

}

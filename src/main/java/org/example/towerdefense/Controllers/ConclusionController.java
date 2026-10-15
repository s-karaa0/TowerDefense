package org.example.towerdefense.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.towerdefense.GameApplication;

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
    private AnchorPane resultRoot;
    @FXML
    private Label win;
    @FXML
    private Label lose;
    @FXML
    private Label resultMessage;

    public void setApplication(GameApplication gameApp) {
        this.gameApp = gameApp;
        conclusion = gameApp.getConclusion();
        if (conclusion) {
            resultRoot.getStyleClass().add("result-win");
            win.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 56px; -fx-font-weight: bold; "
                    + "-fx-text-fill: #fff3c4; "
                    + "-fx-effect: dropshadow(gaussian, rgba(218, 174, 77, 0.9), 18, 0.45, 0, 0);");
            win.setVisible(true);
            lose.setVisible(false);
            resultMessage.setText("Der Feind zieht sich in Schande zurück. Das Reich ist sicher – für den Moment.");
        } else if (!conclusion) {
            resultRoot.getStyleClass().add("result-lose");
            lose.setStyle("-fx-font-family: 'Georgia'; -fx-font-size: 42px; -fx-font-weight: bold; "
                    + "-fx-text-fill: #fff0e8; "
                    + "-fx-effect: dropshadow(gaussian, rgba(147, 51, 38, 0.9), 16, 0.4, 0, 2);");
            win.setVisible(false);
            lose.setVisible(true);
            resultMessage.setText("Sie haben niemanden verschont. Das Reich ist komplett ausgelöscht und die Schuld daran trägst allein du. Du hast versagt.");
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

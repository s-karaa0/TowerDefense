package org.example.towerdefense.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import org.example.towerdefense.GameApplication;

import java.io.IOException;

public class StartController {
    /*nach einfügen der Map funktion sind die meisten dieser Funktionen obsolit geworden
    ich habe mich entschieden den alten Code einfach auszukommentieren
    ! Wichtig: Einige Dinge müssen hier noch erledigt werden bevor es fertig ist
    namentlich muss die Choice Box noch entfernt werden und die fxml angepasst werden
    ebenso ist die mapsView zu designtechnisch zu verbesseren dies ist aber nicht meine
    Aufgabe mleimhof
     */


    /*@FXML
    public ChoiceBox<String> fieldChoiceBox;
    public String difficulty;*/
    public GameApplication gameApp;

    /*public enum Difficulty {
        EASY, MEDIUM, HARD
    }*/

    public void getApplication(GameApplication gameApp) {
        this.gameApp = gameApp;
    }

    /*public void initialize() {
        fieldChoiceBox.getItems().addAll("easy", "normal", "hard");
        fieldChoiceBox.setValue("easy");
    }*/

    public void choiceBoxClicked(ActionEvent actionEvent) {
       /* if (gameApp != null) {
            difficulty = fieldChoiceBox.getValue();
            gameApp.setDifficulty(difficulty);
        }*/
    }

    public void onStartButtonClicked(ActionEvent actionEvent) throws IOException {
        //difficulty = fieldChoiceBox.getValue();
        //gameApp.setDifficulty(difficulty);
        gameApp.switchScene("/maps-view.fxml", "map");
        //System.out.println(difficulty);
        //gameApp.switchScene("game", "game");
    }

    public void onSettingsButtonClicked(ActionEvent actionEvent) throws IOException {
        gameApp.switchScene("/settings-view.fxml", "settings");
    }

}

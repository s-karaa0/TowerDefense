package org.example.towerdefense;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GameApplication extends Application {
    private static Stage stage;
    private static boolean isRunning = false;
    private static SceneState currentState = SceneState.start;
    private static GameController gameController;

    public enum SceneState {
        start,
        game,
        conclusion
    }

    public SceneState getCurrentState() {
        return currentState;
    }

    public boolean isStartState() {
        return currentState == SceneState.start;
    }

    public void start(Stage primaryStage) throws IOException {

        System.out.println("-----------------");
        stage = primaryStage;
        switchScene("/start-game-view.fxml", "start");

        stage.setTitle("Tower Defense");
        stage.show();
    }

    public void switchScene(String fxml, String layout) throws IOException {
        boolean x = gameController != null;
        try {
        switch (layout) {
            case "settings": {
                Stage settingsStage = new Stage();
                settingsStage.initOwner(stage);
                settingsStage.initModality(Modality.WINDOW_MODAL);

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
                Parent root = fxmlLoader.load();

                Scene settingsScene = new Scene(root);
                settingsStage.setScene(settingsScene);
                SettingsController settingsController = fxmlLoader.getController();
                settingsController.setApplication(this);

                settingsStage.setTitle("Settings");
                settingsStage.centerOnScreen();
                settingsStage.show();

                break;
            }

            case "start": {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
                Parent root = fxmlLoader.load();

                StartController startController = fxmlLoader.getController();
                startController.setApplication(this);

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.centerOnScreen();
                isRunning = false;

                currentState = SceneState.start;
                break;
            }

            case "game": {
                if (!isRunning) {
                    isRunning = true;
                    GameController controller = new GameController();
                    Scene scene = new Scene(controller.getRoot(), 1000, 800);
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.setTitle("Tower Defense Game");
                    controller.startLoop();
                    setGameController(controller);
                }

                currentState = SceneState.game;
                break;
            }

            case "conclusion": {
                Stage conclusionStage = new Stage();
                conclusionStage.initOwner(stage);
                conclusionStage.initModality(Modality.WINDOW_MODAL);


                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
                Parent root = fxmlLoader.load();

                Scene conclusionScene = new Scene(root);
                conclusionStage.setScene(conclusionScene);
                ConclusionController conclusionController = fxmlLoader.getController();
                conclusionController.setApplication(this);
                conclusionStage.centerOnScreen();
                conclusionStage.show();
                isRunning = false;

                currentState = SceneState.conclusion;
                break;
            }

        }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(currentState);
    }

    public void setGameController(GameController gameCtr){this.gameController = gameCtr;}

    public void startGame(){gameController.startLoop();}
    public void stopGame(){gameController.stopLoop();}
    public void restartGame(){gameController.restartLoop();}
    public void resumeGame(){
        if(gameController != null){
            gameController.resumeLoop();
        }else {
            System.out.println("GameController is nicht definiert");
        }
    }
}

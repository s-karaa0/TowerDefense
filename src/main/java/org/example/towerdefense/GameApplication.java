package org.example.towerdefense;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameApplication extends Application {
    private static Stage stage;
    private static boolean isRunning = false;
    private static SceneState currentState = SceneState.start;
    private static GameController gameController;
    private String difficulty = "easy";
    private boolean conclusion;

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
        try {
            switch (layout) {
                case "settings": {
                    System.out.println(difficulty);
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
                    startController.getApplication(this);

                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    isRunning = false;

                    currentState = SceneState.start;
                    MusicManager.playQueue(new ArrayList<>(List.of(
                            "/music/menu2.mp3",
                            "/music/menu.mp3",
                            "/music/menu3.mp3"
                    )), "menu");
                    break;
                }

                case "game": {
                    if (!isRunning) {
                        isRunning = true;
                        GameController controller = new GameController(difficulty);
                        Pane root = controller.getRoot();

                        Image bgImage = null;


                        // why not difficulty.equals()? because if null throws nullpointerexception
                        if ("easy".equals(difficulty)) {
                            bgImage = new Image(getClass().getResource("/images/fields/field-easy.png").toExternalForm());
                            MusicManager.playQueue(new ArrayList<>(List.of(
                                    "/music/game-easy2.mp3",
                                    "/music/game-easy3.mp3",
                                    "/music/game-easy4.mp3"
                            )), "easy");
                        } else if ("normal".equals(difficulty)) {
                            bgImage = new Image(getClass().getResource("/images/fields/field-normal.png").toExternalForm());
                            MusicManager.playQueue(new ArrayList<>(List.of(
                                    "/music/game-normal.mp3",
                                    "/music/game-normal2.mp3",
                                    "/music/game-normal3.mp3"
                            )), "normal");
                        } else if ("hard".equals(difficulty)) {
                            bgImage = new Image(getClass().getResource("/images/fields/field-hard.png").toExternalForm());
                            MusicManager.playQueue(new ArrayList<>(List.of(
                                    "/music/game-hard.mp3",
                                    "/music/game-hard2.mp3",
                                    "/music/game-hard3.mp3"
                            )), "hard");
                        }



                        /*ImageView bg = new ImageView(bgImage);
                        bg.setFitWidth(1000);
                        bg.setFitHeight(800);
                         */

                        controller.getBackgroundImage(bgImage);

                        Scene scene = new Scene(root, 1000, 800);
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

                    System.out.println(difficulty);

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

                    conclusionController.getConclusion(conclusion);

                    currentState = SceneState.conclusion;
                    break;
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void setGameController(GameController gameCtr) {
        this.gameController = gameCtr;
    }

    public void startGame() {
        gameController.startLoop();
    }

    public void stopGame() {
        gameController.stopLoop();
    }

    public void restartGame() {
        gameController.restartLoop();
    }

    public void resumeGame() {
        if (gameController != null) {
            gameController.resumeLoop();
        } else {
            System.out.println("GameController is nicht definiert");
        }
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
        System.out.println(difficulty);
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setConclusion(boolean conclusion) {
        this.conclusion = conclusion;
    }

    public boolean getConclusion() {
        return conclusion;
    }

}

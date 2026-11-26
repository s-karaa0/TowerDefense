package org.example.towerdefense;

import javafx.geometry.Point2D;
import org.example.towerdefense.Controllers.GameController;
import org.example.towerdefense.Tower.Tower;
import org.example.towerdefense.Wave.WaveManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SaveState implements Serializable {
    // used to identify if the serialized object and the
    // loaded class are compatible.
    private static final long serialVersionUID = 1L;
    private static SaveState saveState;

    private SaveState() {}

    public static SaveState getInstance() {
        if (saveState == null) {
            saveState = new SaveState();
        }
        return saveState;
    }

    public void saveStateOfGame(WaveManager waveManager, GameController gameController) throws IOException {
        File directory = new File("./src/main/resources/savefile");
        File file = new File(directory, "savestate.txt");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();
        }

        List<SavedTowerData> savedTowers = createSavedTowers(gameController);

        // Serializing
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(gameController.getDifficulty());

            objectOutputStream.writeInt(waveManager.getGroup());

            objectOutputStream.writeInt(waveManager.getGame().getHp());
            objectOutputStream.writeInt(waveManager.getGame().getMoney());

            // after saving all of these, the range has to be defined in gamecontroller for the radius while hovering on the towers
            // objectOutputStream.writeInt()

            // TowerData as a list
            objectOutputStream.writeObject(savedTowers);

            objectOutputStream.flush();
            objectOutputStream.close();
            System.out.println("Save state was successfully saved!");

        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error when loading save state " + e.getMessage());
        }
    }

    public void loadState(WaveManager waveManager, GameController gameController) throws IOException {

        File directory = new File("./src/main/resources/savefile");
        File file = new File(directory, "savestate.txt");

        if (!directory.exists()) {
            directory.mkdirs();
        }

        if (!file.exists()) {
            file.createNewFile();
        }

        // Deserialization
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fileInputStream);
            String currentSavedDiff = in.readObject().toString();

            if(!currentSavedDiff.equals(gameController.getDifficulty())){

                in.close();
                fileInputStream.close();
                System.out.println("Nigga das ist falsche difficulty");
                return;
            }

            gameController.clearBoard();
            gameController.stopLoop();
            waveManager.resetAllWaves();
            waveManager.getGame().resetGame();

            waveManager.setGroup(in.readInt() - 1);
            waveManager.getGame().setHp(in.readInt());
            waveManager.getGame().setMoney(in.readInt());
            List<SavedTowerData> towerData = (List<SavedTowerData>) in.readObject();

            for (SavedTowerData towerData1 : towerData) {
                Point2D point2D = new Point2D(towerData1.getX(), towerData1.getY());
                int id = towerData1.getTowerType();
                int dmg = towerData1.getDmg();
                double range = towerData1.getRange();
                double speed = towerData1.getSpeed();


                gameController.spawnLoadedTower(point2D, id, dmg, range, speed);
            }



            in.close();
            fileInputStream.close();


            waveManager.startNextWave();
            gameController.startLoop();
            gameController.resumeLoop();
            System.out.println("Object has been deserialized");

        } catch (IOException ex) {
            System.out.println("IOException is caught");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates the list in which the coordinates and the tower type are
     * saved with the helper class SavedTowerData
     */
    private static List<SavedTowerData> createSavedTowers(GameController gameController) {
        List<SavedTowerData> savedTowers = new ArrayList<>();

        Map<Point2D, Tower> towerMap = gameController.getTowerMap();

        for (Map.Entry<Point2D, Tower> entry : towerMap.entrySet()) {
            Point2D point = entry.getKey();
            Tower tower = entry.getValue();

            SavedTowerData data = new SavedTowerData(
                    point.getX(),
                    point.getY(),
                    tower.getId(),
                    tower.getDamage(),
                    tower.getRange(),
                    tower.getCooldown()
            );

            savedTowers.add(data);
        }
        return savedTowers;
    }
}
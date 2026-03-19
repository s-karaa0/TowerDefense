package org.example.towerdefense;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.towerdefense.Wave.WaveManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.io.IOException;
import java.util.Objects;

public class SettingsController {

    private final AnchorPane root = new AnchorPane();
    private GameApplication gameApp;
    @FXML
    public Button closeButton;
    @FXML
    public Button continueButton;

    public void setApplication(GameApplication gameApp) {
        this.gameApp = gameApp;
        if(gameApp != null && gameApp.isStartState()) {
            continueButton.setDisable(true);
            continueButton.setVisible(false);
        } else {
            continueButton.setDisable(false);
            continueButton.setVisible(true);
        }
    }
    public void onMusicButtonClicked(ActionEvent actionEvent) {
    }

    public void onSaveClicked(ActionEvent actionEvent) throws IOException {
            Stage stage = FXMLLoader.load(getClass().getResource("settings-view.fxml"));
            FileChooser fc=new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML-Dateien", "*.xml"));
            File f=fc.showSaveDialog(stage);
            if (f!=null){
                saveToXML(f);
            }
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
    public void saveToXML(File file){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("settings-view.fxml"));
        GameController controller=loader.getController();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            WaveManager wm=controller.getWaveManager();
            dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();
        // Root-Element
        Element root = doc.createElement("GameState");
        doc.appendChild(root);
        Element level = doc.createElement("Level");
        level.appendChild(doc.createTextNode(String.valueOf(wm.getCurrentWave())));
        root.appendChild(level);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(file);

        transformer.transform(source, result);

        System.out.println("XML gespeichert: " + file.getAbsolutePath());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

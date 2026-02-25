package org.example.towerdefense;

import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;

public class Tower {
    private String url; // hier kommt das Bild von den ressources
    private int damage;
    private int speed;
    ChoiceBox<Object> cb = new ChoiceBox<>();

    public Tower(String url, int damage, int speed) {
        this.url = url;
        this.damage = damage;
        this.speed = speed;
    }

    private void setGoal() {
        cb.setItems(FXCollections.observableArrayList("erster", "letzter", "starkster", "schnellster"));

        if (cb.getSelectionModel().getSelectedItem().equals("erster")) {
            // brauche zuerst die Gegner-Klasse für die Funktion
            // setze Ziel auf den ersten
        } else if (cb.getSelectionModel().getSelectedItem().equals("letzter")) {
            // setze Ziel auf den letzten
        } else if (cb.getSelectionModel().getSelectedItem().equals("staerkster")) {
            // setze Ziel auf den stärksten
        } else {
            // setze Ziel auf den schnellsten
        }
    }
}

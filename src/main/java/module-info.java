module org.example.towerdefense {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.media;


    opens org.example.towerdefense to javafx.fxml;
    exports org.example.towerdefense;
    exports org.example.towerdefense.Wave;
    opens org.example.towerdefense.Wave to javafx.fxml;
    exports org.example.towerdefense.Path;
    opens org.example.towerdefense.Path to javafx.fxml;
}
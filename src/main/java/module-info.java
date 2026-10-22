module org.example.towerdefense {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.media;
    requires com.google.gson;

    opens org.example.towerdefense.Model to com.google.gson;

    opens org.example.towerdefense to javafx.fxml;
    exports org.example.towerdefense;
    exports org.example.towerdefense.Wave;
    opens org.example.towerdefense.Wave to javafx.fxml;
    exports org.example.towerdefense.Path;
    opens org.example.towerdefense.Path to javafx.fxml;
    exports org.example.towerdefense.Controllers;
    opens org.example.towerdefense.Controllers to javafx.fxml;
    exports org.example.towerdefense.Tower;
    opens org.example.towerdefense.Tower to javafx.fxml;

}
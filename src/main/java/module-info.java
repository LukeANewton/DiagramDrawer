module JavaFXtutorial {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires static lombok;

    opens sample.controller to javafx.fxml;
    opens sample to javafx.graphics;
}
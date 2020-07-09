module JavaFXtutorial {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires static lombok;

    opens diagramdrawer.controller to javafx.fxml;
    opens diagramdrawer to javafx.graphics;
}
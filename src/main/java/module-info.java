module diagramdrawer.DiagramDrawer {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires static lombok;
    requires java.desktop;
    requires javafx.swing;
    requires reflections;
    requires javatuples;

    opens diagramdrawer.controller to javafx.fxml;
    opens diagramdrawer.model to javafx.fxml;
    opens diagramdrawer to javafx.graphics;
}
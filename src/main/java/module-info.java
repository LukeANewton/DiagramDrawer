module DiagramDrawer {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires static lombok;
    requires org.slf4j;
    requires java.desktop;
    requires javafx.swing;

    opens diagramdrawer.controller to javafx.fxml;
    opens diagramdrawer.model to javafx.fxml;
    opens diagramdrawer to javafx.graphics;
}
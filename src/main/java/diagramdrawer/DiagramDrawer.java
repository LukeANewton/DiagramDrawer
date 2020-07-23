package diagramdrawer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**Contains the main method to start the application*/
public class DiagramDrawer extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                DiagramDrawer.class.getResource("/main/mainlayout.fxml"));
        Scene scene = new Scene(loader.load());

        primaryStage.setTitle("Diagram Drawer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

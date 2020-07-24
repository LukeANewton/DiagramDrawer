package diagramdrawer.controller;


import diagramdrawer.controller.canvasstate.AddComponentState;
import diagramdrawer.model.drawablecomponent.SingleSectionClassBox;
import diagramdrawer.model.drawablecomponent.ThreeSectionClassBox;
import diagramdrawer.model.drawablecomponent.TwoSectionClassBox;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.embed.swing.SwingFXUtils;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**Set up the methods for use in the mainlayout.fxml window.*/
public class MainWindowController {
    // the root node of the main window fxml
    @FXML
    public BorderPane root;
    //the fxml Pane node containing the canvas
    @FXML
    private Pane canvasPane;
    //the fxml canvas node to be drawn on
    @FXML
    private Canvas canvas;

    //the controller for the Canvas contents
    private CanvasContentManagementController canvasContentManagementController;

    /**initialize method once FXML loads*/
    @FXML
    private void initialize() {
        //set canvas size of center pane
        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());

        //initialize the canvas content management controller
        canvasContentManagementController = new CanvasContentManagementController(canvas);
    }

    /**handler for adding a new SingleSectionClassBox to the canvas*/
    @FXML
    public void drawNewSingleSectionClass(){
        canvasContentManagementController.setCurrentCanvasState(
                new AddComponentState(canvasContentManagementController, new SingleSectionClassBox()));
    }

    /**handler for adding a new TwoSectionClassBox to the canvas*/
    @FXML
    public void drawNewTwoSectionClass(){
        canvasContentManagementController.setCurrentCanvasState(
                new AddComponentState(canvasContentManagementController, new TwoSectionClassBox()));
    }

    /**handler for adding a new ThreeSectionClassBox to the canvas*/
    @FXML
    public void drawNewThreeSectionClass(){
        canvasContentManagementController.setCurrentCanvasState(
                new AddComponentState(canvasContentManagementController, new ThreeSectionClassBox()));
    }

    /**handler for loading a new set of DrawableComponents onto the canvas*/
    @FXML
    public void loadCanvasContents(){

    }

    /**handler for saving the current set of DrawableComponents on the canvas to a file*/
    @FXML
    public void saveCurrentCanvasContents(){

    }

    /**handler for saving the current set of DrawableComponents on the canvas as a PNG image**/
    @FXML
    public void exportCanvasToImageOnClick(){
        canvasContentManagementController.setHighlightedComponent(null);
        canvasContentManagementController.getCanvasDrawController().issueDrawingCommand(() -> {
            //capture canvas image
            WritableImage image = canvas.snapshot(new SnapshotParameters(), null);

            //set up file save dialog
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File selectedFile = fileChooser.showSaveDialog(root.getScene().getWindow());
            if (selectedFile != null) {
                try {
                    //save file
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", selectedFile);
                } catch (IOException e) {
                    //print error alert
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Export to image failed:");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });
    }
}
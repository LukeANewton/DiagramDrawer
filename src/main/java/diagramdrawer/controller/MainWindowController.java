package diagramdrawer.controller;


import diagramdrawer.controller.canvasstate.AddComponentState;
import diagramdrawer.model.drawablecomponent.boxcomponent.SingleSectionClassBox;
import diagramdrawer.model.drawablecomponent.boxcomponent.ThreeSectionClassBox;
import diagramdrawer.model.drawablecomponent.boxcomponent.TwoSectionClassBox;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
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
    private ScrollPane scrollPane;
    //the fxml canvas node to be drawn on
    @FXML
    private Canvas canvas;

    //the controller for the Canvas contents
    private CanvasContentManagementController canvasContentManagementController;
    //the maximum dimension for the canvas drawing
    private static final double CANVAS_MAX_SIZE = 4000;

    /**initialize method once FXML loads*/
    @FXML
    private void initialize() {
        //initialize the canvas content management controller
        canvasContentManagementController = new CanvasContentManagementController(canvas);

        //set a listener to redraw the canvas when the window is resized
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
                canvasContentManagementController.getCanvasDrawController().redrawCanvas();
        root.sceneProperty().addListener((observableScene, oldScene, newScene) ->
                newScene.windowProperty().addListener((observableWindow, oldWindow, newWindow) -> {
                    root.getScene().getWindow().widthProperty().addListener(stageSizeListener);
                    root.getScene().getWindow().heightProperty().addListener(stageSizeListener);
        }));

        //set canvas size of center pane
        canvasContentManagementController.getCanvasDrawController().issueDrawingCommand(() -> {
            canvas.heightProperty().setValue(scrollPane.heightProperty().getValue());
            canvas.widthProperty().setValue(scrollPane.widthProperty().getValue());
        });
        //set listeners to grow canvas size
        scrollPane.vvalueProperty().addListener((observableValue, number, t1) -> {
            if (t1.doubleValue() == scrollPane.getVmax() && canvas.heightProperty().getValue() < CANVAS_MAX_SIZE) {
                canvasContentManagementController.getCanvasDrawController().issueDrawingCommand(() ->
                        canvas.heightProperty().setValue(canvas.heightProperty().getValue() + 100));
            }
        });
        scrollPane.hvalueProperty().addListener((observableValue, number, t1) -> {
            if(t1.doubleValue() == scrollPane.getHmax() && canvas.widthProperty().getValue() < CANVAS_MAX_SIZE){
                canvasContentManagementController.getCanvasDrawController().issueDrawingCommand(() ->
                        canvas.widthProperty().setValue(canvas.widthProperty().getValue() + 100));
            }
        });
    }

    /**handler for adding a new SingleSectionClassBox to the canvas*/
    @FXML
    public void drawNewSingleSectionClass(){
        System.out.println(canvas.getWidth());
        System.out.println(canvas.getHeight());
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
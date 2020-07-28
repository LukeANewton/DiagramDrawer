package diagramdrawer.controller;


import diagramdrawer.controller.canvasstate.AddComponentState;
import diagramdrawer.model.drawablecomponent.Connection;
import diagramdrawer.model.drawablecomponent.DrawableComponent;
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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
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
    //the amount to increase canvas size by when scrolling
    private static final double CANVAS_SIZE_INCREASE = 100;

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
                        canvas.heightProperty().setValue(canvas.heightProperty().getValue() + CANVAS_SIZE_INCREASE));
            }
        });
        scrollPane.hvalueProperty().addListener((observableValue, number, t1) -> {
            if(t1.doubleValue() == scrollPane.getHmax() && canvas.widthProperty().getValue() < CANVAS_MAX_SIZE){
                canvasContentManagementController.getCanvasDrawController().issueDrawingCommand(() ->
                        canvas.widthProperty().setValue(canvas.widthProperty().getValue() + CANVAS_SIZE_INCREASE));
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
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (selectedFile != null) {
            try{
                FileInputStream in = new FileInputStream(selectedFile);
                String contents = new String(in.readAllBytes(), StandardCharsets.UTF_8);

                //get each tag contents as a separate String
                ArrayList<String> tags = new ArrayList<>();
                for(int i = 0; i < contents.length(); i++){
                    if(contents.charAt(i) == '<'){
                        for(int j = i; j < contents.length() - 1; j++){
                            if(contents.charAt(j) == '/' && contents.charAt(j+1) == '>'){
                                tags.add(contents.substring(i+1, j));
                                i=j+1;
                                break;
                            }
                        }
                    }
                }

                //for each tag, find the tag fields and instantiate a new object
                ArrayList<DrawableComponent> drawableComponents = new ArrayList<>();
                for(String tag: tags){
                    String tagName = tag.substring(0, tag.indexOf(" "));
                    String tagFields = tag.substring(tag.indexOf(" "));

                    //build a map of each tag's fields
                    HashMap<String, String> map = separateTagFields(tagFields, new HashMap<>());

                    if(tagName.equals(Connection.class.getName())){
                        drawableComponents.add(Connection.fromXML(map));
                    } else if(tagName.equals(SingleSectionClassBox.class.getName())){
                        drawableComponents.add(SingleSectionClassBox.fromXML(map));
                    } else if(tagName.equals(TwoSectionClassBox.class.getName())){
                        drawableComponents.add(TwoSectionClassBox.fromXML(map));
                    } else if(tagName.equals(ThreeSectionClassBox.class.getName())){
                        drawableComponents.add(ThreeSectionClassBox.fromXML(map));
                    }
                }

                //set the new list of components
                canvasContentManagementController.setDrawnComponents(drawableComponents);
                canvasContentManagementController.getCanvasDrawController().redrawCanvas();
            } catch(IOException e){
                //print error alert
                System.out.println(e.getMessage());
                showErrorAlert("Load failed:", e.getMessage());
            }
        }
    }

    /**
     * splits a string containing a series of key-value pairs separated by spaces into a hash map
     *
     * @param tagFields the string left to parse
     * @param partialSolution the map built so far from the string
     * @return the final hash map result
     */
    private HashMap<String, String> separateTagFields(String tagFields, HashMap<String, String> partialSolution){
        String fieldName;
        for(int i = 0; i < tagFields.length(); i++){
            if(tagFields.charAt(i) == '='){//this is the end of the field name
                fieldName = tagFields.substring(1, tagFields.indexOf("="));
                for(int j = i+2; j < tagFields.length(); j++){
                    if(tagFields.charAt(j) == '\"'){
                        partialSolution.put(fieldName, tagFields.substring(i+2, j));
                        return separateTagFields(tagFields.substring(j+1), partialSolution);
                    }
                }
            }
        }
        return partialSolution;
    }

    /**handler for saving the current set of DrawableComponents on the canvas to a file*/
    @FXML
    public void saveCurrentCanvasContents() {
        FileChooser fileChooser = new FileChooser();
        //Show save file dialog
        File selectedFile = fileChooser.showSaveDialog(root.getScene().getWindow());
        if (selectedFile != null) {
            try {
                //generate save file
                StringBuilder s = new StringBuilder();
                for (DrawableComponent component : canvasContentManagementController.getDrawnComponents()) {
                    s.append(component.toXML()).append(System.lineSeparator());
                }

                //save file
                FileOutputStream out = new FileOutputStream(selectedFile);
                out.write(s.toString().getBytes());
                out.close();
            } catch(IOException e){
                showErrorAlert("Save failed:", e.getMessage());
            }
        }
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
                    showErrorAlert("Export to image failed:", e.getMessage());
                }
            }
        });
    }

    /**
     * displays an alert box with the passed message
     *
     * @param message the message to display on the alert box
     * @param error details to add to the message
     */
    private void showErrorAlert(String message, String error){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(message);
        alert.setContentText(error);
        alert.showAndWait();
    }
}
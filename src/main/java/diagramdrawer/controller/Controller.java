package diagramdrawer.controller;


import diagramdrawer.model.SingleSectionClass;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Controller {
    @FXML
    public Pane canvasPane;
    @FXML
    private Button boxOneSectionButton;
    @FXML
    private Canvas canvas;

    public Controller() {

    }

    @FXML
    private void initialize() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //set canvas size of center pane
        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());

        //add event to toolbar buttons to enable canvas clicking
        boxOneSectionButton.setOnAction(event -> canvas.setOnMouseClicked(clickEvent -> drawSingleSectionBox(gc, clickEvent)));
    }

    /**
     * method to draw a class box on the canvas at the location clicked
     *
     * @param gc the context of the canvas to draw on
     * @param event the mouse click event containing the click coordinates for drawing
     */
    private void drawSingleSectionBox(GraphicsContext gc, MouseEvent event) {
        final int DEFAULT_SINGLE_SECTION_BOX_HEIGHT = 50;
        final int DEFAULT_SINGLE_SECTION_BOX_WIDTH = 100;

        //the click coordinates are the center of the box, so they are converted first to start drawing from
        final int startX = (int) (event.getX() - (DEFAULT_SINGLE_SECTION_BOX_WIDTH/2));
        final int startY = (int) (event.getY() - (DEFAULT_SINGLE_SECTION_BOX_HEIGHT/2));

        Runnable task = () -> {
            //create object and draw it
            SingleSectionClass boxToDraw = new SingleSectionClass("Class", startX, startY,
                    DEFAULT_SINGLE_SECTION_BOX_HEIGHT, DEFAULT_SINGLE_SECTION_BOX_WIDTH);
            boxToDraw.draw(gc);

            //remove event so one button click lets you place one object
            canvas.setOnMouseClicked(null);
        };

        //run thread
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
}
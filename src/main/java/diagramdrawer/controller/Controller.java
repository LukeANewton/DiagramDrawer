package diagramdrawer.controller;


import diagramdrawer.model.DrawableComponent;
import diagramdrawer.model.SingleSectionClass;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@Slf4j
public class Controller {
    @FXML
    public Pane canvasPane;
    @FXML
    private Button boxOneSectionButton;
    @FXML
    private Canvas canvas;

    //default sizes for newly created components
    private final int DEFAULT_SINGLE_SECTION_BOX_HEIGHT = 50;
    private final int DEFAULT_SINGLE_SECTION_BOX_WIDTH = 100;

    //the components drawn on the canvas
    ArrayList<DrawableComponent> drawnComponents;

    public Controller() {
        drawnComponents = new ArrayList<>();
    }

    @FXML
    private void initialize() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //set canvas size of center pane
        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());

        //add event to toolbar buttons to enable canvas clicking
        boxOneSectionButton.setOnAction(event -> {
            canvas.setOnMouseClicked(clickEvent -> {
                try {
                    drawFinalComponent(gc, SingleSectionClass.class, clickEvent.getX(), clickEvent.getY(),
                                    DEFAULT_SINGLE_SECTION_BOX_HEIGHT, DEFAULT_SINGLE_SECTION_BOX_WIDTH);
                } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
            canvas.setOnMouseMoved(moveEvent -> {
                try {
                    drawPreviewComponent(gc, SingleSectionClass.class, moveEvent.getX(), moveEvent.getY(),
                            DEFAULT_SINGLE_SECTION_BOX_HEIGHT, DEFAULT_SINGLE_SECTION_BOX_WIDTH);
                } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
                });
    }

    private void drawFinalComponent(GraphicsContext gc, Class<? extends DrawableComponent> classBox, double clickX, double clickY,
                                    int height, int width) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        canvas.setOnMouseMoved(null);
        canvas.setOnMouseClicked(null);
        DrawableComponent newComponent = drawClassBox(gc, classBox, clickX, clickY, height, width, Color.BLACK, "Class");
        drawnComponents.add(newComponent);
    }

    private void drawPreviewComponent(GraphicsContext gc, Class<? extends DrawableComponent> classBox, double clickX, double clickY,
                                      int height, int width) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        clearTempCanvasContents(gc);
        drawClassBox(gc, classBox, clickX, clickY, height, width, Color.LIGHTGRAY, "");
    }

    /**
     * method to draw a class box on the canvas at the location clicked
     *
     * @param gc the context of the canvas to draw on
     * @param classBox the class of the object to draw on the canvas
     * @param clickX the x coordinate at the center of the draw
     * @param clickY the y coordinate of the center of the draw
     * @param height the height of the object to draw
     * @param width the width of the object to draw
     * @param color the color to draw the box in
     * @param title the title to display on the box
     */
    private DrawableComponent drawClassBox(GraphicsContext gc, Class<? extends DrawableComponent> classBox, double clickX, double clickY,
                              int height, int width, Color color, String title) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //the click coordinates are the center of the box, so they are converted first to the corner to start drawing from
        final int startX = (int) (clickX - (width >> 1));
        final int startY = (int) (clickY - (height >> 1));

        DrawableComponent boxToDraw = classBox.getConstructor(
                new Class[]{String.class, int.class, int.class, int.class, int.class})
                .newInstance(title, startX, startY, height, width);

        Runnable task = () -> boxToDraw.draw(gc, color);

        //run thread
        Platform.runLater(task);
        return boxToDraw;
    }

    /**clears the canvas of any temporary draws by resetting canvas and redrawing each permanent draw
     *
     * @param gc the graphics context of the canvas
     */
    private void clearTempCanvasContents(GraphicsContext gc){
        Runnable task = () -> {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            for(DrawableComponent component : drawnComponents){
                component.draw(gc, Color.BLACK);
            }
        };

        //run thread
        Platform.runLater(task);

    }
}
package diagramdrawer.controller;


import diagramdrawer.model.DrawableComponent;
import diagramdrawer.model.SingleSectionClass;
import diagramdrawer.model.TwoSectionClass;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

@Slf4j
public class Controller {
    @FXML
    public Pane canvasPane;
    @FXML
    private Button boxOneSectionButton;
    @FXML
    private Button boxTwoSectionButton;
    @FXML
    private Canvas canvas;

    //default sizes for newly created components
    private final int DEFAULT_SINGLE_SECTION_BOX_HEIGHT = 50;
    private final int DEFAULT_SINGLE_SECTION_BOX_WIDTH = 100;
    private final int DEFAULT_TWO_SECTION_BOX_HEIGHT = 80;
    private final int DEFAULT_TWO_SECTION_BOX_WIDTH = 100;

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

        //add event on single section class box
        boxOneSectionButton.setOnAction(event -> setNewComponentListeners(gc, SingleSectionClass.class,
                DEFAULT_SINGLE_SECTION_BOX_HEIGHT, DEFAULT_SINGLE_SECTION_BOX_WIDTH));

        //add event on two section class box
        boxTwoSectionButton.setOnAction(event -> setNewComponentListeners(gc, TwoSectionClass.class,
                DEFAULT_TWO_SECTION_BOX_HEIGHT, DEFAULT_TWO_SECTION_BOX_WIDTH));
    }

    /**
     * sets a event handler for creating a new object on the given graphics context that is of type classBox,
     * and has a specified height and width. The handler sets a click event on the canvas that draws the specified
     * DrawableComponent at the location clicked, or cancels the operation if the click is a right click
     *
     * @param gc the graphics context to draw on
     * @param classBox the class to draw on the canvas
     * @param height the height of the new object to draw
     * @param width the width of the new object ot draw
     */
    private void setNewComponentListeners(GraphicsContext gc, Class<? extends DrawableComponent> classBox,
                                          int height, int width){
        //add listener to draw the object on left click, and cancel the operation on right click
        canvas.setOnMouseClicked(clickEvent -> {
            if(clickEvent.getButton() == MouseButton.SECONDARY){
                // a right click indicates cancelling the new component addition
                cancelNewComponentListeners();
                clearTempCanvasContents(gc);
            } else if(clickEvent.getButton() == MouseButton.PRIMARY){
                try {
                    drawFinalComponent(gc, classBox, clickEvent.getX(), clickEvent.getY(), height, width);
                } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        //add listener to draw preview on canvas whenever the mouse is moved to a new location
        canvas.setOnMouseMoved(moveEvent -> {
            try {
                drawPreviewComponent(gc, classBox, moveEvent.getX(), moveEvent.getY(), height, width);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    /**used to reset the canvas mouse event handlers when they aree no longer needed*/
    private void cancelNewComponentListeners(){
        canvas.setOnMouseMoved(null);
        canvas.setOnMouseClicked(null);
    }

    /**
     * draws a DrawableComponent in black onto the canvas at the specified X and Y coordinates,
     * with a specified height and width. The component is added to the master list of canvas contents
     * and the mouse event handlers for drawing a new component are removed
     *
     * @param gc the graphics context of the canvas to draw onto
     * @param classBox the type of DrawableComponent to draw
     * @param clickX the X coordinate at the center of the object to draw
     * @param clickY the Y coordinate at the center of the object to draw
     * @param height the height of the object to draw
     * @param width the width of the object to draw
     */
    private void drawFinalComponent(GraphicsContext gc, Class<? extends DrawableComponent> classBox, double clickX, double clickY,
                                    int height, int width) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        cancelNewComponentListeners();
        DrawableComponent newComponent = drawComponent(gc, classBox, clickX, clickY, height, width, Color.BLACK, "Class");
        drawnComponents.add(newComponent);
    }

    /**
     * draws a DrawableComponent in light gray onto the canvas at the specified X and Y coordinates,
     * with a specified height and width. This is intended as a preview of what the object will look
     * like when clicked
     *
     * @param gc the graphics context of the canvas to draw onto
     * @param classBox the type of DrawableComponent to draw
     * @param clickX the X coordinate at the center of the object to draw
     * @param clickY the Y coordinate at the center of the object to draw
     * @param height the height of the object to draw
     * @param width the width of the object to draw
     */
    private void drawPreviewComponent(GraphicsContext gc, Class<? extends DrawableComponent> classBox, double clickX, double clickY,
                                      int height, int width) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        clearTempCanvasContents(gc);
        drawComponent(gc, classBox, clickX, clickY, height, width, Color.LIGHTGRAY, "");
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
    private DrawableComponent drawComponent(GraphicsContext gc, Class<? extends DrawableComponent> classBox, double clickX, double clickY,
                              int height, int width, Color color, String title) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //the click coordinates are the center of the box, so they are converted first to the corner to start drawing from
        final int startX = (int) (clickX - (width >> 1));
        final int startY = (int) (clickY - (height >> 1));

        //create new component to draw
        DrawableComponent boxToDraw = classBox.getConstructor(
                new Class[]{String.class, int.class, int.class, int.class, int.class})
                .newInstance(title, startX, startY, height, width);

        //draw component in thread
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

        Platform.runLater(task);
    }
}
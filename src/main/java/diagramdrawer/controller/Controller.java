package diagramdrawer.controller;


import diagramdrawer.model.DrawableComponent;
import diagramdrawer.model.SingleSectionClass;
import diagramdrawer.model.TwoSectionClass;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
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

    //thicknesses for drawing lines
    private final int DRAW_THICKNESS = 1;       //"normal" width lines for drawing
    private final int HIGHLIGHT_THICKNESS = 4;  //thicker lines to indicate which box is highlighted

    //the components drawn on the canvas
    ArrayList<DrawableComponent> drawnComponents;
    //the DrawableComponent currently highlighted
    DrawableComponent highlightedComponent;
    //the GraphicsContent for the canvas to draw on
    GraphicsContext gc;

    public Controller() {
        drawnComponents = new ArrayList<>();
        highlightedComponent = null;
    }

    @FXML
    private void initialize() {
        gc = canvas.getGraphicsContext2D();

        //set canvas size of center pane
        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());

        //add event on single section class box
        boxOneSectionButton.setOnAction(event -> setNewComponentListeners(SingleSectionClass.class,
                DEFAULT_SINGLE_SECTION_BOX_HEIGHT, DEFAULT_SINGLE_SECTION_BOX_WIDTH));

        //add event on two section class box
        boxTwoSectionButton.setOnAction(event -> setNewComponentListeners(TwoSectionClass.class,
                DEFAULT_TWO_SECTION_BOX_HEIGHT, DEFAULT_TWO_SECTION_BOX_WIDTH));

        //add event on canvas to highlight/unhighlight drawn components
        canvas.setOnMouseEntered(this::highlightDrawableComponentHandler);
    }

    /**
     * sets a event handler for creating a new object on the given graphics context that is of type classBox,
     * and has a specified height and width. The handler sets a click event on the canvas that draws the specified
     * DrawableComponent at the location clicked, or cancels the operation if the click is a right click
     *
     * @param classBox the class to draw on the canvas
     * @param height the height of the new object to draw
     * @param width the width of the new object ot draw
     */
    private void setNewComponentListeners(Class<? extends DrawableComponent> classBox,
                                          int height, int width){
        //unhighlight any objects
        highlightedComponent = null;

        //add listener to draw the object on left click, and cancel the operation on right click
        canvas.setOnMousePressed(clickEvent -> {
            if(clickEvent.getButton() == MouseButton.SECONDARY){
                // a right click indicates cancelling the new component addition
                cancelNewComponentListeners();
            } else if(clickEvent.getButton() == MouseButton.PRIMARY){
                try {
                    drawFinalComponent(classBox, clickEvent.getX(), clickEvent.getY(), height, width);
                } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        //add listener to draw preview on canvas whenever the mouse is moved to a new location
        canvas.setOnMouseMoved(moveEvent -> {
            try {
                drawPreviewComponent(classBox, moveEvent.getX(), moveEvent.getY(), height, width);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    /**used to reset the canvas mouse event handlers when they are no longer needed*/
    private void cancelNewComponentListeners(){
        canvas.setOnMouseMoved(null);
        canvas.setOnMousePressed(this::highlightDrawableComponentHandler);
    }

    /**
     * draws a DrawableComponent in black onto the canvas at the specified X and Y coordinates,
     * with a specified height and width. The component is added to the master list of canvas contents
     * and the mouse event handlers for drawing a new component are removed
     *
     * @param classBox the type of DrawableComponent to draw
     * @param clickX the X coordinate at the center of the object to draw
     * @param clickY the Y coordinate at the center of the object to draw
     * @param height the height of the object to draw
     * @param width the width of the object to draw
     */
    private void drawFinalComponent(Class<? extends DrawableComponent> classBox, double clickX, double clickY,
                                    int height, int width) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        cancelNewComponentListeners();
        DrawableComponent newComponent = drawComponent(classBox, clickX, clickY, height, width, Color.BLACK, "Class");
        drawnComponents.add(newComponent);
    }

    /**
     * draws a DrawableComponent in light gray onto the canvas at the specified X and Y coordinates,
     * with a specified height and width. This is intended as a preview of what the object will look
     * like when clicked
     *
     * @param classBox the type of DrawableComponent to draw
     * @param clickX the X coordinate at the center of the object to draw
     * @param clickY the Y coordinate at the center of the object to draw
     * @param height the height of the object to draw
     * @param width the width of the object to draw
     */
    private void drawPreviewComponent(Class<? extends DrawableComponent> classBox, double clickX, double clickY,
                                      int height, int width) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        drawComponent(classBox, clickX, clickY, height, width, Color.LIGHTGRAY, "");
    }

    /**
     * method to draw a class box on the canvas at the location clicked
     *
     * @param classBox the class of the object to draw on the canvas
     * @param clickX the x coordinate at the center of the draw
     * @param clickY the y coordinate of the center of the draw
     * @param height the height of the object to draw
     * @param width the width of the object to draw
     * @param color the color to draw the box in
     * @param title the title to display on the box
     */
    private DrawableComponent drawComponent(Class<? extends DrawableComponent> classBox, double clickX, double clickY,
                              int height, int width, Color color, String title) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        //the click coordinates are the center of the box, so they are converted first to the corner to start drawing from
        final int startX = (int) (clickX - (width >> 1));
        final int startY = (int) (clickY - (height >> 1));

        //create new component to draw
        DrawableComponent boxToDraw = classBox.getConstructor(
                new Class[]{String.class, int.class, int.class, int.class, int.class})
                .newInstance(title, startX, startY, height, width);

        //draw component in thread
        issueDrawingCommand(() -> boxToDraw.draw(gc, color, DRAW_THICKNESS));

        return boxToDraw;
    }

    /**
     * Click event handler for the canvas to highlight/unhighlight components
     *
     * @param clickEvent the MouseEvent containing the location on the canvas clicked
     */
    private void highlightDrawableComponentHandler(MouseEvent clickEvent){
        double x = clickEvent.getX();
        double y = clickEvent.getY();

        //check if click location is in the bound of a component
        for(DrawableComponent component: drawnComponents){
            if(component.getStartX() < x && component.getStartY() < y &&
                    component.getStartX() + component.getWidth() > x && component.getStartY() + component.getHeight() > y){
                //highlight/unhighlight the clicked component
                if(component.equals(highlightedComponent)) {
                    highlightedComponent = null;
                } else {
                    highlightedComponent = component;
                }
                issueDrawingCommand(() -> {});
                canvas.setOnDragDetected((dragEvent) -> dragComponentHandler(dragEvent, component));
                return;
            }
        }
        //if we make it here, the background was clicked, and nothing should be highlighted
        highlightedComponent = null;
        issueDrawingCommand(() -> {});
    }

    private void dragComponentHandler(MouseEvent mouseEvent, DrawableComponent component){
       canvas.setOnMouseDragged((e) -> System.out.println("dragging"));
        canvas.setOnMouseReleased((e) ->  {
            System.out.println("done dragging");
            canvas.setOnMouseReleased(null);
            canvas.setOnMousePressed(this::highlightDrawableComponentHandler);
        });
        System.out.println("start dragging");
    }

    /**
     * used to run a GUI updating Runnable on the JavaFX application thread
     *
     * @param task the Runnable to execute
     */
    private void issueDrawingCommand(Runnable task){
        Platform.runLater(() -> {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            for(DrawableComponent component : drawnComponents){
                if(component.equals(highlightedComponent))
                    component.draw(gc, Color.RED, HIGHLIGHT_THICKNESS);
                else
                    component.draw(gc, Color.BLACK, DRAW_THICKNESS);
            }
        });
        Platform.runLater(task);
    }
}
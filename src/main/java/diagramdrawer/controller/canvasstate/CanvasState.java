package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.Controller;
import diagramdrawer.model.DrawableComponent;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;

public abstract class CanvasState {
    //the controller for the main window
    protected Controller controller;
    //the canvas to draw on
    protected Canvas canvas;

    //thicknesses for drawing lines
    private final int DRAW_THICKNESS = 1;       //"normal" width lines for drawing
    private final int HIGHLIGHT_THICKNESS = 4;  //thicker lines to indicate which box is highlighted

    /**
     * Constructor
     *
     * @param controller the controller for the main window that uses this Canvas State object
     */
    public CanvasState(Controller controller){
        this.controller = controller;
        this.canvas = controller.getCanvas();
        enterState();
    }

    /**
     * draws a DrawableComponent in black onto the canvas at the specified X and Y coordinates,
     * with a specified height and width.
     *
     * @param component the DrawableComponent to draw
     * @param clickX the X coordinate at the center of the object to draw
     * @param clickY the Y coordinate at the center of the object to draw
     */
    protected DrawableComponent drawFinalComponent(DrawableComponent component, double clickX, double clickY) {
        return drawComponent(component, clickX, clickY, Color.BLACK, "Class");
    }

    /**
     * draws a DrawableComponent in light gray onto the canvas at the specified X and Y coordinates,
     * with a specified height and width. This is intended as a preview of what the object will look
     * like when clicked
     *
     * @param component the DrawableComponent to draw
     *  @param clickX the X coordinate at the center of the object to draw
     *  @param clickY the Y coordinate at the center of the object to draw
     */
    protected void drawPreviewComponent(DrawableComponent component, double clickX, double clickY) {
        drawComponent(component, clickX, clickY, Color.LIGHTGRAY, "");
    }

    /**
     * method to draw a class box on the canvas at the location clicked
     *
     * @param boxToDraw  the DrawableComponent to draw
     * @param clickX the x coordinate at the center of the draw
     * @param clickY the y coordinate of the center of the draw
     * @param color the color to draw the box in
     * @param title the title to display on the box
     * @return the component drawn on the canvas
     */
    protected DrawableComponent drawComponent(DrawableComponent boxToDraw, double clickX, double clickY, Color color, String title) {
        boxToDraw.setTitle(title);
        boxToDraw.setCenterX(clickX);
        boxToDraw.setCenterY(clickY);
        issueDrawingCommand(() -> boxToDraw.draw(canvas.getGraphicsContext2D(), color, DRAW_THICKNESS));

        return boxToDraw;
    }

    /**
     * used to run a GUI updating Runnable on the JavaFX application thread
     *
     * @param task the Runnable to execute
     */
    protected void issueDrawingCommand(Runnable task){
        Platform.runLater(() -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();

            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            for(DrawableComponent component : controller.getDrawnComponents()){
                if(component.equals(controller.getHighlightedComponent()))
                    component.draw(gc, Color.RED, HIGHLIGHT_THICKNESS);
                else
                    component.draw(gc, Color.BLACK, DRAW_THICKNESS);
            }
        });
        Platform.runLater(task);
    }

    /**redraw the canvas with no changes to the contents*/
    public void redrawCanvas(){
        issueDrawingCommand(() -> {});
    }

    /**Set the listeners for each event*/
    public void enterState(){
        canvas.setOnMousePressed(this::mousePressedHandler);
        canvas.setOnMouseMoved(this::mouseMoveHandler);
        canvas.setOnMouseReleased(this::mouseReleasedHandler);
        canvas.setOnMouseDragged(this::mouseDraggedHandler);
        canvas.setOnDragDetected(this::dragDetectedHandler);
        Stage.getWindows().stream().filter(Window::isShowing).findFirst().ifPresent(
                currentWindow -> currentWindow.getScene().setOnKeyPressed(this::keyStrokeHandler));

        controller.updateStateStack();
    }

    /**activities to be done before the next state is entered*/
    public void exitState(){}

    /*listeners for each event to be optionally overridden for each state*/
    public void mousePressedHandler(MouseEvent mouseEvent){}
    public void mouseMoveHandler(MouseEvent mouseEvent){}
    public void mouseReleasedHandler(MouseEvent mouseEvent){}
    public void mouseDraggedHandler(MouseEvent mouseEvent){}
    public void dragDetectedHandler(MouseEvent dragEvent){}
    public void keyStrokeHandler(KeyEvent keyEvent){}
}

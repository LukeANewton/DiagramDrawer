package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.CanvasContentsController;
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
    protected CanvasContentsController canvasContentsController;
    //the canvas to draw on
    protected Canvas canvas;

    //thicknesses for drawing lines
    private final int DRAW_THICKNESS = 1;       //"normal" width lines for drawing
    private final int HIGHLIGHT_THICKNESS = 4;  //thicker lines to indicate which box is highlighted

    /**
     * Constructor
     *
     * @param canvasContentsController the controller for the main window that uses this Canvas State object
     */
    public CanvasState(CanvasContentsController canvasContentsController){
        this.canvasContentsController = canvasContentsController;
        this.canvas = canvasContentsController.getCanvas();
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
        component.setCenterX(clickX);
        component.setCenterY(clickY);
        issueDrawingCommand(() -> component.draw(canvas.getGraphicsContext2D(), Color.BLACK, DRAW_THICKNESS));

        return component;
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
        issueDrawingCommand(() -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setLineWidth(DRAW_THICKNESS);
            gc.setStroke(Color.LIGHTGRAY);
            gc.strokeRect(clickX - (component.getWidth()/2), clickY - (component.getHeight()/2),
                    component.getWidth(), component.getHeight());
        });
    }

    /**
     * used to run a GUI updating Runnable on the JavaFX application thread
     *
     * @param task the Runnable to execute
     */
    public void issueDrawingCommand(Runnable task){
        Platform.runLater(() -> {
            GraphicsContext gc = canvas.getGraphicsContext2D();

            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            for(DrawableComponent component : canvasContentsController.getDrawnComponents()){
                if(component.equals(canvasContentsController.getHighlightedComponent()))
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

        canvasContentsController.updateStateStack();
        redrawCanvas();
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

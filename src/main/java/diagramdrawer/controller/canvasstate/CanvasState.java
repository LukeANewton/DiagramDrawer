package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.Controller;
import diagramdrawer.model.DrawableComponent;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public abstract class CanvasState {
    protected Controller controller;
    protected Canvas canvas;

    //thicknesses for drawing lines
    private final int DRAW_THICKNESS = 1;       //"normal" width lines for drawing
    private final int HIGHLIGHT_THICKNESS = 4;  //thicker lines to indicate which box is highlighted

    public CanvasState(Controller controller){
        this.controller = controller;
        this.canvas = controller.getCanvas();
        enterState();
    }

    /**
     * draws a DrawableComponent in black onto the canvas at the specified X and Y coordinates,
     * with a specified height and width. The component is added to the master list of canvas contents
     * and the mouse event handlers for drawing a new component are removed
     *
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
     *  @param clickX the X coordinate at the center of the object to draw
     *  @param clickY the Y coordinate at the center of the object to draw
     */
    protected void drawPreviewComponent(DrawableComponent component, double clickX, double clickY) {
        drawComponent(component, clickX, clickY, Color.LIGHTGRAY, "");
    }

    /**
     * method to draw a class box on the canvas at the location clicked
     *
     * @param clickX the x coordinate at the center of the draw
     * @param clickY the y coordinate of the center of the draw
     * @param color the color to draw the box in
     * @param title the title to display on the box
     */
    protected DrawableComponent drawComponent(DrawableComponent boxToDraw, double clickX, double clickY, Color color, String title) {
        boxToDraw.setTitle(title);
        boxToDraw.setCenterX(clickX);
        boxToDraw.setCenterY(clickY);

        //draw component in thread
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

    public void enterState(){
        canvas.setOnMousePressed(this::mousePressedHandler);
        canvas.setOnMouseMoved(this::mouseMoveHandler);
        canvas.setOnMouseReleased(this::mouseReleasedHandler);
        canvas.setOnMouseDragged(this::mouseDraggedHandler);
        canvas.setOnDragDetected(this::dragDetectedHandler);
    }

    public void exitState(){}
    public void mousePressedHandler(MouseEvent mouseEvent){}
    public void mouseMoveHandler(MouseEvent mouseEvent){}
    public void mouseReleasedHandler(MouseEvent mouseEvent){}
    public void mouseDraggedHandler(MouseEvent mouseEvent){}
    public void dragDetectedHandler(MouseEvent dragEvent){}
}

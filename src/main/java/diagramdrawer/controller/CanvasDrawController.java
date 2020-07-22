package diagramdrawer.controller;

import diagramdrawer.model.drawablecomponent.DrawableComponent;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CanvasDrawController {
    //the canvas to draw on
    private Canvas canvas;
    //the graphics context of the canvas to draw on
    private GraphicsContext gc;
    //the controller that manages the canvas content
    private CanvasContentManagementController canvasContentManagementController;

    //thicknesses for drawing lines
    private final int DRAW_THICKNESS = 1;       //"normal" width lines for drawing
    private final int HIGHLIGHT_THICKNESS = 4;  //thicker lines to indicate which box is highlighted

    public CanvasDrawController(CanvasContentManagementController canvasContentManagementController){
        this.canvasContentManagementController = canvasContentManagementController;
        this.canvas = canvasContentManagementController.getCanvas();
        this.gc = canvas.getGraphicsContext2D();
    }

    /**
     * draws a DrawableComponent in black onto the canvas at the specified X and Y coordinates,
     * with a specified height and width.
     *
     * @param component the DrawableComponent to draw
     * @param clickX the X coordinate at the center of the object to draw
     * @param clickY the Y coordinate at the center of the object to draw
     */
    public DrawableComponent drawFinalComponent(DrawableComponent component, double clickX, double clickY) {
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
    public void drawPreviewComponent(DrawableComponent component, double clickX, double clickY) {
        issueDrawingCommand(() -> {
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
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            for(DrawableComponent component : canvasContentManagementController.getDrawnComponents()){
                if(component.equals(canvasContentManagementController.getHighlightedComponent()))
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
}

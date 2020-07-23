package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.CanvasContentManagementController;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

/**Sets the events performed on each possible action depending on which implementation is active*/
public abstract class CanvasState {
    //the controller for the main window
    protected CanvasContentManagementController canvasContentManagementController;
    //the canvas to draw on
    protected Canvas canvas;

    /**
     * Constructor
     *
     * @param canvasContentManagementController the controller for the main window that uses this Canvas State object
     */
    public CanvasState(CanvasContentManagementController canvasContentManagementController){
        this.canvasContentManagementController = canvasContentManagementController;
        this.canvas = canvasContentManagementController.getCanvas();
        enterState();
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

        canvasContentManagementController.getDrawnComponentStateStack().updateStateStack(canvasContentManagementController.getDrawnComponents());
        canvasContentManagementController.getCanvasDrawController().redrawCanvas();
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

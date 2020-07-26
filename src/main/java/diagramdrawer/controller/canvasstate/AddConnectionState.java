package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.CanvasContentManagementController;
import diagramdrawer.model.drawablecomponent.Connection;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javafx.geometry.Point2D;

public class AddConnectionState extends CanvasState {
    //the new connection to draw on the canvas
    Connection newConnection;

    /**
     * Constructor
     *
     * @param canvasContentManagementController the controller for the main window using this state
     * @param newConnection                      the new component to draw on the canvas
     */
    public AddConnectionState(CanvasContentManagementController canvasContentManagementController, Connection newConnection) {
        super(canvasContentManagementController);
        this.newConnection = newConnection;
        newConnection.setStart(canvasContentManagementController.findClosestPointOnComponentEdge(newConnection.getStart().getX(), newConnection.getStart().getY()));
    }

    @Override
    public void exitState() {
        canvasContentManagementController.setCurrentCanvasState(new SelectComponentState(canvasContentManagementController));
    }

    @Override
    public void enterState() {
        super.enterState();
        canvasContentManagementController.setHighlightedComponent(null);
    }

    @Override
    public void mousePressedHandler(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseButton.SECONDARY){
            // a right click indicates cancelling the new component addition
            exitState();
        } else if(mouseEvent.getButton() == MouseButton.PRIMARY){
            //draw the component on the canvas
            newConnection.setEnd(canvasContentManagementController.findClosestPointOnComponentEdge(mouseEvent.getX(), mouseEvent.getY()));
            canvasContentManagementController.getCanvasDrawController().drawFinalComponent(newConnection);
            canvasContentManagementController.addComponent(newConnection);
            exitState();
        }
    }

    @Override
    public void mouseMoveHandler(MouseEvent mouseEvent) {
        //draw a preview of where the component will be drawn on the canvas
        newConnection.setEnd(new Point2D(mouseEvent.getX(), mouseEvent.getY()));
        canvasContentManagementController.getCanvasDrawController().drawPreviewComponent(newConnection);
    }
}

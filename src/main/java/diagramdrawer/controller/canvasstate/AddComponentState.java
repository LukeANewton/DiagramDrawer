package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.CanvasContentManagementController;
import diagramdrawer.model.drawablecomponent.DrawableComponent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class AddComponentState extends CanvasState {
    DrawableComponent newComponent;

    /**
     * Constructor
     *
     * @param canvasContentManagementController the controller for the main window using this state
     * @param newComponent the new component to draw on the canvas
     */
    public AddComponentState(CanvasContentManagementController canvasContentManagementController, DrawableComponent newComponent){
        super(canvasContentManagementController);
        this.newComponent = newComponent;
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
            newComponent = canvasContentManagementController.getCanvasDrawController().drawFinalComponent(newComponent, mouseEvent.getX(), mouseEvent.getY());
            canvasContentManagementController.addComponent(newComponent);
            exitState();
        }
    }

    @Override
    public void mouseMoveHandler(MouseEvent mouseEvent) {
        canvasContentManagementController.getCanvasDrawController().drawPreviewComponent(newComponent, mouseEvent.getX(), mouseEvent.getY());
    }
}

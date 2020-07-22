package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.CanvasContentsController;
import diagramdrawer.model.drawablecomponent.DrawableComponent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class AddComponentState extends CanvasState {
    DrawableComponent newComponent;

    /**
     * Constructor
     *
     * @param canvasContentsController the controller for the main window using this state
     * @param newComponent the new component to draw on the canvas
     */
    public AddComponentState(CanvasContentsController canvasContentsController, DrawableComponent newComponent){
        super(canvasContentsController);
        this.newComponent = newComponent;
    }

    @Override
    public void exitState() {
        canvasContentsController.setCurrentCanvasState(new SelectComponentState(canvasContentsController));
    }

    @Override
    public void enterState() {
        super.enterState();
        canvasContentsController.setHighlightedComponent(null);
    }

    @Override
    public void mousePressedHandler(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseButton.SECONDARY){
            // a right click indicates cancelling the new component addition
            exitState();
        } else if(mouseEvent.getButton() == MouseButton.PRIMARY){
            newComponent = canvasContentsController.getCanvasDrawController().drawFinalComponent(newComponent, mouseEvent.getX(), mouseEvent.getY());
            canvasContentsController.addComponent(newComponent);
            exitState();
        }
    }

    @Override
    public void mouseMoveHandler(MouseEvent mouseEvent) {
        canvasContentsController.getCanvasDrawController().drawPreviewComponent(newComponent, mouseEvent.getX(), mouseEvent.getY());
    }
}

package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.CanvasContentManagementController;
import diagramdrawer.model.drawablecomponent.DrawableComponent;
import javafx.scene.input.MouseEvent;

public class MoveComponentState extends CanvasState{
    DrawableComponent componentToDrag;

    /**
     * Constructor
     *
     * @param canvasContentManagementController the controller for the main window using this state
     * @param componentToDrag the component to move on the canvas
     */
    public MoveComponentState(CanvasContentManagementController canvasContentManagementController, DrawableComponent componentToDrag){
        super(canvasContentManagementController);
        this.componentToDrag = componentToDrag;
    }

    @Override
    public void exitState(){
        canvasContentManagementController.setCurrentCanvasState(new SelectComponentState(canvasContentManagementController));
    }

    @Override
    public void mouseDraggedHandler(MouseEvent mouseEvent) {
        canvasContentManagementController.getCanvasDrawController().drawPreviewComponent(componentToDrag, mouseEvent.getX(), mouseEvent.getY());
    }

    @Override
    public void mouseReleasedHandler(MouseEvent mouseEvent) {
        canvasContentManagementController.getCanvasDrawController().drawFinalComponent(componentToDrag, mouseEvent.getX(), mouseEvent.getY());
        canvasContentManagementController.getCanvasDrawController().redrawCanvas();
        exitState();
    }
}

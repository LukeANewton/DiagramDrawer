package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.CanvasContentManagementController;
import diagramdrawer.model.drawablecomponent.DrawableComponent;
import javafx.scene.input.MouseEvent;

/**Handles the repositioning of a component on the canvas*/
public class MoveComponentState extends CanvasState{
    //the component to reposition on the canvas
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
        //draw a preview of where the component will now be drawn
        canvasContentManagementController.getCanvasDrawController().drawPreviewComponent(componentToDrag, mouseEvent.getX(), mouseEvent.getY());
    }

    @Override
    public void mouseReleasedHandler(MouseEvent mouseEvent) {
        //redraw the component in it's new canvas position
        canvasContentManagementController.getCanvasDrawController().drawFinalComponent(componentToDrag, mouseEvent.getX(), mouseEvent.getY());
        canvasContentManagementController.getCanvasDrawController().redrawCanvas();
        exitState();
    }
}

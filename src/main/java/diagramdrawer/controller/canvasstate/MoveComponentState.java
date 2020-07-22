package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.CanvasContentsController;
import diagramdrawer.model.DrawableComponent;
import javafx.scene.input.MouseEvent;

public class MoveComponentState extends CanvasState{
    DrawableComponent componentToDrag;

    /**
     * Constructor
     *
     * @param canvasContentsController the controller for the main window using this state
     * @param componentToDrag the component to move on the canvas
     */
    public MoveComponentState(CanvasContentsController canvasContentsController, DrawableComponent componentToDrag){
        super(canvasContentsController);
        this.componentToDrag = componentToDrag;
    }

    @Override
    public void exitState(){
        canvasContentsController.setCurrentCanvasState(new SelectComponentState(canvasContentsController));
    }

    @Override
    public void mouseDraggedHandler(MouseEvent mouseEvent) {
        super.drawPreviewComponent(componentToDrag, mouseEvent.getX(), mouseEvent.getY());
    }

    @Override
    public void mouseReleasedHandler(MouseEvent mouseEvent) {
        super.drawFinalComponent(componentToDrag, mouseEvent.getX(), mouseEvent.getY());
        super.redrawCanvas();
        exitState();
    }
}

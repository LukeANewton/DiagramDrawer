package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.Controller;
import diagramdrawer.model.DrawableComponent;
import javafx.scene.input.MouseEvent;

public class MoveComponentState extends CanvasState{
    DrawableComponent componentToDrag;

    /**
     * Constructor
     *
     * @param controller the controller for the main window using this state
     * @param componentToDrag the component to move on the canvas
     */
    public MoveComponentState(Controller controller, DrawableComponent componentToDrag){
        super(controller);
        this.componentToDrag = componentToDrag;
    }

    @Override
    public void exitState(){
        controller.setCurrentCanvasState(new SelectComponentState(controller));
    }

    @Override
    public void mouseDraggedHandler(MouseEvent mouseEvent) {
        super.drawPreviewComponent(controller.getHighlightedComponent(), mouseEvent.getX(), mouseEvent.getY());
    }

    @Override
    public void mouseReleasedHandler(MouseEvent mouseEvent) {
        super.drawFinalComponent(controller.getHighlightedComponent(), mouseEvent.getX(), mouseEvent.getY());
        super.redrawCanvas();
        exitState();
    }
}

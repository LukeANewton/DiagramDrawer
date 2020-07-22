package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.MainWindowController;
import diagramdrawer.model.DrawableComponent;
import javafx.scene.input.MouseEvent;

public class MoveComponentState extends CanvasState{
    DrawableComponent componentToDrag;

    /**
     * Constructor
     *
     * @param mainWindowController the controller for the main window using this state
     * @param componentToDrag the component to move on the canvas
     */
    public MoveComponentState(MainWindowController mainWindowController, DrawableComponent componentToDrag){
        super(mainWindowController);
        this.componentToDrag = componentToDrag;
    }

    @Override
    public void exitState(){
        mainWindowController.setCurrentCanvasState(new SelectComponentState(mainWindowController));
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

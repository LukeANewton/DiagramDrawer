package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.Controller;
import diagramdrawer.model.DrawableComponent;
import javafx.scene.input.MouseEvent;

public class DragComponentState extends CanvasState{
    DrawableComponent componentToDrag;

    /**
     * Constructor
     *
     * @param controller the controller for the main window using this state
     * @param componentToDrag the component to move on the canvas
     */
    public DragComponentState(Controller controller, DrawableComponent componentToDrag){
        super(controller);
        this.componentToDrag = componentToDrag;
    }

    @Override
    public void enterState() {
        super.enterState();
        System.out.println("start dragging");
    }

    @Override
    public void exitState(){
        controller.setCurrentCanvasState(new SelectComponentState(controller));
    }

    @Override
    public void mouseDraggedHandler(MouseEvent mouseEvent) {
        System.out.println("dragging");
    }

    @Override
    public void mouseReleasedHandler(MouseEvent mouseEvent) {
        System.out.println("done dragging");
        exitState();
    }
}

package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.Controller;
import diagramdrawer.model.DrawableComponent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class AddComponentState extends CanvasState {
    DrawableComponent newComponent;

    /**
     * Constructor
     *
     * @param controller the controller for the main window using this state
     * @param newComponent the new component to draw on the canvas
     */
    public AddComponentState(Controller controller, DrawableComponent newComponent){
        super(controller);
        this.newComponent = newComponent;
    }

    @Override
    public void exitState() {
        controller.setCurrentCanvasState(new SelectComponentState(controller));
    }

    @Override
    public void enterState() {
        super.enterState();
        controller.setHighlightedComponent(null);
    }

    @Override
    public void mousePressedHandler(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseButton.SECONDARY){
            // a right click indicates cancelling the new component addition
            exitState();
        } else if(mouseEvent.getButton() == MouseButton.PRIMARY){
            newComponent = drawFinalComponent(newComponent, mouseEvent.getX(), mouseEvent.getY());
            controller.addComponent(newComponent);
            exitState();
        }
    }

    @Override
    public void mouseMoveHandler(MouseEvent mouseEvent) {
        drawPreviewComponent(newComponent, mouseEvent.getX(), mouseEvent.getY());
    }
}

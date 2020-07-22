package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.MainWindowController;
import diagramdrawer.model.DrawableComponent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class AddComponentState extends CanvasState {
    DrawableComponent newComponent;

    /**
     * Constructor
     *
     * @param mainWindowController the controller for the main window using this state
     * @param newComponent the new component to draw on the canvas
     */
    public AddComponentState(MainWindowController mainWindowController, DrawableComponent newComponent){
        super(mainWindowController);
        this.newComponent = newComponent;
    }

    @Override
    public void exitState() {
        mainWindowController.setCurrentCanvasState(new SelectComponentState(mainWindowController));
    }

    @Override
    public void enterState() {
        super.enterState();
        mainWindowController.setHighlightedComponent(null);
    }

    @Override
    public void mousePressedHandler(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseButton.SECONDARY){
            // a right click indicates cancelling the new component addition
            exitState();
        } else if(mouseEvent.getButton() == MouseButton.PRIMARY){
            newComponent = drawFinalComponent(newComponent, mouseEvent.getX(), mouseEvent.getY());
            mainWindowController.addComponent(newComponent);
            exitState();
        }
    }

    @Override
    public void mouseMoveHandler(MouseEvent mouseEvent) {
        drawPreviewComponent(newComponent, mouseEvent.getX(), mouseEvent.getY());
    }
}

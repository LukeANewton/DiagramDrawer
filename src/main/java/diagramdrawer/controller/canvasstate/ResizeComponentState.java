package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.MainWindowController;
import diagramdrawer.model.DrawableComponent;
import javafx.scene.input.MouseEvent;

public class ResizeComponentState extends CanvasState {
    private DrawableComponent componentToResize;
    private ResizeDirection resizeDirection;

    private static final double MINIMUM_COMPONENT_SIZE = 5;

    /**
     * Constructor
     *
     * @param mainWindowController the controller for the main window using this state
     * @param componentToResize the component to resize on the canvas
     */
    public ResizeComponentState(MainWindowController mainWindowController, DrawableComponent componentToResize, ResizeDirection resizeDirection){
        super(mainWindowController);
        this.componentToResize = componentToResize;
        this.resizeDirection = resizeDirection;
    }

    @Override
    public void exitState(){
        mainWindowController.updateStateStack();
        mainWindowController.setCurrentCanvasState(new SelectComponentState(mainWindowController));
    }

    @Override
    public void mouseDraggedHandler(MouseEvent mouseEvent) {
        //set new size
        double oldCenterX = componentToResize.getCenterX();
        double oldCenterY = componentToResize.getCenterY();
        double leftEdge = componentToResize.getCenterX() - (componentToResize.getWidth() / 2);
        double topEdge = componentToResize.getCenterY() - (componentToResize.getHeight() / 2);
        double rightEdge = componentToResize.getCenterX() + (componentToResize.getWidth() / 2);
        double bottomEdge = componentToResize.getCenterY() + (componentToResize.getHeight() / 2);
        switch(resizeDirection){
            case LEFT:
                componentToResize.setWidth(rightEdge - mouseEvent.getX());
                componentToResize.setCenterX(mouseEvent.getX() + (componentToResize.getWidth() / 2));
                break;
            case RIGHT:
                componentToResize.setWidth(mouseEvent.getX() - leftEdge);
                componentToResize.setCenterX(mouseEvent.getX() - (componentToResize.getWidth() / 2));
                break;
            case TOP:
                componentToResize.setHeight(bottomEdge - mouseEvent.getY());
                componentToResize.setCenterY(mouseEvent.getY() + (componentToResize.getHeight() / 2));
                break;
            case BOTTOM:
                componentToResize.setHeight(mouseEvent.getY() - topEdge);
                componentToResize.setCenterY(mouseEvent.getY() - (componentToResize.getHeight() / 2));
                break;
        }

        //set minimum size for component
        if(componentToResize.getHeight() <= MINIMUM_COMPONENT_SIZE){
            componentToResize.setHeight(MINIMUM_COMPONENT_SIZE);
            componentToResize.setCenterY(oldCenterY);
        }
        if(componentToResize.getWidth() <= MINIMUM_COMPONENT_SIZE){
            componentToResize.setWidth(MINIMUM_COMPONENT_SIZE);
            componentToResize.setCenterX(oldCenterX);
        }

        super.drawPreviewComponent(componentToResize, componentToResize.getCenterX(), componentToResize.getCenterY());
    }

    @Override
    public void mouseReleasedHandler(MouseEvent mouseEvent) {
        super.drawFinalComponent(componentToResize, componentToResize.getCenterX(), componentToResize.getCenterY());
        super.redrawCanvas();
        exitState();
    }
}

package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.Controller;
import diagramdrawer.model.DrawableComponent;
import javafx.scene.input.MouseEvent;

public class ResizeComponentState extends CanvasState {
    DrawableComponent componentToResize;
    ResizeDirection resizeDirection;

    /**
     * Constructor
     *
     * @param controller the controller for the main window using this state
     * @param componentToResize the component to resize on the canvas
     */
    public ResizeComponentState(Controller controller, DrawableComponent componentToResize, ResizeDirection resizeDirection){
        super(controller);
        this.componentToResize = componentToResize;
        this.resizeDirection = resizeDirection;
    }

    @Override
    public void exitState(){
        controller.setCurrentCanvasState(new SelectComponentState(controller));
    }

    @Override
    public void mouseDraggedHandler(MouseEvent mouseEvent) {
        //set new size
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

        super.drawPreviewComponent(componentToResize, componentToResize.getCenterX(), componentToResize.getCenterY());
    }

    @Override
    public void mouseReleasedHandler(MouseEvent mouseEvent) {
        super.drawFinalComponent(componentToResize, componentToResize.getCenterX(), componentToResize.getCenterY());
        super.redrawCanvas();
        exitState();
    }
}

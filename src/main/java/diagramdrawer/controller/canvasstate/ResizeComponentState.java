package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.CanvasContentManagementController;
import diagramdrawer.model.drawablecomponent.DrawableComponent;
import javafx.scene.input.MouseEvent;

/**Handles the resizing of a component on the canvas*/
public class ResizeComponentState extends CanvasState {
    //the component to resize
    private DrawableComponent componentToResize;
    //the direction in which to resize the component
    private ResizeDirection resizeDirection;

    private static final double MINIMUM_COMPONENT_SIZE = 5;

    /**
     * Constructor
     *
     * @param canvasContentManagementController the controller for the main window using this state
     * @param componentToResize the component to resize on the canvas
     */
    public ResizeComponentState(CanvasContentManagementController canvasContentManagementController, DrawableComponent componentToResize, ResizeDirection resizeDirection){
        super(canvasContentManagementController);
        this.componentToResize = componentToResize;
        this.resizeDirection = resizeDirection;
    }

    @Override
    public void exitState(){
        canvasContentManagementController.setCurrentCanvasState(new SelectComponentState(canvasContentManagementController));
    }

    @Override
    public void mouseDraggedHandler(MouseEvent mouseEvent) {
        //set new size based on direction specified
        double oldCenterX = componentToResize.getCenterX();
        double oldCenterY = componentToResize.getCenterY();
        double leftEdge = componentToResize.getCenterX() - (componentToResize.getWidth() / 2);
        double topEdge = componentToResize.getCenterY() - (componentToResize.getHeight() / 2);
        double rightEdge = componentToResize.getCenterX() + (componentToResize.getWidth() / 2);
        double bottomEdge = componentToResize.getCenterY() + (componentToResize.getHeight() / 2);
        switch(resizeDirection){
            case LEFT:
                resizeHorizontalLeft(rightEdge, mouseEvent.getX());
                break;
            case RIGHT:
                resizeHorizontalRight(leftEdge, mouseEvent.getX());
                break;
            case TOP:
                resizeVerticalUp(bottomEdge, mouseEvent.getY());
                break;
            case BOTTOM:
                resizeVerticalDown(topEdge, mouseEvent.getY());
                break;
            case TOP_LEFT:
                resizeHorizontalLeft(rightEdge, mouseEvent.getX());
                resizeVerticalUp(bottomEdge, mouseEvent.getY());
                break;
            case TOP_RIGHT:
                resizeHorizontalRight(leftEdge, mouseEvent.getX());
                resizeVerticalUp(bottomEdge, mouseEvent.getY());
                break;
            case BOTTOM_LEFT:
                resizeHorizontalLeft(rightEdge, mouseEvent.getX());
                resizeVerticalDown(topEdge, mouseEvent.getY());
                break;
            case BOTTOM_RIGHT:
                resizeHorizontalRight(leftEdge, mouseEvent.getX());
                resizeVerticalDown(topEdge, mouseEvent.getY());
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

        //draw preview of resizing
        canvasContentManagementController.getCanvasDrawController().drawPreviewComponent(componentToResize, componentToResize.getCenterX(), componentToResize.getCenterY());
    }

    @Override
    public void mouseReleasedHandler(MouseEvent mouseEvent) {
        //draw final resized component
        canvasContentManagementController.getCanvasDrawController().drawFinalComponent(componentToResize, componentToResize.getCenterX(), componentToResize.getCenterY());
        canvasContentManagementController.getCanvasDrawController().redrawCanvas();
        exitState();
    }

    /**
     * resize the selected component horizontally on the left side
     *
     * @param oppositeEdge the x coordinate of the right edge of the box to resize
     * @param xClickCoord the x coordinate of the mouse to resize with
     */
    private void resizeHorizontalLeft(double oppositeEdge, double xClickCoord){
        componentToResize.setWidth(oppositeEdge - xClickCoord);
        componentToResize.setCenterX(xClickCoord + (componentToResize.getWidth() / 2));
    }

    /**
     * resize the selected component horizontally on the right side
     *
     * @param oppositeEdge the x coordinate of the left edge of the box to resize
     * @param xClickCoord the x coordinate of the mouse to resize with
     */
    private void resizeHorizontalRight(double oppositeEdge, double xClickCoord){
        componentToResize.setWidth(xClickCoord - oppositeEdge);
        componentToResize.setCenterX(xClickCoord - (componentToResize.getWidth() / 2));
    }

    /**
     * resize the selected component vertically on the top side
     *
     * @param oppositeEdge the y coordinate of the bottom edge of the box to resize
     * @param yClickCoord the y coordinate of the mouse to resize with
     */
    private void resizeVerticalUp(double oppositeEdge, double yClickCoord){
        componentToResize.setHeight(oppositeEdge - yClickCoord);
        componentToResize.setCenterY(yClickCoord + (componentToResize.getHeight() / 2));
    }

    /**
     * resize the selected component vertically on the bottom side
     *
     * @param oppositeEdge the y coordinate of the top edge of the box to resize
     * @param yClickCoord the y coordinate of the mouse to resize with
     */
    private void resizeVerticalDown(double oppositeEdge, double yClickCoord){
        componentToResize.setHeight(yClickCoord - oppositeEdge);
        componentToResize.setCenterY(yClickCoord - (componentToResize.getHeight() / 2));
    }
}

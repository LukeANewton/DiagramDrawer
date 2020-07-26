package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.CanvasContentManagementController;
import diagramdrawer.model.drawablecomponent.Connection;
import diagramdrawer.model.drawablecomponent.DrawableComponent;
import diagramdrawer.model.drawablecomponent.boxcomponent.BoxComponent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**Handles the selecting and deselecting of components drawn on the canvas to
 * perform actions on*/
public class SelectComponentState extends CanvasState {
    //sets how close to the edge a mouse drag must be to trigger component resizing
    private static final double COMPONENT_EDGE_THRESHOLD = 15;

    /**
     * Constructor
     *
     * @param canvasContentManagementController the controller for the main window using this state
     */
    public SelectComponentState(CanvasContentManagementController canvasContentManagementController){
        super(canvasContentManagementController);
    }

    @Override
    public void mousePressedHandler(MouseEvent mouseEvent) {
        //get the position on the mouse click
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();

        //check if click location is in the bound of a component
        for(DrawableComponent component: canvasContentManagementController.getDrawnComponents()){
            if(component.checkPointInBounds(x, y)){
                if(mouseEvent.getButton() == MouseButton.PRIMARY){
                    if(mouseEvent.getClickCount() == 1){ //single click
                        //highlight/unhighlight the clicked component
                        canvasContentManagementController.setHighlightedComponent(component);
                        canvasContentManagementController.getCanvasDrawController().redrawCanvas();
                        return;
                    } else if(mouseEvent.getClickCount() == 2){ //double click
                        //edit the contents of the component that was double clicked
                        canvasContentManagementController.setCurrentCanvasState(
                                new EditComponentContentsState(canvasContentManagementController, component));
                    }
                } else if(mouseEvent.getButton() == MouseButton.SECONDARY){//right click
                    //draw a connection between two points
                    canvasContentManagementController.setCurrentCanvasState(
                            new AddConnectionState(canvasContentManagementController,
                                    new Connection(mouseEvent.getX(), mouseEvent.getY())));
                }
            }
        }
        //if we make it here, the background was clicked, and nothing should be highlighted
        canvasContentManagementController.setHighlightedComponent(null);
        canvasContentManagementController.getCanvasDrawController().redrawCanvas();
    }

    @Override
    public void dragDetectedHandler(MouseEvent dragEvent) {
        if(canvasContentManagementController.getHighlightedComponent() instanceof BoxComponent){
            //drag on the highlighted component
            BoxComponent componentToDrag = (BoxComponent) canvasContentManagementController.getHighlightedComponent();
            if(componentToDrag != null) {
            /*we need to check if the drag is on the edge of the component (to resize),
            or in the center (to move)*/
                double x = dragEvent.getX();
                double y = dragEvent.getY();
                double leftEdge = componentToDrag.getCenterX() - (componentToDrag.getWidth() / 2);
                double topEdge = componentToDrag.getCenterY() - (componentToDrag.getHeight() / 2);
                double rightEdge = componentToDrag.getCenterX() + (componentToDrag.getWidth() / 2);
                double bottomEdge = componentToDrag.getCenterY() + (componentToDrag.getHeight() / 2);

                if (checkCloseToEdge(topEdge, y) && checkCloseToEdge(leftEdge, x)){//resize from the top left
                    canvasContentManagementController.setCurrentCanvasState(new ResizeComponentState(
                            canvasContentManagementController, componentToDrag, ResizeDirection.TOP_LEFT));
                } else if (checkCloseToEdge(topEdge, y) && checkCloseToEdge(rightEdge, x)){//resize from the top right
                    canvasContentManagementController.setCurrentCanvasState(new ResizeComponentState(
                            canvasContentManagementController, componentToDrag, ResizeDirection.TOP_RIGHT));
                } else if (checkCloseToEdge(bottomEdge, y) && checkCloseToEdge(leftEdge, x)){//resize from the bottom left
                    canvasContentManagementController.setCurrentCanvasState(new ResizeComponentState(
                            canvasContentManagementController, componentToDrag, ResizeDirection.BOTTOM_LEFT));
                } else if (checkCloseToEdge(bottomEdge, y) && checkCloseToEdge(rightEdge, x)){//resize from the bottom right
                    canvasContentManagementController.setCurrentCanvasState(new ResizeComponentState(
                            canvasContentManagementController, componentToDrag, ResizeDirection.BOTTOM_RIGHT));
                } else if (checkCloseToEdge(rightEdge, x)){//resize from the right edge
                    canvasContentManagementController.setCurrentCanvasState(new ResizeComponentState(
                            canvasContentManagementController, componentToDrag, ResizeDirection.RIGHT));
                }else if(checkCloseToEdge(bottomEdge, y)){//resize from the bottom edge
                    canvasContentManagementController.setCurrentCanvasState(new ResizeComponentState(
                            canvasContentManagementController, componentToDrag, ResizeDirection.BOTTOM));
                }else if(checkCloseToEdge(topEdge, y)) {//resize from the top edge
                    canvasContentManagementController.setCurrentCanvasState(new ResizeComponentState(
                            canvasContentManagementController, componentToDrag, ResizeDirection.TOP));
                }else if(checkCloseToEdge(leftEdge, x)) {//resize from the left edge
                    canvasContentManagementController.setCurrentCanvasState(new ResizeComponentState(
                            canvasContentManagementController, componentToDrag, ResizeDirection.LEFT));
                } else {//if no edge checks pass, we are repositioning the component, rather than resizing
                    canvasContentManagementController.setCurrentCanvasState(new MoveComponentState(canvasContentManagementController, componentToDrag));
                }
            }
        }
    }

    /**
     * checks if the location the drag event started is near the highlighted component's edge
     *
     * @param edgeCoordinate the coordinate edge to check against
     * @param mouseCoordinate the coordinate the drag event started from
     * @return true if the drag action started near the specified edge
     */
    private boolean checkCloseToEdge(double edgeCoordinate, double mouseCoordinate){
        return mouseCoordinate < (edgeCoordinate + COMPONENT_EDGE_THRESHOLD) &&
                mouseCoordinate > (edgeCoordinate - COMPONENT_EDGE_THRESHOLD);
    }

    @Override
    public void keyStrokeHandler(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.DELETE || keyEvent.getCode() == KeyCode.BACK_SPACE){
            //delete the selected component
            DrawableComponent componentToDelete = canvasContentManagementController.getHighlightedComponent();
            if(componentToDelete != null){
                canvasContentManagementController.removeComponent(componentToDelete);
                canvasContentManagementController.setHighlightedComponent(null);
                canvasContentManagementController.getCanvasDrawController().redrawCanvas();
            }
        } else if (keyEvent.getCode() == KeyCode.Z){
            //undo the last change made to the canvas
            canvasContentManagementController.undoLastCanvasChange();
        }
    }
}

package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.CanvasContentManagementController;
import diagramdrawer.model.drawablecomponent.DrawableComponent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class SelectComponentState extends CanvasState {
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
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();

        //check if click location is in the bound of a component
        for(DrawableComponent component: canvasContentManagementController.getDrawnComponents()){
            if(component.checkPointInBounds(x, y)){
                if(mouseEvent.getClickCount() == 1){
                    //highlight/unhighlight the clicked component
                    canvasContentManagementController.setHighlightedComponent(component);
                    canvasContentManagementController.getCanvasDrawController().redrawCanvas();
                    return;
                } else if(mouseEvent.getClickCount() == 2){
                    canvasContentManagementController.setCurrentCanvasState(new EditComponentContentsState(canvasContentManagementController, component));
                }

            }
        }
        //if we make it here, the background was clicked, and nothing should be highlighted
        canvasContentManagementController.setHighlightedComponent(null);
        canvasContentManagementController.getCanvasDrawController().redrawCanvas();
    }

    @Override
    public void dragDetectedHandler(MouseEvent dragEvent) {
        DrawableComponent componentToDrag = canvasContentManagementController.getHighlightedComponent();

        if(componentToDrag != null) {
            /*we need to check if the drag is on the edge of the component (to resize),
            or in the center (to move)*/
            double x = dragEvent.getX();
            double y = dragEvent.getY();
            double leftEdge = componentToDrag.getCenterX() - (componentToDrag.getWidth() / 2);
            double topEdge = componentToDrag.getCenterY() - (componentToDrag.getHeight() / 2);
            double rightEdge = componentToDrag.getCenterX() + (componentToDrag.getWidth() / 2);
            double bottomEdge = componentToDrag.getCenterY() + (componentToDrag.getHeight() / 2);

            if (checkCloseToEdge(topEdge, y) && checkCloseToEdge(leftEdge, x)){
                canvasContentManagementController.setCurrentCanvasState(new ResizeComponentState(
                        canvasContentManagementController, componentToDrag, ResizeDirection.TOP_LEFT));
            } else if (checkCloseToEdge(topEdge, y) && checkCloseToEdge(rightEdge, x)){
                canvasContentManagementController.setCurrentCanvasState(new ResizeComponentState(
                        canvasContentManagementController, componentToDrag, ResizeDirection.TOP_RIGHT));
            } else if (checkCloseToEdge(bottomEdge, y) && checkCloseToEdge(leftEdge, x)){
                canvasContentManagementController.setCurrentCanvasState(new ResizeComponentState(
                        canvasContentManagementController, componentToDrag, ResizeDirection.BOTTOM_LEFT));
            } else if (checkCloseToEdge(bottomEdge, y) && checkCloseToEdge(rightEdge, x)){
                canvasContentManagementController.setCurrentCanvasState(new ResizeComponentState(
                        canvasContentManagementController, componentToDrag, ResizeDirection.BOTTOM_RIGHT));
            } else if (checkCloseToEdge(rightEdge, x)){
                canvasContentManagementController.setCurrentCanvasState(new ResizeComponentState(
                        canvasContentManagementController, componentToDrag, ResizeDirection.RIGHT));
            }else if(checkCloseToEdge(bottomEdge, y)){
                canvasContentManagementController.setCurrentCanvasState(new ResizeComponentState(
                        canvasContentManagementController, componentToDrag, ResizeDirection.BOTTOM));
            }else if(checkCloseToEdge(topEdge, y)) {
                canvasContentManagementController.setCurrentCanvasState(new ResizeComponentState(
                        canvasContentManagementController, componentToDrag, ResizeDirection.TOP));
            }else if(checkCloseToEdge(leftEdge, x)) {
                canvasContentManagementController.setCurrentCanvasState(new ResizeComponentState(
                        canvasContentManagementController, componentToDrag, ResizeDirection.LEFT));
            } else {
                canvasContentManagementController.setCurrentCanvasState(new MoveComponentState(canvasContentManagementController, componentToDrag));
            }
        }
    }

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
            canvasContentManagementController.undoLastCanvasChange();
        }
    }
}

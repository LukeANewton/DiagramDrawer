package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.CanvasContentsController;
import diagramdrawer.model.drawablecomponent.DrawableComponent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class SelectComponentState extends CanvasState {
    private static final double COMPONENT_EDGE_THRESHOLD = 15;

    /**
     * Constructor
     *
     * @param canvasContentsController the controller for the main window using this state
     */
    public SelectComponentState(CanvasContentsController canvasContentsController){
        super(canvasContentsController);
    }

    @Override
    public void mousePressedHandler(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();

        //check if click location is in the bound of a component
        for(DrawableComponent component: canvasContentsController.getDrawnComponents()){
            if(component.checkPointInBounds(x, y)){
                if(mouseEvent.getClickCount() == 1){
                    //highlight/unhighlight the clicked component
                    canvasContentsController.setHighlightedComponent(component);
                    redrawCanvas();
                    return;
                } else if(mouseEvent.getClickCount() == 2){
                    canvasContentsController.setCurrentCanvasState(new EditComponentContentsState(canvasContentsController, component));
                }

            }
        }
        //if we make it here, the background was clicked, and nothing should be highlighted
        canvasContentsController.setHighlightedComponent(null);
        redrawCanvas();
    }

    @Override
    public void dragDetectedHandler(MouseEvent dragEvent) {
        DrawableComponent componentToDrag = canvasContentsController.getHighlightedComponent();

        if(componentToDrag != null) {
            /*we need to check if the drag is on the edge of the component (to resize),
            or in the center (to move)*/
            double x = dragEvent.getX();
            double y = dragEvent.getY();
            double leftEdge = componentToDrag.getCenterX() - (componentToDrag.getWidth() / 2);
            double topEdge = componentToDrag.getCenterY() - (componentToDrag.getHeight() / 2);
            double rightEdge = componentToDrag.getCenterX() + (componentToDrag.getWidth() / 2);
            double bottomEdge = componentToDrag.getCenterY() + (componentToDrag.getHeight() / 2);

            if (checkCloseToEdge(rightEdge, x)){
                canvasContentsController.setCurrentCanvasState(new ResizeComponentState(
                        canvasContentsController, componentToDrag, ResizeDirection.RIGHT));
            }else if(checkCloseToEdge(bottomEdge, y)){
                canvasContentsController.setCurrentCanvasState(new ResizeComponentState(
                        canvasContentsController, componentToDrag, ResizeDirection.BOTTOM));
            }else if(checkCloseToEdge(topEdge, y)) {
                canvasContentsController.setCurrentCanvasState(new ResizeComponentState(
                        canvasContentsController, componentToDrag, ResizeDirection.TOP));
            }else if(checkCloseToEdge(leftEdge, x)) {
                canvasContentsController.setCurrentCanvasState(new ResizeComponentState(
                        canvasContentsController, componentToDrag, ResizeDirection.LEFT));
            } else {
                canvasContentsController.setCurrentCanvasState(new MoveComponentState(canvasContentsController, componentToDrag));
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
            DrawableComponent componentToDelete = canvasContentsController.getHighlightedComponent();
            if(componentToDelete != null){
                canvasContentsController.removeComponent(componentToDelete);
                canvasContentsController.setHighlightedComponent(null);
                super.redrawCanvas();
            }
        } else if (keyEvent.getCode() == KeyCode.Z){
            canvasContentsController.undoLastCanvasChange();
        }
    }
}

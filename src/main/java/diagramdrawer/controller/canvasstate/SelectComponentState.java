package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.MainWindowController;
import diagramdrawer.model.DrawableComponent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class SelectComponentState extends CanvasState {
    private static final double COMPONENT_EDGE_THRESHOLD = 15;

    /**
     * Constructor
     *
     * @param mainWindowController the controller for the main window using this state
     */
    public SelectComponentState(MainWindowController mainWindowController){
        super(mainWindowController);
    }

    @Override
    public void mousePressedHandler(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();

        //check if click location is in the bound of a component
        for(DrawableComponent component: mainWindowController.getDrawnComponents()){
            if(component.checkPointInBounds(x, y)){
                if(mouseEvent.getClickCount() == 1){
                    //highlight/unhighlight the clicked component
                    mainWindowController.setHighlightedComponent(component);
                    redrawCanvas();
                    return;
                } else if(mouseEvent.getClickCount() == 2){
                    mainWindowController.setCurrentCanvasState(new EditComponentContentsState(mainWindowController, component));
                }

            }
        }
        //if we make it here, the background was clicked, and nothing should be highlighted
        mainWindowController.setHighlightedComponent(null);
        redrawCanvas();
    }

    @Override
    public void dragDetectedHandler(MouseEvent dragEvent) {
        DrawableComponent componentToDrag = mainWindowController.getHighlightedComponent();

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
                mainWindowController.setCurrentCanvasState(new ResizeComponentState(
                        mainWindowController, componentToDrag, ResizeDirection.RIGHT));
            }else if(checkCloseToEdge(bottomEdge, y)){
                mainWindowController.setCurrentCanvasState(new ResizeComponentState(
                        mainWindowController, componentToDrag, ResizeDirection.BOTTOM));
            }else if(checkCloseToEdge(topEdge, y)) {
                mainWindowController.setCurrentCanvasState(new ResizeComponentState(
                        mainWindowController, componentToDrag, ResizeDirection.TOP));
            }else if(checkCloseToEdge(leftEdge, x)) {
                    mainWindowController.setCurrentCanvasState(new ResizeComponentState(
                            mainWindowController, componentToDrag, ResizeDirection.LEFT));
            } else {
                mainWindowController.setCurrentCanvasState(new MoveComponentState(mainWindowController, componentToDrag));
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
            DrawableComponent componentToDelete = mainWindowController.getHighlightedComponent();
            if(componentToDelete != null){
                mainWindowController.removeComponent(componentToDelete);
                mainWindowController.setHighlightedComponent(null);
                super.redrawCanvas();
            }
        } else if (keyEvent.getCode() == KeyCode.Z){
            mainWindowController.undoLastAction();
        }
    }
}

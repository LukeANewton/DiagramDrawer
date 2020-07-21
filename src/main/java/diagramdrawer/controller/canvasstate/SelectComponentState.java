package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.Controller;
import diagramdrawer.model.DrawableComponent;
import javafx.scene.input.MouseEvent;

public class SelectComponentState extends CanvasState {

    /**
     * Constructor
     *
     * @param controller the controller for the main window using this state
     */
    public SelectComponentState(Controller controller){
        super(controller);
    }

    @Override
    public void mousePressedHandler(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();

        //check if click location is in the bound of a component
        for(DrawableComponent component: controller.getDrawnComponents()){
            if(component.checkPointInBounds(x, y)){
                //highlight/unhighlight the clicked component
                controller.setHighlightedComponent(component);
                redrawCanvas();
                return;
            }
        }
        //if we make it here, the background was clicked, and nothing should be highlighted
        controller.setHighlightedComponent(null);
        redrawCanvas();
    }

    @Override
    public void dragDetectedHandler(MouseEvent dragEvent) {
        if(controller.getHighlightedComponent() != null) {
            controller.setCurrentCanvasState(
                    new MoveComponentState(controller, controller.getHighlightedComponent()));
        }
    }
}

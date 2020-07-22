package diagramdrawer.controller;

import diagramdrawer.controller.canvasstate.CanvasState;
import diagramdrawer.controller.canvasstate.SelectComponentState;
import diagramdrawer.model.CanvasContentStateStack;
import diagramdrawer.model.drawablecomponent.DrawableComponent;
import javafx.scene.canvas.Canvas;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class CanvasContentsController {
    //the components drawn on the canvas
    @Getter @Setter
    private ArrayList<DrawableComponent> drawnComponents;
    //the DrawableComponent currently highlighted
    @Getter @Setter
    private DrawableComponent highlightedComponent;
    //a stack to allow actions to be undone
    @Getter
    private CanvasContentStateStack drawnComponentStateStack;
    //the state of the canvas
    @Setter @Getter
    private CanvasState currentCanvasState;
    //the canvas to be drawn on
    @Getter
    Canvas canvas;

    public CanvasContentsController(Canvas canvas) {
        this.canvas = canvas;
        drawnComponents = new ArrayList<>();
        highlightedComponent = null;
        drawnComponentStateStack = new CanvasContentStateStack();

        //set the starting state for the canvas
        currentCanvasState = new SelectComponentState(this);
    }

    /**
     * adds a component to the list of components in the diagram
     *
     * @param newComponent the component added to the list
     */
    public void addComponent(DrawableComponent newComponent){
        drawnComponents.add(newComponent);
    }

    /**
     * removes a component to the list of components in the diagram
     *
     * @param componentToRemove the component to remove from the list
     */
    public void removeComponent(DrawableComponent componentToRemove){
        drawnComponents.remove(componentToRemove);
    }

    /**undo the last change made to the canvas drawing*/
    public void undoLastCanvasChange(){
        drawnComponents = drawnComponentStateStack.undoLastCanvasChange(drawnComponents);
        currentCanvasState.redrawCanvas();
    }
}

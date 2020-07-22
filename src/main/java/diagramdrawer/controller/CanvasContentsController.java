package diagramdrawer.controller;

import diagramdrawer.controller.canvasstate.CanvasState;
import diagramdrawer.controller.canvasstate.SelectComponentState;
import diagramdrawer.model.DrawableComponent;
import javafx.scene.canvas.Canvas;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Stack;

public class CanvasContentsController {
    //the components drawn on the canvas
    @Getter @Setter
    private ArrayList<DrawableComponent> drawnComponents;
    //the DrawableComponent currently highlighted
    @Getter @Setter
    private DrawableComponent highlightedComponent;
    //a stack to allow actions to be undone
    private Stack<ArrayList<DrawableComponent>> drawnComponentStateQueue;
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
        drawnComponentStateQueue = new Stack<>();
        drawnComponentStateQueue.add(new ArrayList<>());

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

    /**push the current contents of the canvas onto the stack of canvas states*/
    public void updateStateStack(){
        ArrayList<DrawableComponent> newStackState = new ArrayList<>();
        drawnComponents.forEach(component -> newStackState.add(component.createCopy()));
        drawnComponentStateQueue.push(newStackState);
    }

    /**
     * undo the last update made to the list of drawn components
     */
    public void undoLastAction(){
        if(drawnComponentStateQueue.size() > 0) {
            ArrayList<DrawableComponent> newComponentSet;
            do{
                newComponentSet = new ArrayList<>(drawnComponentStateQueue.pop());
            }while(newComponentSet.equals(drawnComponents) && drawnComponentStateQueue.size() > 0);
            drawnComponents = new ArrayList<>(newComponentSet);
            currentCanvasState.redrawCanvas();
        }
    }
}

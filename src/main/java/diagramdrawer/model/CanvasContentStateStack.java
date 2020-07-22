package diagramdrawer.model;

import diagramdrawer.model.drawablecomponent.DrawableComponent;

import java.util.ArrayList;
import java.util.Stack;

public class CanvasContentStateStack {
    //a stack to allow actions to be undone
    private Stack<ArrayList<DrawableComponent>> drawnComponentStateQueue;

    public CanvasContentStateStack(){
        drawnComponentStateQueue = new Stack<>();
        drawnComponentStateQueue.add(new ArrayList<>());
    }

    /**
     * push the current contents of the canvas onto the stack of canvas states
     *
     * @param drawnComponents the new list of DrawableComponents to push to the stack
     */
    public void updateStateStack(ArrayList<DrawableComponent> drawnComponents){
        ArrayList<DrawableComponent> newStackState = new ArrayList<>();
        drawnComponents.forEach(component -> newStackState.add(component.createCopy()));
        drawnComponentStateQueue.push(newStackState);
    }

    /**
     * undo the last update made to the list of drawn components
     *
     * @param drawnComponents the current list of DrawableComponents to push to the stack
     * @return the new list of DrawableComponenets to use as
     */
    public ArrayList<DrawableComponent> undoLastCanvasChange(ArrayList<DrawableComponent> drawnComponents){
        if(drawnComponentStateQueue.size() > 0) {
            ArrayList<DrawableComponent> newComponentSet;
            do{
                newComponentSet = new ArrayList<>(drawnComponentStateQueue.pop());
            }while(newComponentSet.equals(drawnComponents) && drawnComponentStateQueue.size() > 0);
           return new ArrayList<>(newComponentSet);
        }
        return drawnComponents;
    }
}

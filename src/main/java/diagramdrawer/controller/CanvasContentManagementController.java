package diagramdrawer.controller;

import diagramdrawer.controller.canvasstate.CanvasState;
import diagramdrawer.controller.canvasstate.SelectComponentState;
import diagramdrawer.model.CanvasContentStateStack;
import diagramdrawer.model.drawablecomponent.DrawableComponent;
import diagramdrawer.model.drawablecomponent.boxcomponent.BoxComponent;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**Keeps track of the contents of the canvas and the history of changes made to the canvas*/
public class CanvasContentManagementController {
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
    private Canvas canvas;
    //the controller to perform draw commands on the canvas
    @Getter
    private CanvasDrawController canvasDrawController;

    public CanvasContentManagementController(Canvas canvas) {
        this.canvas = canvas;
        drawnComponents = new ArrayList<>();
        highlightedComponent = null;
        drawnComponentStateStack = new CanvasContentStateStack();
        canvasDrawController = new CanvasDrawController(this);

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
        canvasDrawController.redrawCanvas();
    }

    /**
     * determines what component the passed coordinates belong to and finds a point on the edge of
     * tht component that is closest to the passed coordinates
     *
     * @param clickX the x coordinate of the point to check
     * @param clickY the y coordinate of the point to check
     * @return the closest point on the edge of the component that the passed coordinates lies inside
     */
    public Point2D findClosestPointOnComponentEdge(double clickX, double clickY){
        for(DrawableComponent component: drawnComponents){
            if(component.checkPointInBounds(clickX, clickY) && component instanceof BoxComponent) {
                BoxComponent boxComponent = (BoxComponent) component;
                double topY = boxComponent.getCenterY() - (boxComponent.getHeight() / 2);
                double bottomY = topY + boxComponent.getHeight();
                double leftX = boxComponent.getCenterX() - (boxComponent.getWidth() / 2);
                double rightX = leftX + boxComponent.getWidth();

                if (checkIfFirstPairClosest(clickX, leftX, rightX, clickY, topY, bottomY)) {
                    //the click is closest to the left edge
                    return new Point2D(leftX, clickY);
                } else if (checkIfFirstPairClosest(clickX, rightX, leftX, clickY, topY, bottomY)) {
                    //the click is closest to the right edge
                    return new Point2D(rightX, clickY);
                } else if (checkIfFirstPairClosest(clickY, topY, bottomY, clickX, leftX, rightX)) {
                    //the click is closest to the top edge
                    return new Point2D(clickX, topY);
                } else {
                    //the click is closest to the bottom edge
                    return new Point2D(clickX, bottomY);
                }
            }
        }
        return new Point2D(clickX, clickY);
    }

    /**
     * compares each combination of the 6 values passed to check if the first
     * two values are more similar than any other combination of the values
     *
     * @param d1 the first value
     * @param d2 the second value
     * @param d3 the third value
     * @param d4 the fourth value
     * @param d5 the fifth value
     * @param d6 the sixth value
     * @return true if the first two values are closer to each other than any othe combination of the passed values
     */
    private boolean checkIfFirstPairClosest(
            double d1, double d2, double d3, double d4, double  d5, double d6){
        return (checkIfFirstCoordinateCloser(d1, d2, d1, d3) &&
                checkIfFirstCoordinateCloser(d1, d2, d4, d5) &&
                checkIfFirstCoordinateCloser(d1, d2, d4, d6));
    }

    /**
     * checks if the first two numbers provided are closer than the second to numbers provided
     *
     * @param source the first number used in the first comparison
     * @param first the second number used in the first comparison
     * @param source2 the first number used in the second comparison
     * @param second the second number used in the second comparison
     * @return true if source and first are close together than source2 and second, otherwise false
     */
    private boolean checkIfFirstCoordinateCloser(double source, double first, double source2, double second){
        return Math.abs(source - first) < Math.abs(source2 - second);
    }
}

package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.CanvasContentManagementController;
import diagramdrawer.model.drawablecomponent.Connection;
import diagramdrawer.model.drawablecomponent.DrawableComponent;
import diagramdrawer.model.drawablecomponent.boxcomponent.BoxComponent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javafx.geometry.Point2D;

public class AddConnectionState extends CanvasState {
    //the new connection to draw on the canvas
    Connection newConnection;

    /**
     * Constructor
     *
     * @param canvasContentManagementController the controller for the main window using this state
     * @param newConnection                      the new component to draw on the canvas
     */
    public AddConnectionState(CanvasContentManagementController canvasContentManagementController, Connection newConnection) {
        super(canvasContentManagementController);
        this.newConnection = newConnection;
        newConnection.setStart(findPointOnComponentEdge(newConnection.getStart().getX(), newConnection.getStart().getY()));
    }

    @Override
    public void exitState() {
        canvasContentManagementController.setCurrentCanvasState(new SelectComponentState(canvasContentManagementController));
    }

    @Override
    public void enterState() {
        super.enterState();
        canvasContentManagementController.setHighlightedComponent(null);
    }

    @Override
    public void mousePressedHandler(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseButton.SECONDARY){
            // a right click indicates cancelling the new component addition
            exitState();
        } else if(mouseEvent.getButton() == MouseButton.PRIMARY){
            //draw the component on the canvas
            newConnection.setEnd(findPointOnComponentEdge(mouseEvent.getX(), mouseEvent.getY()));
            canvasContentManagementController.getCanvasDrawController().drawFinalComponent(newConnection);
            canvasContentManagementController.addComponent(newConnection);
            exitState();
        }
    }

    @Override
    public void mouseMoveHandler(MouseEvent mouseEvent) {
        //draw a preview of where the component will be drawn on the canvas
        newConnection.setEnd(new Point2D(mouseEvent.getX(), mouseEvent.getY()));
        canvasContentManagementController.getCanvasDrawController().drawPreviewComponent(newConnection);
    }

    /**
     * determines what component the passed coordinates belong to and finds a point on the edge of
     * tht component that is closest to the passed coordinates
     *
     * @param clickX the x coordinate of the point to check
     * @param clickY the y coordinate of the point to check
     * @return the closest point on the edge of the component that the passed coordinates lies inside
     */
    private Point2D findPointOnComponentEdge(double clickX, double clickY){
        for(DrawableComponent component: canvasContentManagementController.getDrawnComponents()){
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

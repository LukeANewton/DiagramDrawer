package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.CanvasContentManagementController;
import diagramdrawer.model.drawablecomponent.Connection;
import diagramdrawer.model.drawablecomponent.DrawableComponent;
import diagramdrawer.model.drawablecomponent.boxcomponent.BoxComponent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javafx.geometry.Point2D;

public class AddConnectionState extends AddComponentState {
    /**
     * Constructor
     *
     * @param canvasContentManagementController the controller for the main window using this state
     * @param newComponent                      the new component to draw on the canvas
     */
    public AddConnectionState(CanvasContentManagementController canvasContentManagementController, DrawableComponent newComponent) {
        super(canvasContentManagementController, newComponent);
        Connection connection = (Connection) newComponent;
        connection.setStart(findPointOnComponentEdge(connection.getStart().getX(), connection.getStart().getY()));
    }
    
    @Override
    public void mousePressedHandler(MouseEvent mouseEvent) {
        if(mouseEvent.getButton() == MouseButton.SECONDARY){
            // a right click indicates cancelling the new component addition
            exitState();
        } else if(mouseEvent.getButton() == MouseButton.PRIMARY){
            //draw the component on the canvas
            Point2D p = findPointOnComponentEdge(mouseEvent.getX(), mouseEvent.getY());
           // ((Connection) newComponent).setEnd(findPointOnComponentEdge(mouseEvent));
            canvasContentManagementController.getCanvasDrawController().drawFinalComponent(
                    newComponent, p.getX(), p.getY());
            canvasContentManagementController.addComponent(newComponent);
            exitState();
        }
    }

    private Point2D findPointOnComponentEdge(double clickX, double clickY){
        for(DrawableComponent component: canvasContentManagementController.getDrawnComponents()){
            if(component.checkPointInBounds(clickX, clickY)
                    && component instanceof BoxComponent) {
                BoxComponent boxComponent = (BoxComponent) component;
                double topY = boxComponent.getCenterY() - (boxComponent.getHeight() / 2);
                double bottomY = topY + boxComponent.getHeight();
                double leftX = boxComponent.getCenterX() - (boxComponent.getWidth() / 2);
                double rightX = leftX + boxComponent.getWidth();

                if (checkIfFirstCoordinateCloser(clickX, leftX, clickX, rightX) &&
                        checkIfFirstCoordinateCloser(clickX, leftX, clickY, topY) &&
                        checkIfFirstCoordinateCloser(clickX, leftX, clickY, bottomY)) {
                    //the click is closest to the left edge
                    return new Point2D(leftX, clickY);
                } else if (checkIfFirstCoordinateCloser(clickX, rightX, clickX, leftX) &&
                        checkIfFirstCoordinateCloser(clickX, rightX, clickY, topY) &&
                        checkIfFirstCoordinateCloser(clickX, rightX, clickY, bottomY)) {
                    //the click is closest to the right edge
                    return new Point2D(rightX, clickY);
                } else if (checkIfFirstCoordinateCloser(clickY, topY, clickY, bottomY) &&
                        checkIfFirstCoordinateCloser(clickY, topY, clickX, leftX) &&
                        checkIfFirstCoordinateCloser(clickY, topY, clickX, rightX)) {
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

    private boolean checkIfFirstCoordinateCloser(double source, double first, double source2, double second){
        return Math.abs(source - first) < Math.abs(source2 - second);
    }
}

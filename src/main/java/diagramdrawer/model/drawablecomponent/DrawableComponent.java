package diagramdrawer.model.drawablecomponent;

import javafx.scene.canvas.GraphicsContext;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

/**Contains the information about a component to be drawn on the application canvas, and the methods
 * to draw and edit that component*/
public abstract class DrawableComponent {

    /**
     * checks if the given point is inside the bounds of this component
     *
     * @param x the x coordinate of the point to check
     * @param y the y coordinate of the point to check
     * @return true if the point is inside the bounds of the component, otherwise false
     */
    public abstract boolean checkPointInBounds(double x, double y);

    /**
     * draws the component on the given GraphicsContext with the specified color and line thickness
     *
     * @param gc the GraphicsContext of the canvas to draw on
     * @param color the color to draw in
     * @param lineWidth the thickness of the lines to draw with
     */
    public abstract void draw(GraphicsContext gc, Color color, int lineWidth);

    /**Creates a copy of the DrawableComponent*/
    public abstract DrawableComponent createCopy();

    /**
     * Return a VBox containing the contents to populate a dialog box with to edit this object's contents
     *
     * @return a VBox containing the contents to populate a dialog box with to edit this object's contents
     */
    public abstract VBox fetchUpdateContentsDialog();

    /**Updates the contents of this DrawableComponent after closing the edit dialog*/
    public abstract void updateContents();

    public static DrawableComponent fromXML(HashMap<String, String> arguments) {
        return null;
    }

    /**
     * builds an XML tag for the calling class with fields specified in the passed hash map
     *
     * @param arguments the fields to include in the tag
     * @return a String containing an XML tag to represent the object
     */
    protected String buildXML(HashMap<String, String> arguments){
        StringBuilder s = new StringBuilder();
        s.append("<").append(this.getClass().getName());
        for(Map.Entry<String, String> entry : arguments.entrySet()) {
            s.append(" ").append(entry.getKey()).append("=\"").append(entry.getValue()).append("\"");
        }
        s.append("/>");
        return s.toString();
    }
}

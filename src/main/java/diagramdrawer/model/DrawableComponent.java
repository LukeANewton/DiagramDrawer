package diagramdrawer.model;

import javafx.scene.canvas.GraphicsContext;

import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DrawableComponent {
    protected double width;
    protected double height;
    protected double centerX;
    protected double centerY;
    protected String title;

    /**
     * Constructor
     *
     * @param title the title to write on the component
     * @param centerX the x coordinate in the center of the object to draw
     * @param centerY the y coordinate in the center of the object to draw
     * @param height the height of the object to draw
     * @param width the width of the object to draw
     */
    protected DrawableComponent(String title, double centerX, double centerY, double height, double width){
        this.title = title;
        this.centerX = centerX;
        this.centerY = centerY;
        this.height = height;
        this.width = width;
    }

    /**
     * checks if the given point is inside the bounds of this component
     *
     * @param x the x coordinate of the point to check
     * @param y the y coordinate of the point to check
     * @return true if the point is inside the bounds of the component, otherwise false
     */
    public boolean checkPointInBounds(double x, double y){
        double startX = centerX - (width / 2);
        double startY = centerY - (height / 2);

        return startX < x && startY < y && startX + width > x && startY + height > y;
    }

    /**
     * draws the component on the given GraphicsContext with the specified color and line thickness
     *
     * @param gc the GraphicsContext of the canvas to draw on
     * @param color the color to draw in
     * @param lineWidth the thickness of the lines to draw with
     */
    public abstract void draw(GraphicsContext gc, Color color, int lineWidth);
}

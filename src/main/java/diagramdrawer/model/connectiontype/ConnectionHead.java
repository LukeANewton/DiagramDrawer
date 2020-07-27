package diagramdrawer.model.connectiontype;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public interface ConnectionHead {
    /**
     * draws the head on the given GraphicsContext with the specified color and line thickness
     *
     * @param gc the GraphicsContext of the canvas to draw on
     * @param color the color to draw in
     * @param lineWidth the thickness of the lines to draw with
     */
    void drawHead(GraphicsContext gc, Color color, int lineWidth);
}

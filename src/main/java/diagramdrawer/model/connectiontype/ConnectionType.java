package diagramdrawer.model.connectiontype;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**Enum defining the possible connection types between two components*/
public enum ConnectionType {
    None(null),
    Dependency(new Dependency()),
    Association(new Association()),
    Aggregation(new Aggregation()),
    Composition(new Composition()),
    Inheritance(new Inheritance()),
    Implementation(new Implementation());

    private ConnectionHead connectionHead;

    ConnectionType(ConnectionHead connectionHead){
        this.connectionHead = connectionHead;
    }

    /**
     * draws the head on the given GraphicsContext with the specified color and line thickness
     *
     * @param gc the GraphicsContext of the canvas to draw on
     * @param color the color to draw in
     * @param lineWidth the thickness of the lines to draw with
     */
    public void drawHead(GraphicsContext gc, Color color, int lineWidth){
        if(connectionHead != null){
            connectionHead.drawHead(gc, color, lineWidth);
        }
    }
}

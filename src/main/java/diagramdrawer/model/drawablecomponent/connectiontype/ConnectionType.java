package diagramdrawer.model.drawablecomponent.connectiontype;

import diagramdrawer.model.drawablecomponent.connectiontype.connectionhead.ClosedArrow;
import diagramdrawer.model.drawablecomponent.connectiontype.connectionhead.ConnectionHead;
import diagramdrawer.model.drawablecomponent.connectiontype.connectionhead.DiamondHead;
import diagramdrawer.model.drawablecomponent.connectiontype.connectionhead.OpenArrow;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.javatuples.Pair;

/**Enum defining the possible connection types between two components*/
public enum ConnectionType {
    None(null),
    Dependency(new OpenArrow()),
    Association(new OpenArrow()),
    Aggregation(new DiamondHead(Color.WHITE)),
    Composition(new DiamondHead(Color.BLACK)),
    Inheritance(new ClosedArrow()),
    Implementation(new ClosedArrow());

    //the strategy for drawing the arrow head
    private ConnectionHead connectionHead;

    /**
     * Constructor
     *
     * @param connectionHead the strategy for drawing the arrow head
     */
    ConnectionType(ConnectionHead connectionHead){
        this.connectionHead = connectionHead;
    }

    /**
     * draws the head on the given GraphicsContext with the specified color and line thickness
     *
     * @param gc the GraphicsContext of the canvas to draw on

     */
    public void drawHead(GraphicsContext gc, Pair<Double, Double> lastPoint, Pair<Double, Double> secondLast) {
        if(connectionHead != null){
            connectionHead.drawHead(gc, lastPoint, secondLast);
        }
    }
}

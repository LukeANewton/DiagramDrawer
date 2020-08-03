package diagramdrawer.model.drawablecomponent.connectiontype;

import diagramdrawer.model.drawablecomponent.connectiontype.connectionhead.ClosedArrow;
import diagramdrawer.model.drawablecomponent.connectiontype.connectionhead.ConnectionHead;
import diagramdrawer.model.drawablecomponent.connectiontype.connectionhead.DiamondHead;
import diagramdrawer.model.drawablecomponent.connectiontype.connectionhead.OpenArrow;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;
import org.javatuples.Pair;

/**Enum defining the possible connection types between two components*/
public enum ConnectionType {
    None(null, LineStyle.SOLID_LINE),
    Dependency(new OpenArrow(), LineStyle.DASHED_LINE),
    Association(new OpenArrow(), LineStyle.SOLID_LINE),
    Aggregation(new DiamondHead(Color.WHITE), LineStyle.SOLID_LINE),
    Composition(new DiamondHead(Color.BLACK), LineStyle.SOLID_LINE),
    Inheritance(new ClosedArrow(), LineStyle.SOLID_LINE),
    Implementation(new ClosedArrow(), LineStyle.DASHED_LINE);

    //the strategy for drawing the arrow head
    private ConnectionHead connectionHead;
    //the space to be set in between line dashes (0 if line is solid)
    @Getter
    private int dashedLineGap;

    private static class LineStyle{
        private static final int DASHED_LINE = 5;
        private static final int SOLID_LINE = 0;
    }


    /**
     * Constructor
     *
     * @param connectionHead the strategy for drawing the arrow head
     */
    ConnectionType(ConnectionHead connectionHead, int dashedLineGap){
        this.connectionHead = connectionHead;
        this.dashedLineGap = dashedLineGap;
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

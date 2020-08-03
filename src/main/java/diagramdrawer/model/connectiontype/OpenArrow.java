package diagramdrawer.model.connectiontype;

import javafx.scene.canvas.GraphicsContext;
import org.javatuples.Pair;

/**Describes how to draw the head on an association relationship*/
public class OpenArrow extends ConnectionHead {
    @Override
    public void drawHead(GraphicsContext gc, Pair<Double, Double> lastPoint, Pair<Double, Double> secondLast) {
        super.drawHead(gc, lastPoint, secondLast);

        //convert angle to radians
        double theta = degreeToRadians(ROTATION_ANGLE) ;

        //shift absolute coordinates to vector from last point
        double vectorX = secondLast.getValue0() - lastPoint.getValue0();
        double vectorY = secondLast.getValue1() - lastPoint.getValue1();

        //draw clockwise
        rotateAndDraw(gc, lastPoint, vectorX, vectorY, theta);
        //draw counter-clockwise
        rotateAndDraw(gc, lastPoint, vectorX, vectorY, 2 * Math.PI - theta);


    }
}

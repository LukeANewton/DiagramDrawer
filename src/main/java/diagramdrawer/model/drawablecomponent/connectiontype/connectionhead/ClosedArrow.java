package diagramdrawer.model.drawablecomponent.connectiontype.connectionhead;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.javatuples.Pair;

/**Describes how to draw the head on an inheritance relationship*/
public class ClosedArrow extends ConnectionHead {
    @Override
    public void drawHead(GraphicsContext gc, Pair<Double, Double> lastPoint, Pair<Double, Double> secondLast) {
        super.drawHead(gc, lastPoint, secondLast);

        //shift absolute coordinates to vector from last point
        double vectorX = secondLast.getValue0() - lastPoint.getValue0();
        double vectorY = secondLast.getValue1() - lastPoint.getValue1();

        //draw clockwise
        Pair<Double, Double> arrowEndC = rotateAndDraw(gc, lastPoint, vectorX, vectorY, ROTATION_ANGLE_RADIANS);
        //draw counter-clockwise
        Pair<Double, Double> arrowEndCC = rotateAndDraw(gc, lastPoint, vectorX, vectorY, 2 * Math.PI - ROTATION_ANGLE_RADIANS);

        //draw bottom line
        gc.strokeLine(arrowEndC.getValue0(), arrowEndC.getValue1(), arrowEndCC.getValue0(), arrowEndCC.getValue1());
        //remove connection line up the middle
        gc.setFill(Color.WHITE);
        double [] pointsX = new double[]{arrowEndC.getValue0(), arrowEndCC.getValue0(), lastPoint.getValue0()};
        double [] pointsY = new double[]{arrowEndC.getValue1(), arrowEndCC.getValue1(), lastPoint.getValue1()};
        gc.fillPolygon(pointsX, pointsY,3);
        gc.strokePolygon(pointsX, pointsY,3);
    }
}

package diagramdrawer.model.drawablecomponent.connectiontype.connectionhead;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.javatuples.Pair;

/**Describes how to draw a diamond-shaped arrowhead*/
public class DiamondHead extends ConnectionHead {
    //the color to use to fill the diamond arrow head
    private Color diamondFill;

    /**
     * Constructor
     *
     * @param diamondFill the color to use to fill the diamond arrow head
     */
    public DiamondHead(Color diamondFill){
        this.diamondFill = diamondFill;
    }

    @Override
    public void drawHead(GraphicsContext gc, Pair<Double, Double> lastPoint, Pair<Double, Double> secondLast) {
        super.drawHead(gc, lastPoint, secondLast);

        //shift absolute coordinates to vector from last point
        double vectorX = secondLast.getValue0() - lastPoint.getValue0();
        double vectorY = secondLast.getValue1() - lastPoint.getValue1();

        //get clockwise side point
        Pair<Double, Double> arrowEndC = rotateAndDraw(gc, lastPoint, vectorX, vectorY, ROTATION_ANGLE_RADIANS);
        //get counter-clockwise side point
        Pair<Double, Double> arrowEndCC = rotateAndDraw(gc, lastPoint, vectorX, vectorY, 2 * Math.PI - ROTATION_ANGLE_RADIANS);

        //get the point at the back of the arrow rotating one arrow head tip further
        vectorX = lastPoint.getValue0() - arrowEndC.getValue0();
        vectorY = lastPoint.getValue1() - arrowEndC.getValue1();
        Pair<Double, Double> diamondBack = rotateAndDraw(gc, arrowEndC, vectorX, vectorY,
                //diamonds are parallelograms: adjacent angles add to 180 degrees
                Math.PI - 2*ROTATION_ANGLE_RADIANS);
        gc.setFill(diamondFill);
        double [] pointsX = new double[]{lastPoint.getValue0(), arrowEndC.getValue0(),
                diamondBack.getValue0(), arrowEndCC.getValue0()};
        double [] pointsY = new double[]{lastPoint.getValue1(), arrowEndC.getValue1(),
                diamondBack.getValue1(), arrowEndCC.getValue1()};

        gc.fillPolygon(pointsX, pointsY, pointsX.length);
        gc.strokePolygon(pointsX, pointsY, pointsX.length);
    }
}

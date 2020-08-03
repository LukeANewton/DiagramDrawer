package diagramdrawer.model.connectiontype;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.javatuples.Pair;

/**Describes how to draw the head on an composition relationship*/
public class DiamondHead extends ConnectionHead {
    private Color diamondFill;

    public DiamondHead(Color diamondFill){
        this.diamondFill = diamondFill;
    }

    @Override
    public void drawHead(GraphicsContext gc, Pair<Double, Double> lastPoint, Pair<Double, Double> secondLast) {
        super.drawHead(gc, lastPoint, secondLast);

        //convert angle to radians
        double theta = degreeToRadians(ROTATION_ANGLE) ;

        //shift absolute coordinates to vector from last point
        double vectorX = secondLast.getValue0() - lastPoint.getValue0();
        double vectorY = secondLast.getValue1() - lastPoint.getValue1();

        //get clockwise side point
        Pair<Double, Double> arrowEndC = rotateAndDraw(gc, lastPoint, vectorX, vectorY, theta);
        //get counter-clockwise side point
        Pair<Double, Double> arrowEndCC = rotateAndDraw(gc, lastPoint, vectorX, vectorY, 2 * Math.PI - theta);

        /*determine point on other side of diamond by determining how long the diamond must be,
        then a bisection search along the line to find the right coordinates*/
        double m = (lastPoint.getValue1() - secondLast.getValue1())/(lastPoint.getValue0() - secondLast.getValue0());
        double b = lastPoint.getValue1() - m * lastPoint.getValue0();

        double diamondLength = Math.cos(theta) * ARROW_HEAD_LENGTH * 2;
        double difference = 0.5;
        double base = secondLast.getValue0();
        double end = lastPoint.getValue0();
        double distance;
        do{
            double x = (end + base) / 2;

            double xDiff = lastPoint.getValue0() - x;
            double yDiff = lastPoint.getValue1() - (m*x+b);

            distance = Math.sqrt(Math.pow(xDiff,2)+Math.pow(yDiff,2));

            if(Math.abs(distance-diamondLength) < difference){//the point found is close enough to draw an accurate diamond
                Pair<Double, Double> diamondBack = new Pair<>(x,  m * x + b);
                gc.setFill(diamondFill);
                double [] pointsX = new double[]{lastPoint.getValue0(), arrowEndC.getValue0(),
                        diamondBack.getValue0(), arrowEndCC.getValue0()};
                double [] pointsY = new double[]{lastPoint.getValue1(), arrowEndC.getValue1(),
                        diamondBack.getValue1(), arrowEndCC.getValue1()};
                gc.fillPolygon(pointsX, pointsY, pointsX.length);
                gc.strokePolygon(pointsX, pointsY, pointsX.length);
                break;
            } else if(distance > diamondLength){
                base = x;
            }else{
                end = x;
            }
        }while(Math.abs(distance-diamondLength) > difference);
    }
}

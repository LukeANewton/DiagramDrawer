package diagramdrawer.model.drawablecomponent.connectiontype.connectionhead;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.javatuples.Pair;

/** Contains the strategy for drawing a specific type of arrowhead*/
public abstract class ConnectionHead {
    //the angle to place between the arrow line and sides of the arrow head
    protected static final double ROTATION_ANGLE_DEGREES = 30;
    protected static final double ROTATION_ANGLE_RADIANS = ROTATION_ANGLE_DEGREES * Math.PI / 180;
    //the length to make each side of the arrow head
    protected static final double ARROW_HEAD_LENGTH = 15;

    /**
     * draws the head on the given GraphicsContext with the specified color and line thickness
     *
     * @param gc the GraphicsContext of the canvas to draw on
     */
    public void drawHead(GraphicsContext gc, Pair<Double, Double> lastPoint, Pair<Double, Double> secondLast){
         gc.setStroke(Color.BLACK);
         gc.setLineWidth(1);
     }

    /**
     * draws on side of an arrowhead and returns the coordinates of the end of the arrowhead side
     *
     * @param gc the GraphicContext to use to draw
     * @param lastPoint the point at the tip of the arrow head to be drawn
     * @param vectorX the x component of the vector form of the connection line
     * @param vectorY the y component of the vector form of the connection line
     * @param theta the angle between the arrow line and arrowhead sides
     * @return the coordinates of the tip of the arrowhead side drawn
     */
    protected Pair<Double, Double> rotateAndDraw(GraphicsContext gc, Pair<Double, Double> lastPoint,
                                                 double vectorX, double vectorY, double theta){
        //rotate vector clockwise between points by rotation matrix
        double rotatedVectorX = vectorX * Math.cos(theta) - vectorY * Math.sin(theta);
        double rotatedVectorY = vectorX * Math.sin(theta) + vectorY * Math.cos(theta);
        //normalize vectors
        double vectorLength = Math.sqrt(Math.pow(rotatedVectorX, 2) + Math.pow(rotatedVectorY, 2));
        rotatedVectorX /= vectorLength;
        rotatedVectorY /= vectorLength;
        //set vector length
        rotatedVectorX *= ARROW_HEAD_LENGTH;
        rotatedVectorY *= ARROW_HEAD_LENGTH;
        //shift vector back to absolute coordinates
        double rotatedX = rotatedVectorX + lastPoint.getValue0();
        double rotatedY = rotatedVectorY + lastPoint.getValue1();

        //draw line
        gc.strokeLine(lastPoint.getValue0(), lastPoint.getValue1(), rotatedX, rotatedY);

        return new Pair<>(rotatedX, rotatedY);
    }
}

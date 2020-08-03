package diagramdrawer.model.connectiontype.connectionhead;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.javatuples.Pair;

public abstract class ConnectionHead {
    protected static final double ROTATION_ANGLE_DEGREES = 30;
    protected static final double ROTATION_ANGLE_RADIANS = ROTATION_ANGLE_DEGREES * Math.PI / 180;
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

    protected Pair<Double, Double> rotateAndDraw(GraphicsContext gc, Pair<Double, Double> lastPoint, double vectorX, double vectorY, double theta){
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

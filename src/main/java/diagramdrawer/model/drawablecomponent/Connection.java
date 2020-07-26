package diagramdrawer.model.drawablecomponent;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Connection extends DrawableComponent {
    private Point2D start;
    private Point2D end;

    public Connection(double startX, double startY){
        this.start = new Point2D(startX, startY);
    }

    public Connection(double startX, double startY, double endX, double endY){
        this.start = new Point2D(startX, startY);
        this.end = new Point2D(endX, endY);
    }

    @Override
    public boolean checkPointInBounds(double x, double y) {
        //build a string line through between the points
        double m = (end.getY() - start.getY()) / (end.getX() - start.getX());
        double b = start.getY() - m * start.getX();
        //since the lines are so thin, we'll give some wiggle room: 5 pixels on either side
        return Math.abs(m*x + b - y) <= 5;
    }

    @Override
    public void draw(GraphicsContext gc, Color color, int lineWidth) {
        gc.setLineWidth(lineWidth);
        gc.setStroke(color);
        gc.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
    }

    @Override
    public DrawableComponent createCopy() {
        return new Connection(this.start.getX(), this.start.getY(), this.end.getX(), this.end.getY());
    }

    @Override
    public VBox getUpdateContentsDialog() {
        return new VBox();
    }

    @Override
    public void updateContents() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return start.getX() == that.start.getX() && start.getY() == that.start.getY()
                && end.getX() == that.end.getX() && end.getY() == that.end.getY();
    }
}

package diagramdrawer.model;

import javafx.scene.canvas.GraphicsContext;

import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class DrawableComponent {
    protected int width;
    protected int height;
    protected double centerX;
    protected double centerY;
    protected String title;

    protected DrawableComponent(String title, double centerX, double centerY, int height, int width){
        this.title = title;
        this.centerX = centerX;
        this.centerY = centerY;
        this.height = height;
        this.width = width;
    }

    public boolean checkPointInBounds(double x, double y){
        double startX = centerX - (width >> 1);
        double startY = centerY - (height >> 1);

        return startX < x && startY < y && startX + width > x && startY + height > y;
    }

    public abstract void draw(GraphicsContext gc, Color color, int lineWidth);
}

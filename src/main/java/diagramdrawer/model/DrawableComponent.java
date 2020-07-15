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
    protected double startX;
    protected double startY;
    protected String title;

    protected DrawableComponent(String title, double centerX, double centerY, int height, int width){
        this.title = title;
        this.startX = centerX - (width >> 1);
        this.startY = centerY - (height >> 1);
        this.height = height;
        this.width = width;
    }

    public abstract void draw(GraphicsContext gc, Color color, int lineWidth);
}

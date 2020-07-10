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
    protected int startX;
    protected int startY;

    public DrawableComponent(int startX, int startY, int height, int width){
        this.startX = startX;
        this.startY = startY;
        this.height = height;
        this.width = width;
    }

    public abstract void draw(GraphicsContext gc, Color color);
}

package diagramdrawer.model;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleSectionClass extends DrawableComponent{
    private String title;

    public SingleSectionClass(String title, int startX, int startY, int height, int width){
        super(startX, startY, height, width);
        this.title = title;
    }

    @Override
    public void draw(GraphicsContext gc, Color color) {
        //draw shape
        gc.setStroke(color);
        gc.setLineWidth(1);
        gc.strokeLine(startX, startY, startX, startY + height);
        gc.strokeLine(startX, startY + height, startX + width, startY + height);
        gc.strokeLine(startX + width, startY + height, startX + width, startY);
        gc.strokeLine(startX + width, startY, startX, startY);

        //center text in box by creating a throwaway scene to get text size
        final Text throwaway = new Text(title);
        new Scene(new Group(throwaway));
        gc.fillText(title, startX + (width/2) - (throwaway.getLayoutBounds().getWidth()/2),
                startY + (height/2) + (throwaway.getLayoutBounds().getHeight()/4));
    }
}

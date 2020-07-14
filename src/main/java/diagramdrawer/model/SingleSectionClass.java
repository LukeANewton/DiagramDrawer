package diagramdrawer.model;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class SingleSectionClass extends DrawableComponent{

    public SingleSectionClass(String title, int startX, int startY, int height, int width){
        super(title, startX, startY, height, width);
    }

    @Override
    public void draw(GraphicsContext gc, Color color, int lineWidth) {
        //draw shape
        gc.setStroke(color);
        gc.setLineWidth(lineWidth);
        gc.strokeLine(startX, startY, startX, startY + height);
        gc.strokeLine(startX, startY + height, startX + width, startY + height);
        gc.strokeLine(startX + width, startY + height, startX + width, startY);
        gc.strokeLine(startX + width, startY, startX, startY);

        //center text in box by creating a throwaway scene to get text size
        final Text throwaway = new Text(title);
        new Scene(new Group(throwaway));
        gc.fillText(title, startX + (width >> 1) - (throwaway.getLayoutBounds().getWidth()/2),
                startY + (height >> 1) + (throwaway.getLayoutBounds().getHeight()/4));
    }
}

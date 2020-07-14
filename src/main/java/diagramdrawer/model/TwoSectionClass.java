package diagramdrawer.model;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwoSectionClass extends DrawableComponent {
    private String sectionContents;

    public TwoSectionClass(String title, int startX, int startY, int height, int width) {
        super(title, startX, startY, height, width);
        sectionContents = "";
    }

    @Override
    public void draw(GraphicsContext gc, Color color, int lineWidth) {
        //draw surrounding box
        gc.setStroke(color);
        gc.setLineWidth(lineWidth);
        gc.strokeLine(startX, startY, startX, startY + height);
        gc.strokeLine(startX, startY + height, startX + width, startY + height);
        gc.strokeLine(startX + width, startY + height, startX + width, startY);
        gc.strokeLine(startX + width, startY, startX, startY);

        //determine height of title to determine where section divider should go and to center text
        Text throwaway = new Text(title);
        new Scene(new Group(throwaway));

        //center text in box by creating a throwaway scene to get text size
        throwaway = new Text(title);
        new Scene(new Group(throwaway));
        gc.fillText(title, startX + (width >> 1) - (throwaway.getLayoutBounds().getWidth()/2),
                startY + throwaway.getLayoutBounds().getHeight());

        //draw line underneath title to divide sections
        //height of the text plus 5 padding on top and bottom
        double dividerYcoord = startY + throwaway.getLayoutBounds().getHeight() + 10;
        gc.strokeLine(startX, dividerYcoord, startX + width, dividerYcoord);
    }
}

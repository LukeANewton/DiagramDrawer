package diagramdrawer.model;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class SingleSectionClass extends DrawableComponent{

    //default sizes for newly created components
    private static final int DEFAULT_SINGLE_SECTION_BOX_HEIGHT = 50;
    private static final int DEFAULT_SINGLE_SECTION_BOX_WIDTH = 100;

    /**constructor*/
    public SingleSectionClass(){
        super("Class", 0, 0, DEFAULT_SINGLE_SECTION_BOX_HEIGHT, DEFAULT_SINGLE_SECTION_BOX_WIDTH);
    }

    /**
     * Constructor
     *
     * @param title the title to write on the component
     * @param centerX the x coordinate in the center of the object to draw
     * @param centerY the y coordinate in the center of the object to draw
     */
    public SingleSectionClass(String title, double centerX, double centerY){
        super(title, centerX, centerY, DEFAULT_SINGLE_SECTION_BOX_HEIGHT, DEFAULT_SINGLE_SECTION_BOX_WIDTH);
    }

    @Override
    public void draw(GraphicsContext gc, Color color, int lineWidth) {
        double startX = centerX - (width / 2);
        double startY = centerY - (height / 2);

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
        gc.fillText(title, startX + (width / 2) - (throwaway.getLayoutBounds().getWidth()/2),
                startY + (height / 2) + (throwaway.getLayoutBounds().getHeight()/4));
    }
}

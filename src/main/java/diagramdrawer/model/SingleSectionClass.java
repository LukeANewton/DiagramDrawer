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

        //get size of text by creating a throwaway scene to get text size
        final Text throwaway = new Text(title);
        new Scene(new Group(throwaway));
        double titleWidth = throwaway.getLayoutBounds().getWidth();
        double titleHeight = throwaway.getLayoutBounds().getHeight();

        //draw outside box
        super.draw(gc, color, lineWidth);

        //center text in box
        gc.fillText(title, startX + (width / 2) - (titleWidth/2),
                startY + (height / 2) + (titleHeight/4));
    }
}

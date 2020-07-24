package diagramdrawer.model.drawablecomponent;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**Represents a class box with a single section for the class name*/
public class SingleSectionClassBox extends BoxComponent{
    //default sizes for newly created components
    private static final int DEFAULT_SINGLE_SECTION_BOX_HEIGHT = 50;
    private static final int DEFAULT_SINGLE_SECTION_BOX_WIDTH = 100;

    /**constructor*/
    public SingleSectionClassBox(){
        super("Class", 0, 0, DEFAULT_SINGLE_SECTION_BOX_HEIGHT, DEFAULT_SINGLE_SECTION_BOX_WIDTH);
    }

    /**
     * Constructor
     *
     * @param title the title to write on the component
     * @param centerX the x coordinate in the center of the object to draw
     * @param centerY the y coordinate in the center of the object to draw
     * @param height the height of the box
     * @param width  the width of the box
     */
    public SingleSectionClassBox(String title, double centerX, double centerY, double height, double width){
        super(title, centerX, centerY, height, width);
    }

    @Override
    public void draw(GraphicsContext gc, Color color, int lineWidth) {
        //get the top left of the component to draw
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

    @Override
    public DrawableComponent createCopy() {
        return new SingleSectionClassBox(this.title, this.centerX, this.centerY, this.height, this.width);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleSectionClassBox that = (SingleSectionClassBox) o;
        return title.equals(that.title) && centerX == that.getCenterX() && centerY == that.centerY
                && height == that.height && width == that.width;
    }
}
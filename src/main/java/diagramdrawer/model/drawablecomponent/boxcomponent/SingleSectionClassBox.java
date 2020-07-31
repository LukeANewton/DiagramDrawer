package diagramdrawer.model.drawablecomponent.boxcomponent;

import diagramdrawer.model.drawablecomponent.DrawableComponent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;

/**Represents a class box with a single section for the class name*/
public class SingleSectionClassBox extends BoxComponent {
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
    protected double getTitleYCoord(){
        final Text throwaway = new Text(title);
        new Scene(new Group(throwaway));
        return centerY + (throwaway.getLayoutBounds().getHeight()/4);
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

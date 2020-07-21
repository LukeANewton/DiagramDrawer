package diagramdrawer.model;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class TwoSectionClass extends DrawableComponent {
    private String sectionContents;

    //default sizes for newly created components
    private static final int DEFAULT_TWO_SECTION_BOX_HEIGHT = 80;
    private static final int DEFAULT_TWO_SECTION_BOX_WIDTH = 100;

    /**Constructor*/
    public TwoSectionClass(){
        super("Class", 0, 0, DEFAULT_TWO_SECTION_BOX_HEIGHT, DEFAULT_TWO_SECTION_BOX_WIDTH);
        sectionContents = "";
    }

    /**
     * Constructor
     *
     * @param title the title to write on the component
     * @param centerX the x coordinate in the center of the object to draw
     * @param centerY the y coordinate in the center of the object to draw
     * @param height  the height of the box
     * @param width the width of the box
     * @param contents the contents to write into the second section
     */
    public TwoSectionClass(String title, String contents, double centerX, double centerY, double height, double width) {
        super(title, centerX, centerY, height, width);
        sectionContents = contents;
    }

    @Override
    public void draw(GraphicsContext gc, Color color, int lineWidth) {
        double startX = centerX - (width / 2);
        double startY = centerY - (height / 2);

        //draw outside box
        super.draw(gc, color, lineWidth);

        //center text in box by creating a throwaway scene to get text size
        Text throwaway = new Text(title);
        new Scene(new Group(throwaway));
        gc.fillText(title, startX + (width / 2) - (throwaway.getLayoutBounds().getWidth()/2),
                startY + throwaway.getLayoutBounds().getHeight());

        //draw line underneath title to divide sections
        //height of the text plus 5 padding on top and bottom
        double dividerYcoord = startY + throwaway.getLayoutBounds().getHeight() + 10;
        if(dividerYcoord < startY + height) {
            gc.strokeLine(startX, dividerYcoord, startX + width, dividerYcoord);
        }
    }

    @Override
    public DrawableComponent createCopy() {
        return new TwoSectionClass(this.title, this.sectionContents, this.centerX, this.centerY, this.height, this.width);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TwoSectionClass that = (TwoSectionClass) o;
        return title.equals(that.title) && sectionContents.equals(that.getSectionContents())
                && centerX == that.getCenterX() && centerY == that.centerY && height == that.height
                && width == that.width;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectionContents);
    }
}

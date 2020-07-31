package diagramdrawer.model.drawablecomponent.boxcomponent;

import diagramdrawer.model.drawablecomponent.DrawableComponent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

/**Represents a class box that contains two sections: one for the class title, and one for text information*/
public class TwoSectionClassBox extends BoxComponent {
    //the text area used to edit the component's textual contents
    private TextArea contentsTextArea;
    //the textual contents of the class box
    @Getter @Setter
    private String sectionContents;

    //default sizes for newly created components
    private static final int DEFAULT_TWO_SECTION_BOX_HEIGHT = 80;
    private static final int DEFAULT_TWO_SECTION_BOX_WIDTH = 100;

    /**Constructor*/
    public TwoSectionClassBox(){
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
    public TwoSectionClassBox(String title, String contents, double centerX, double centerY, double height, double width) {
        super(title, centerX, centerY, height, width);
        sectionContents = contents;
    }

    @Override
    public void draw(GraphicsContext gc, Color color, int lineWidth) {
        //get the top left of the class box to draw
        double startX = centerX - (width / 2);
        double startY = centerY - (height / 2);

        //draw outside box and title
        super.draw(gc, color, lineWidth);

        //get the height of text to position the section divider from a throwaway scene
        Text throwaway = new Text(title);
        new Scene(new Group(throwaway));

        //draw line underneath title to divide sections
        //height of the title plus 5 padding on top and bottom
        double dividerYcoord = startY + throwaway.getLayoutBounds().getHeight() + 10;
        if(dividerYcoord < startY + height) {
            gc.strokeLine(startX, dividerYcoord, startX + width, dividerYcoord);
        }

        //draw text for section contents
        gc.fillText(sectionContents, startX + 10, dividerYcoord + throwaway.getLayoutBounds().getHeight());
    }

    @Override
    protected double getTitleYCoord(){
        final Text throwaway = new Text(title);
        new Scene(new Group(throwaway));
        return centerY - (height / 2) + throwaway.getLayoutBounds().getHeight();
    }

    @Override
    public DrawableComponent createCopy() {
        return new TwoSectionClassBox(this.title, this.sectionContents, this.centerX, this.centerY, this.height, this.width);
    }

    @Override
    public VBox fetchUpdateContentsDialog() {
        /* VBox containing two HBoxes: each with a label and text box.
        The first is used to update the class title. The second is used to update the contents*/
        VBox vbox = super.fetchUpdateContentsDialog();
        HBox hbox = new HBox();
        Label contentsLabel = new Label("Contents: ");
        contentsTextArea = new TextArea(sectionContents);
        hbox.getChildren().add(contentsLabel);
        hbox.getChildren().add(contentsTextArea);
        vbox.getChildren().add(hbox);
        return vbox;
    }

    @Override
    public void updateContents() {
        super.updateContents();
        sectionContents = contentsTextArea.getText();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TwoSectionClassBox that = (TwoSectionClassBox) o;
        return title.equals(that.title) && sectionContents.equals(that.getSectionContents())
                && centerX == that.getCenterX() && centerY == that.centerY && height == that.height
                && width == that.width;
    }
}

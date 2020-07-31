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

/**Represents a class box that contains three sections: for the class title, fields, and methods*/
public class ThreeSectionClassBox extends BoxComponent {
    //the text area used to edit the component's fields
    private TextArea fieldTextArea;
    //the text area used to edit the component's fields
    private TextArea methodTextArea;
    //the fields of the class box
    @Getter @Setter
    private String fields;
    //the methods of the class box
    @Getter @Setter
    private String methods;

    //default sizes for newly created components
    private static final int DEFAULT_TWO_SECTION_BOX_HEIGHT = 80;
    private static final int DEFAULT_TWO_SECTION_BOX_WIDTH = 100;

    /**Constructor*/
    public ThreeSectionClassBox(){
        super("Class", 0, 0, DEFAULT_TWO_SECTION_BOX_HEIGHT, DEFAULT_TWO_SECTION_BOX_WIDTH);
        fields = "";
        methods ="";
    }

    /**
     * Constructor
     *
     * @param title the title to write on the component
     * @param centerX the x coordinate in the center of the object to draw
     * @param centerY the y coordinate in the center of the object to draw
     * @param height  the height of the box
     * @param width the width of the box
     * @param fields the contents to write into the second section
     * @param methods the contents to write into the third section
     */
    public ThreeSectionClassBox(String title, String fields, String methods,
                                double centerX, double centerY, double height, double width) {
        super(title, centerX, centerY, height, width);
        this.fields = fields;
        this.methods = methods;
    }

    @Override
    public void draw(GraphicsContext gc, Color color, int lineWidth) {
        //get the top left of the class box to draw
        double startX = centerX - (width / 2);
        double startY = centerY - (height / 2);

        //draw outside box
        super.draw(gc, color, lineWidth);

        //draw first divider and field text
        double topOfFieldSection = drawDividerAndText(gc, title, fields, startX, startY);
        drawDividerAndText(gc, fields, methods, startX, topOfFieldSection);
    }

    /**
     * draws a horizontal line as a section divider and draws the text for that section
     *
     * @param gc the GraphicsContext of the canvas to draw on
     * @param textInPreviousSection the text in the previous section to calculate offset for divider
     * @param textToDraw the text to draw
     * @param startX the x coordinate of the left side of the component
     * @param topOfPreviousSection the y coordinate of the top of the previous section of the component
     * @return th y coorinate of the top of the newly drawn section
     */
    private double drawDividerAndText(GraphicsContext gc, String textInPreviousSection, String textToDraw,
                                      double startX, double topOfPreviousSection){
        Text throwaway = new Text(textInPreviousSection);
        new Scene(new Group(throwaway));
        double topOfThisSection = topOfPreviousSection + throwaway.getLayoutBounds().getHeight() + 10;
        //only draw the divider and text if the start of the section would be before the bottom of the component
        if(topOfThisSection < centerY - (height / 2) + height) {
            gc.strokeLine(startX, topOfThisSection, startX + width, topOfThisSection);
            gc.fillText(textToDraw, startX + 10, topOfThisSection + 17);
        }
        return topOfThisSection;
    }

    @Override
    protected double getTitleYCoord(){
        final Text throwaway = new Text(title);
        new Scene(new Group(throwaway));
        return centerY - (height / 2) + throwaway.getLayoutBounds().getHeight();
    }

    @Override
    public DrawableComponent createCopy() {
        return new ThreeSectionClassBox(this.title, this.fields, this.methods,
                this.centerX, this.centerY, this.height, this.width);
    }

    @Override
    public VBox fetchUpdateContentsDialog() {
        /* VBox containing three HBoxes: each with a label and text box.
        The first is used to update the class title. The second is used to update the fields.
        The third is used to update the methods*/
        VBox vbox = super.fetchUpdateContentsDialog();
        HBox hbox = new HBox();
        HBox hbox2 = new HBox();
        Label fieldLabel = new Label("Fields: ");
        Label methodLabel = new Label("Methods: ");
        fieldTextArea = new TextArea(fields);
        methodTextArea = new TextArea(methods);
        hbox.getChildren().add(fieldLabel);
        hbox.getChildren().add(fieldTextArea);
        hbox2.getChildren().add(methodLabel);
        hbox2.getChildren().add(methodTextArea);
        vbox.getChildren().add(hbox);
        vbox.getChildren().add(hbox2);
        return vbox;
    }

    @Override
    public void updateContents() {
        super.updateContents();
        fields = fieldTextArea.getText();
        methods = methodTextArea.getText().trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThreeSectionClassBox that = (ThreeSectionClassBox) o;
        return title.equals(that.title) && fields.equals(that.getFields()) && methods.equals(that.getMethods())
                && centerX == that.getCenterX() && centerY == that.centerY && height == that.height
                && width == that.width;
    }
}

package diagramdrawer.model.drawablecomponent;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;

/**Represents a class box that contains three sections: for the class title, fields, and methods*/
public class ThreeSectionClassBox extends DrawableComponent{
    //the text field used to edit the component's title
    private TextField titleTextField;
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

        //center title text in box by creating a throwaway scene to get text size
        Text throwaway = new Text(title);
        new Scene(new Group(throwaway));
        gc.fillText(title, startX + (width / 2) - (throwaway.getLayoutBounds().getWidth()/2),
                startY + throwaway.getLayoutBounds().getHeight());

        //draw line to divide title and fields
        //height of the text plus 5 padding on top and bottom
        double dividerYcoord = startY + throwaway.getLayoutBounds().getHeight() + 10;
        if(dividerYcoord < startY + height) {
            gc.strokeLine(startX, dividerYcoord, startX + width, dividerYcoord);
            //draw text for fields
            gc.fillText(fields, startX + 10, dividerYcoord + throwaway.getLayoutBounds().getHeight());
        }

        //draw line to divide field and methods
        throwaway = new Text(fields);
        new Scene(new Group(throwaway));
        dividerYcoord = dividerYcoord + throwaway.getLayoutBounds().getHeight() + 10;
        if(dividerYcoord < startY + height) {
            gc.strokeLine(startX, dividerYcoord, startX + width, dividerYcoord);
            //draw text for methods
            gc.fillText(methods, startX + 10, dividerYcoord + 17);
        }
    }


    @Override
    public DrawableComponent createCopy() {
        return new ThreeSectionClassBox(this.title, this.fields, this.methods,
                this.centerX, this.centerY, this.height, this.width);
    }

    @Override
    public VBox getUpdateContentsDialog() {
        /* VBox containing three HBoxes: each with a label and text box.
        The first is used to update the class title. The second is used to update the fields.
        The third is used to update the methods*/
        VBox vbox = new VBox();
        HBox hbox = new HBox();
        HBox hbox2 = new HBox();
        HBox hbox3 = new HBox();
        Label titleLabel = new Label("Title: ");
        Label fieldLabel = new Label("Fields: ");
        Label methodLabel = new Label("Methods: ");
        titleTextField = new TextField(title);
        fieldTextArea = new TextArea(fields);
        methodTextArea = new TextArea(methods);
        hbox.getChildren().add(titleLabel);
        hbox.getChildren().add(titleTextField);
        hbox2.getChildren().add(fieldLabel);
        hbox2.getChildren().add(fieldTextArea);
        hbox3.getChildren().add(methodLabel);
        hbox3.getChildren().add(methodTextArea);
        vbox.getChildren().add(hbox);
        vbox.getChildren().add(hbox2);
        vbox.getChildren().add(hbox3);
        titleLabel.setId("title");
        fieldLabel.setId("title");
        methodLabel.setId("title");
        vbox.setId("border");
        hbox.setId("hbox");
        hbox2.setId("hbox");
        hbox3.setId("hbox");
        return vbox;
    }

    @Override
    public void updateContents() {
        title = titleTextField.getText();
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

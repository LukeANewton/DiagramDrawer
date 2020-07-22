package diagramdrawer.model.drawablecomponent;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class SingleSectionClass extends DrawableComponent{
    private TextField textField;

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
     * @param height the height of the box
     * @param width  the width of the box
     */
    public SingleSectionClass(String title, double centerX, double centerY, double height, double width){
        super(title, centerX, centerY, height, width);
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

    @Override
    public DrawableComponent createCopy() {
        return new SingleSectionClass(this.title, this.centerX, this.centerY, this.height, this.width);
    }

    @Override
    public VBox getUpdateContentsDialog() {
        VBox vbox = new VBox();
        HBox hbox = new HBox();
        Label titleLabel = new Label("Title: ");
        textField = new TextField(title);
        hbox.getChildren().add(titleLabel);
        hbox.getChildren().add(textField);
        vbox.getChildren().add(hbox);
        titleLabel.setId("title");
        vbox.setId("border");
        hbox.setId("hbox");
        return vbox;
    }

    @Override
    public void updateContents() {
        title = textField.getText();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleSectionClass that = (SingleSectionClass) o;
        return title.equals(that.title) && centerX == that.getCenterX() && centerY == that.centerY
                && height == that.height && width == that.width;
    }
}

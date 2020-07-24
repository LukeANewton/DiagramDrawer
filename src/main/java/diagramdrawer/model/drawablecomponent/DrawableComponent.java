package diagramdrawer.model.drawablecomponent;

import javafx.scene.canvas.GraphicsContext;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

/**Contains the information about a component to be drawn on the application canvas, and the methods
 * to draw and edit that component*/
@Setter @Getter
public abstract class DrawableComponent {
    //the width of the component
    protected double width;
    //the height of the component
    protected double height;
    //the x coordinate of the center of the component
    protected double centerX;
    //the y coordinate of the center of the component
    protected double centerY;
    //the title to be written on the component
    protected String title;
    //the text field used to edit the title of the component
    protected TextField titleTextField;

    /**
     * Constructor
     *
     * @param title the title to write on the component
     * @param centerX the x coordinate in the center of the object to draw
     * @param centerY the y coordinate in the center of the object to draw
     * @param height the height of the object to draw
     * @param width the width of the object to draw
     */
    protected DrawableComponent(String title, double centerX, double centerY, double height, double width){
        this.title = title;
        this.centerX = centerX;
        this.centerY = centerY;
        this.height = height;
        this.width = width;
    }

    /**
     * checks if the given point is inside the bounds of this component
     *
     * @param x the x coordinate of the point to check
     * @param y the y coordinate of the point to check
     * @return true if the point is inside the bounds of the component, otherwise false
     */
    public abstract boolean checkPointInBounds(double x, double y);

    /**
     * draws the component on the given GraphicsContext with the specified color and line thickness
     *
     * @param gc the GraphicsContext of the canvas to draw on
     * @param color the color to draw in
     * @param lineWidth the thickness of the lines to draw with
     */
    public abstract void draw(GraphicsContext gc, Color color, int lineWidth);

    /**Creates a copy of the DrawableComponent*/
    public abstract DrawableComponent createCopy();

    /**
     * Return a VBox containing the contents to populate a dialog box with to edit this object's contents
     *
     * @return a VBox containing the contents to populate a dialog box with to edit this object's contents
     */
    public VBox getUpdateContentsDialog(){
        VBox vbox = new VBox();
        HBox hbox = new HBox();
        Label titleLabel = new Label("Title: ");
        titleTextField = new TextField(title);
        hbox.getChildren().add(titleLabel);
        hbox.getChildren().add(titleTextField);
        vbox.getChildren().add(hbox);
        return vbox;
    }

    /**Updates the contents of this DrawableComponent after closing the edit dialog*/
    public void updateContents() {
        title = titleTextField.getText();
    }
}

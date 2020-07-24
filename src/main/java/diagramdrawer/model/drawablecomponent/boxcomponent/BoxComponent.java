package diagramdrawer.model.drawablecomponent.boxcomponent;

import diagramdrawer.model.drawablecomponent.DrawableComponent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**Represents DrawableComponents that are box-shaped*/
public abstract class BoxComponent extends DrawableComponent {

    /**
     * Constructor
     *
     * @param title   the title to write on the component
     * @param centerX the x coordinate in the center of the object to draw
     * @param centerY the y coordinate in the center of the object to draw
     * @param height  the height of the object to draw
     * @param width   the width of the object to draw
     */
    protected BoxComponent(String title, double centerX, double centerY, double height, double width) {
        super(title, centerX, centerY, height, width);
    }

    @Override
    public boolean checkPointInBounds(double x, double y) {
        double startX = centerX - (width / 2);
        double startY = centerY - (height / 2);

        return startX < x && startY < y && startX + width > x && startY + height > y;
    }

    @Override
    public void draw(GraphicsContext gc, Color color, int lineWidth) {
        //draw outside box
        double startX = centerX - (width / 2);
        double startY = centerY - (height / 2);
        gc.setStroke(color);
        gc.setLineWidth(lineWidth);
        gc.strokeLine(startX, startY, startX, startY + height);
        gc.strokeLine(startX, startY + height, startX + width, startY + height);
        gc.strokeLine(startX + width, startY + height, startX + width, startY);
        gc.strokeLine(startX + width, startY, startX, startY);

        final Text throwaway = new Text(title);
        new Scene(new Group(throwaway));
        gc.fillText(title,centerX - (throwaway.getLayoutBounds().getWidth()/2), getTitleYCoord());
    }

    /**
     * fetch the y coordinate to draw the component's title
     *
     * @return the y coordinate to draw the component's title
     */
    protected abstract double getTitleYCoord();
}

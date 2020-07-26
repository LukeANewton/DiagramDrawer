package diagramdrawer.model.connectiontype;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Aggregation extends ConnectionHead {
    @Override
    public String getName() {
        return "aggregation";
    }

    @Override
    public void drawHead(GraphicsContext gc, Color color, int lineWidth) {

    }
}

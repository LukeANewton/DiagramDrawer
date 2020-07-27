package diagramdrawer.model.drawablecomponent;

import diagramdrawer.model.connectiontype.ConnectionType;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Connection implements DrawableComponent {
    private Point2D start;
    private Point2D end;
    private ConnectionType connectionType;
    private ComboBox<ConnectionType> comboBox;

    public Connection(double startX, double startY){
        this.start = new Point2D(startX, startY);
        connectionType = ConnectionType.None;
        comboBox = new ComboBox<>();
    }

    public Connection(double startX, double startY, double endX, double endY, ConnectionType connectionHead){
        this.start = new Point2D(startX, startY);
        this.end = new Point2D(endX, endY);
        this.connectionType = connectionHead;
        comboBox = new ComboBox<>();
    }

    @Override
    public boolean checkPointInBounds(double x, double y) {
        //build a string line through between the points
        double m = (end.getY() - start.getY()) / (end.getX() - start.getX());
        double b = start.getY() - m * start.getX();
        //since the lines are so thin, we'll give some wiggle room: 5 pixels on either side
        return Math.abs(m*x + b - y) <= 5;
    }

    @Override
    public void draw(GraphicsContext gc, Color color, int lineWidth) {
        gc.setLineWidth(lineWidth);
        gc.setStroke(color);
        gc.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
        connectionType.drawHead(gc, color, lineWidth);
    }

    @Override
    public DrawableComponent createCopy() {
        return new Connection(this.start.getX(), this.start.getY(), this.end.getX(), this.end.getY(), this.connectionType);
    }

    @Override
    public VBox getUpdateContentsDialog() {
        //the layout has two main components: a drop down menu to select the connection type,
        // and a button to switch the direction of the connection
        VBox vbox = new VBox();
        HBox hbox = new HBox();
        Label titleLabel = new Label("Connection type: ");
        comboBox.getItems().setAll(ConnectionType.values());
        comboBox.getSelectionModel().select(connectionType);
        Button switchButton = new Button("Switch Direction");
        switchButton.setOnAction((e) -> switchDirection());
        hbox.getChildren().add(titleLabel);
        hbox.getChildren().add(comboBox);
        vbox.getChildren().add(hbox);
        vbox.getChildren().add(switchButton);
        return vbox;
    }

    @Override
    public void updateContents() {
        connectionType = comboBox.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return start.getX() == that.getStart().getX() && start.getY() == that.getStart().getY()
                && end.getX() == that.getEnd().getX() && end.getY() == that.getEnd().getY()
                && connectionType == that.getConnectionType();
    }

    /**swaps the start and end points of this Connection*/
    private void switchDirection(){
        Point2D temp = start;
        start = end;
        end = temp;
    }
}

package diagramdrawer.model.drawablecomponent;

import diagramdrawer.model.connectiontype.ConnectionType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.javatuples.Pair;

@Getter
@Setter
public class Connection extends DrawableComponent {
    private Pair<Double, Double> start;
    private Pair<Double, Double> end;
    private ConnectionType connectionType;
    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    private ComboBox<ConnectionType> comboBox;

    public Connection(){
        connectionType = ConnectionType.None;
        comboBox = new ComboBox<>();
    }

    public Connection(double startX, double startY){
        this.start = new Pair<>(startX, startY);
        connectionType = ConnectionType.None;
        comboBox = new ComboBox<>();
    }

    public Connection(double startX, double startY, double endX, double endY, ConnectionType connectionHead){
        this.start = new Pair<>(startX, startY);
        this.end = new Pair<>(endX, endY);
        this.connectionType = connectionHead;
        comboBox = new ComboBox<>();
    }

    @Override
    public boolean checkPointInBounds(double x, double y) {
        //since the lines are so thin, we'll give some wiggle room: 5 pixels on either side
        return Math.abs(calculateSlope()*x + calculateIntercept() - y) <= 5;
    }

    @Override
    public void draw(GraphicsContext gc, Color color, int lineWidth) {
        gc.setLineWidth(lineWidth);
        gc.setStroke(color);
        gc.strokeLine(start.getValue0(), start.getValue1(), end.getValue0(), end.getValue1());

        connectionType.drawHead(gc, end, start);
    }

    @Override
    public DrawableComponent createCopy() {
        return new Connection(this.start.getValue0(), this.start.getValue1(),
                this.end.getValue0(), this.end.getValue1(), this.connectionType);
    }

    @Override
    public VBox fetchUpdateContentsDialog() {
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
        return start.getValue0().equals(that.getStart().getValue0()) && start.getValue1().equals(that.getStart().getValue1())
                && end.getValue0().equals(that.getEnd().getValue0()) && end.getValue1().equals(that.getEnd().getValue1())
                && connectionType == that.getConnectionType();
    }

    /**swaps the start and end points of this Connection*/
    private void switchDirection(){
        Pair<Double, Double> temp = start;
        start = end;
        end = temp;
    }

    private double calculateSlope(){
        return (end.getValue1() - start.getValue1()) / (end.getValue0() - start.getValue0());
    }

    private double calculateIntercept(){
        return start.getValue1() - calculateSlope() * start.getValue0();
    }
}

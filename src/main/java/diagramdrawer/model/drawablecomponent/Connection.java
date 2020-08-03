package diagramdrawer.model.drawablecomponent;

import diagramdrawer.model.drawablecomponent.connectiontype.ConnectionType;
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
    //the position of the start of the connection
    private Pair<Double, Double> start;
    //the position of the end of the connection
    private Pair<Double, Double> end;
    //defines the relationship that the connection represents
    private ConnectionType connectionType;
    //allows the relationship of the connection to be changed in the dialog box
    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    private ComboBox<ConnectionType> comboBox;

    //constant that allows for some wiggle-room in selecting a connection line with the mouse
    private static final double BOUNDARY_LEEWAY = 10;

    /**Constructor*/
    public Connection(){
        connectionType = ConnectionType.None;
        comboBox = new ComboBox<>();
    }

    /**
     * Constructor
     *
     * @param startX the x coordinate of the start position of the connection
     * @param startY the y coordinate of the start position of the connection
     */
    public Connection(double startX, double startY){
        this.start = new Pair<>(startX, startY);
        connectionType = ConnectionType.None;
        comboBox = new ComboBox<>();
    }

    /**
     * Constructor
     *
     * @param startX the x coordinate of the start position of the connection
     * @param startY the y coordinate of the start position of the connection
     * @param endX the x coordinate of the end position of the connection
     * @param endY the y coordinate of the end position of the connection
     * @param connectionHead the type of connection made
     */
    public Connection(double startX, double startY, double endX, double endY, ConnectionType connectionHead){
        this.start = new Pair<>(startX, startY);
        this.end = new Pair<>(endX, endY);
        this.connectionType = connectionHead;
        comboBox = new ComboBox<>();
    }

    @Override
    public boolean checkPointInBounds(double x, double y) {
        double m =  (end.getValue1() - start.getValue1()) / (end.getValue0() - start.getValue0());
        double b = start.getValue1() - m * start.getValue0();

        //since the lines are so thin, we'll give some wiggle room as defined by the constant
        return Math.abs(m*x + b - y) <= BOUNDARY_LEEWAY &&
                //these bounds need to be checked so we dont click on the line equation but outside the
                //bounds of the drawn segment
                (inBox(start, end, x, y) || inBox(end, start, x, y) ||
                        inBox(new Pair<>(start.getValue0(), end.getValue1()),
                                new Pair<>(end.getValue0(), start.getValue1()), x, y) ||
                        inBox(new Pair<>(end.getValue0(), start.getValue1()),
                                new Pair<>(start.getValue0(), end.getValue1()), x, y));
    }

    /**
     * check if the provided coordinates are within a box defined by the two passed pairs
     *
     * @param topLeft the coordinates of the top-left of the box
     * @param bottomRight the coordinates of the bottom-right of the box
     * @param x the x coordinate of the point to check
     * @param y the y coordinate of the point to check
     * @return true if the coordinates are within the defined box
     */
    private boolean inBox(Pair<Double, Double> topLeft, Pair<Double, Double> bottomRight, double x, double y){
        return topLeft.getValue0() - BOUNDARY_LEEWAY < x && topLeft.getValue1() - BOUNDARY_LEEWAY < y
                && bottomRight.getValue0() + BOUNDARY_LEEWAY > x && bottomRight.getValue1() + BOUNDARY_LEEWAY > y;
    }

    @Override
    public void draw(GraphicsContext gc, Color color, int lineWidth) {
        gc.setLineWidth(lineWidth);
        gc.setStroke(color);
        gc.setLineDashes(connectionType.getDashedLineGap());
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
}

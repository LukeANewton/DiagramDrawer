package diagramdrawer.controller;


import diagramdrawer.controller.canvasstate.AddComponentState;
import diagramdrawer.controller.canvasstate.CanvasState;
import diagramdrawer.controller.canvasstate.SelectComponentState;
import diagramdrawer.model.DrawableComponent;
import diagramdrawer.model.SingleSectionClass;
import diagramdrawer.model.TwoSectionClass;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
public class Controller {
    @FXML
    public Pane canvasPane;
    @FXML
    private Button boxOneSectionButton;
    @FXML
    private Button boxTwoSectionButton;
    @FXML @Getter
    private Canvas canvas;

    @Setter
    CanvasState currentCanvasState;

    @Getter @Setter
    //the components drawn on the canvas
    ArrayList<DrawableComponent> drawnComponents;
    @Getter @Setter
    //the DrawableComponent currently highlighted
    DrawableComponent highlightedComponent;

    public Controller() {
        drawnComponents = new ArrayList<>();
        highlightedComponent = null;
        currentCanvasState = null;
    }

    @FXML
    private void initialize() {
        //set canvas size of center pane
        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());

        //add event on single section class box
        boxOneSectionButton.setOnAction(event -> new AddComponentState(this, new SingleSectionClass()));

        //add event on two section class box
        boxTwoSectionButton.setOnAction(event -> new AddComponentState(this, new TwoSectionClass()));

        //add event on canvas to highlight/unhighlight drawn components
        currentCanvasState = new SelectComponentState(this);
    }

    public void addComponent(DrawableComponent newComponent){
        drawnComponents.add(newComponent);
    }
}
package diagramdrawer.controller;


import diagramdrawer.model.SingleSectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import diagramdrawer.model.Person;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@Slf4j
public class Controller {
    @FXML
    public Pane canvasPane;
    @FXML
    private Button boxOneSectionButton;
    @FXML
    private Canvas canvas;

    private ObservableList<Person> masterData;

    public Controller() {
        masterData = FXCollections.observableArrayList();
        masterData.add(new Person(5, "John", true));
        masterData.add(new Person(7, "Albert", true));
        masterData.add(new Person(11, "Monica", false));
    }

    @FXML
    private void initialize() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());

        log.info(gc.toString());

        boxOneSectionButton.setOnAction(event -> drawSingleSectionBox(gc));


    }

    private void drawSingleSectionBox(GraphicsContext gc) {
        Runnable task = () -> {
            log.info("draw");
            SingleSectionClass boxToDraw = new SingleSectionClass("Class", 50, 50, 50, 100);
            boxToDraw.draw(gc);


            log.info("done draw");
        };

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    private Node createPage(Integer pageIndex) {

        VBox dataContainer = new VBox();

        TableView<Person> tableView = new TableView<>(masterData);
        TableColumn id = new TableColumn("ID");
        TableColumn name = new TableColumn("NAME");
        TableColumn employed = new TableColumn("EMPLOYED");

        tableView.getColumns().addAll(id, name, employed);
        dataContainer.getChildren().add(tableView);

        return dataContainer;
    }

    private void loadData() {

        Task<ObservableList<Person>> task = new Task<>() {
            @Override
            protected ObservableList<Person> call() {
                updateMessage("Loading data");
                System.out.println("searchText");
                return FXCollections.observableArrayList(masterData
                        .stream()
                        .filter(value -> value.getName().toLowerCase().contains("searchText".toLowerCase()))
                        .collect(Collectors.toList()));
            }
        };

        task.setOnSucceeded(event -> {
            //update UI
        });

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

}
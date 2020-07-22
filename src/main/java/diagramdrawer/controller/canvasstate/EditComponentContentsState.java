package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.MainWindowController;
import diagramdrawer.model.DrawableComponent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class EditComponentContentsState extends CanvasState {
    private DrawableComponent componentToEdit;
    private Stage dialog;

    /**
     * Constructor
     *
     * @param mainWindowController the controller for the main window that uses this Canvas State object
     */
    public EditComponentContentsState(MainWindowController mainWindowController, DrawableComponent componentToEdit) {
        super(mainWindowController);
        this.componentToEdit = componentToEdit;
        dialog = new Stage();
        openDialogBox();
    }

    private void openDialogBox() {
        dialog.initModality(Modality.APPLICATION_MODAL);

        VBox vBox = componentToEdit.getUpdateContentsDialog();
        Button doneButton = new Button("Done");
        doneButton.setId("donebutton");
        doneButton.setOnMouseClicked((e) -> exitState());
        vBox.setOnKeyPressed((e) -> {
            if(e.getCode() == KeyCode.ENTER){
                exitState();
            }
        });
        vBox.getChildren().add(doneButton);
        Scene dialogScene = new Scene(vBox);
        dialogScene.getStylesheets().add(String.valueOf(getClass().getResource("/drawablecomponent/drawablecomponent.css")));
        dialog.setScene(dialogScene);
        dialog.show();
    }

    @Override
    public void exitState() {
        componentToEdit.updateContents();
        dialog.close();
        mainWindowController.setCurrentCanvasState(new SelectComponentState(mainWindowController));
    }
}

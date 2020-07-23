package diagramdrawer.controller.canvasstate;

import diagramdrawer.controller.CanvasContentManagementController;
import diagramdrawer.model.drawablecomponent.DrawableComponent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**Handles the editing of a component's contents*/
public class EditComponentContentsState extends CanvasState {
    //the component to edit the contents of
    private DrawableComponent componentToEdit;
    //the dialog box to edit the contents of the component
    private Stage dialog;

    /**
     * Constructor
     *
     * @param canvasContentManagementController the controller for the main window that uses this Canvas State object
     */
    public EditComponentContentsState(CanvasContentManagementController canvasContentManagementController, DrawableComponent componentToEdit) {
        super(canvasContentManagementController);
        this.componentToEdit = componentToEdit;
        dialog = new Stage();
        openDialogBox();
    }

    /**Opens a dialog box to edit the contents of the selected component*/
    private void openDialogBox() {
        dialog.initModality(Modality.APPLICATION_MODAL);

        //get the dialog box contents
        VBox vBox = componentToEdit.getUpdateContentsDialog();
        Button doneButton = new Button("Done");
        doneButton.setId("donebutton");

        //set activities to be performed on completion of the edit
        doneButton.setOnMouseClicked((e) -> exitState());
        vBox.setOnKeyPressed((e) -> {
            if(e.getCode() == KeyCode.ENTER){
                exitState();
            }
        });

        //create and display the dialog box
        vBox.getChildren().add(doneButton);
        Scene dialogScene = new Scene(vBox);
        dialogScene.getStylesheets().add(String.valueOf(getClass().getResource("/drawablecomponent/drawablecomponent.css")));
        dialog.setScene(dialogScene);
        dialog.setOnCloseRequest((e) -> exitState());
        dialog.show();
    }

    @Override
    public void exitState() {
        componentToEdit.updateContents();
        dialog.close();
        canvasContentManagementController.setCurrentCanvasState(new SelectComponentState(canvasContentManagementController));
    }
}

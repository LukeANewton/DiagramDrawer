package diagramdrawer.controller;

import diagramdrawer.model.drawablecomponent.Connection;
import diagramdrawer.model.drawablecomponent.DrawableComponent;
import diagramdrawer.model.drawablecomponent.boxcomponent.SingleSectionClassBox;
import diagramdrawer.model.drawablecomponent.boxcomponent.ThreeSectionClassBox;
import diagramdrawer.model.drawablecomponent.boxcomponent.TwoSectionClassBox;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.reflections.ReflectionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.reflections.Reflections;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**Handles file operations for the system*/
public class FileController {
    private CanvasContentManagementController canvasContentManagementController;
    private FileChooser fileChooser;

    public FileController(CanvasContentManagementController canvasContentManagementController){
        this.canvasContentManagementController = canvasContentManagementController;
        fileChooser = new FileChooser();
    }

    public void saveDrawnComponents(Window window, ArrayList<DrawableComponent> drawableComponents){
        //Show save file dialog
        File selectedFile = fileChooser.showSaveDialog(window);
        if (selectedFile != null) {
            try {
                //generate save file
                DocumentBuilderFactory dbFactory =
                        DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.newDocument();

                // root element
                Element rootElement = doc.createElement("canvas");
                doc.appendChild(rootElement);

                //add element for each drawn component
                for (DrawableComponent component : drawableComponents) {
                    Element componentElement = doc.createElement(component.getClass().getName());
                    //invoke all getters of each component to populate component tag
                    Set<Method> getters = ReflectionUtils.getAllMethods(component.getClass(),
                            ReflectionUtils.withModifier(Modifier.PUBLIC), ReflectionUtils.withPrefix("get"),
                            ReflectionUtils.withParametersCount(0));
                    for(Method getter : getters){
                        if(!getter.getName().equals("getClass")) {
                            Element fieldElement = doc.createElement(getter.getName().substring(3));
                            fieldElement.appendChild(doc.createTextNode(getter.invoke(component).toString()));
                            componentElement.appendChild(fieldElement);
                        }
                    }
                    rootElement.appendChild(componentElement);
                }

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(selectedFile);
                transformer.transform(source, result);
            } catch(Exception e){
                showErrorAlert("Save failed:", e.getMessage());
            }
        }
    }

    public ArrayList<DrawableComponent> loadDrawnComponents(Window window){
        ArrayList<DrawableComponent> drawableComponents = new ArrayList<>();

        File selectedFile = fileChooser.showOpenDialog(window);
        if (selectedFile != null) {
            try{
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(selectedFile);

                Reflections reflections = new Reflections(DrawableComponent.class);
                //since we have to fetch by tag name, we iterate through each subclass of DrawableComponent
                //and find all the tags that are for that subclass. Each tag for that subclass is converted
                //into an object of that type and added to the drawableComponents list
                for(Class<?> c : reflections.getSubTypesOf(DrawableComponent.class)){
                    NodeList componentTags = document.getElementsByTagName(c.getName());
                    for(int i = 0; i < componentTags.getLength(); i++){
                        HashMap<String, String> fieldValueMap = new HashMap<>();
                        Node component = componentTags.item(i);
                        NodeList componentFields = component.getChildNodes();
                        for(int j = 0; j < componentFields.getLength(); j++){
                            Node componentField = componentFields.item(j);
                            fieldValueMap.put(componentField.getNodeName(), componentField.getTextContent());
                        }

                        String tagName = component.getNodeName();
                        if(tagName.equals(Connection.class.getName())){
                            drawableComponents.add(Connection.fromXML(fieldValueMap));
                        } else if(tagName.equals(SingleSectionClassBox.class.getName())){
                            drawableComponents.add(SingleSectionClassBox.fromXML(fieldValueMap));
                        } else if(tagName.equals(TwoSectionClassBox.class.getName())){
                            drawableComponents.add(TwoSectionClassBox.fromXML(fieldValueMap));
                        } else if(tagName.equals(ThreeSectionClassBox.class.getName())){
                            drawableComponents.add(ThreeSectionClassBox.fromXML(fieldValueMap));
                        }

                    }
                }
            } catch(Exception e){
                //print error alert
                showErrorAlert("Load failed:", e.getMessage());
            }
        }
        return drawableComponents;
    }

    public void exportCanvasToImage(Window window, Canvas canvas){
        canvasContentManagementController.setHighlightedComponent(null);
        canvasContentManagementController.getCanvasDrawController().issueDrawingCommand(() -> {
            //capture canvas image
            WritableImage image = canvas.snapshot(new SnapshotParameters(), null);

            //set up file save dialog
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File selectedFile = fileChooser.showSaveDialog(window);
            fileChooser.getExtensionFilters().removeAll(extFilter);
            if (selectedFile != null) {
                try {
                    //save file
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", selectedFile);
                } catch (IOException e) {
                    //print error alert
                    showErrorAlert("Export to image failed:", e.getMessage());
                }
            }
        });
    }

    /**
     * displays an alert box with the passed message
     *
     * @param message the message to display on the alert box
     * @param error details to add to the message
     */
    private void showErrorAlert(String message, String error){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(message);
        alert.setContentText(error);
        alert.showAndWait();
    }
}

package otmui.view;

import otmui.controller.component.*;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

public class UIFactory {

    public static PaneCtrl createLabelText(String label, String text){
        AnchorPane pane = null;
        LabelText ctrl = null;
        try {
            FXMLLoader loader = new FXMLLoader(UIFactory.class.getResource("/fxml/primitives/labelText.fxml"));
            pane = loader.load();
            ctrl = loader.getController();
            ctrl.setLabel(label);
            ctrl.setText(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PaneCtrl(pane,ctrl);
    }

    public static PaneCtrl createLabelList(String label, Collection<String> items){
        AnchorPane pane = null;
        LabelList ctrl = null;
        try {
            FXMLLoader loader = new FXMLLoader(UIFactory.class.getResource("/fxml/primitives/labelList.fxml"));
            pane = loader.load();

            ctrl = loader.getController();
            ctrl.setLabel(label);
            if(items!=null)
                ctrl.setListItems(items);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PaneCtrl(pane,ctrl);
    }

    public static PaneCtrl createLabelButton(String label,String buttonLabel,EventHandler<MouseEvent> handler){
        AnchorPane pane = null;
        LabelButton ctrl = null;
        try {
            FXMLLoader loader = new FXMLLoader(UIFactory.class.getResource("/fxml/primitives/labelButton.fxml"));
            pane = loader.load();
            ctrl = loader.getController();
            ctrl.setLabel(label);
            ctrl.setButtonLabel(buttonLabel);
            ctrl.setButtonAction(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PaneCtrl(pane,ctrl);
    }

    public static PaneCtrl createLabelLabel(String label1,String label2){
        AnchorPane pane = null;
        LabelLabel ctrl = null;
        try {
            FXMLLoader loader = new FXMLLoader(UIFactory.class.getResource("/fxml/primitives/labelLabel.fxml"));
            pane = loader.load();
            ctrl = loader.getController();
            ctrl.setLabel(label1);
            ctrl.setLabel2(label2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PaneCtrl(pane,ctrl);
    }

    public static PaneCtrl createLabelCheckbox(String label,boolean checkbox){
        AnchorPane pane = null;
        LabelCheckbox ctrl = null;
        try {
            FXMLLoader loader = new FXMLLoader(UIFactory.class.getResource("/fxml/primitives/labelCheckbox.fxml"));
            pane = loader.load();
            ctrl = loader.getController();
            ctrl.setLabel(label);
            ctrl.setSelected(checkbox);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PaneCtrl(pane,ctrl);
    }

    public static PaneCtrl createLabelCombobox(String label,Collection<String> items){
        AnchorPane pane = null;
        LabelCombobox ctrl = null;
        try {
            FXMLLoader loader = new FXMLLoader(UIFactory.class.getResource("/fxml/primitives/labelCombobox.fxml"));
            pane = loader.load();
            ctrl = loader.getController();
            ctrl.setLabel(label);
            ctrl.setComboboxItems(items);
            ctrl.selectFirst();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PaneCtrl(pane,ctrl);
    }

//    public static VBox createAddLanes(String label,AddLanes addLanes){
//        VBox vbox = new VBox();
//        vbox.setStyle("-fx-padding: 10;" +
//                "-fx-border-style: solid inside;" +
//                "-fx-border-width: 1;" +
//                "-fx-border-insets: 3;" +
//                "-fx-border-radius: 3;" +
//                "-fx-border-color: gray;");
//        vbox.getChildren().add(createLabelLabel(label,"").pane);
//        vbox.getChildren().add(createLabelText("lanes",String.format("%d",addLanes.lanes)).pane);
//        vbox.getChildren().add(createLabelText("length [km]",String.format("%.3f",addLanes.length)).pane);
//        return vbox;
//    }

    public static VBox createLanegroupPanel(models.BaseLaneGroup lanegroup){
        VBox vbox = new VBox();
        vbox.getChildren().add(createLabelLabel("id",String.format("%d",lanegroup.id)).pane);
        return vbox;
    }

//    public static Node createMacroLinkPanel(Link link){
//        macro.LinkModel model = (macro.LinkModel)link.getModel();
//        VBox vbox = new VBox();
//        vbox.getChildren().add(createLabelLabel("link type","macro"));
//        vbox.getChildren().add(createLabelText("capacity [vphpl]",model.getCapacityVPHPL().toString()));
//        vbox.getChildren().add(createLabelText("free flow speed [kph]",model.getFreeflowSpeedKPH().toString()));
//        vbox.getChildren().add(createLabelText("jam density [vpkpl]",model.getJamDensityVPKPL().toString()));
//        return vbox;
//    }
//
//    public static Node createMesoLinkPanel(Link link){
//        meso.LinkModel model = (meso.LinkModel)link.getModel();
//
//        VBox vbox = new VBox();
//        vbox.getChildren().add(createLabelLabel("link type","meso"));
//
//        // saturation flow rate
//        vbox.getChildren().add(createLabelText("capacity [vphpl]",model.getSaturationFlowVPHPL().toString()));
//
//        // transit time
//        vbox.getChildren().add(createLabelText("transit time [sec]",model.getTransitTimeSEC().toString()));
//
//        return vbox;
//    }
//
//    public static Node createMicroLinkPanel(Link link){
//
//        VBox vbox = new VBox();
//        vbox.getChildren().add(createLabelLabel("link type","micro"));
//        return vbox;
//    }

    public static Alert createExceptionDialog(Exception ex){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Exception Dialog");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.getDialogPane().setExpanded(true);

        return alert;
    }
}

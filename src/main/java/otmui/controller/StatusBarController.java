package otmui.controller;

import otmui.MainApp;
import otmui.graph.item.AbstractDrawLink;
import otmui.graph.item.AbstractDrawNode;
import otmui.graph.item.DrawSensor;
import otmui.model.Scenario;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import org.controlsfx.control.StatusBar;
import utils.OTMUtils;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class StatusBarController implements Initializable {

    public StatusBar statusBar;
    private MainApp myApp;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public StatusBarController(StatusBar statusBar, MainApp myApp){
        this.statusBar = statusBar;
        this.myApp = myApp;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void loadScenario(Scenario scenario){
        this.statusBar.setText("Scenario loaded.");
    }

    public void reset(){
        statusBar.setText("");
        statusBar.progressProperty().setValue(0d);
    }

    /////////////////////////////////////////////////
    // text
    /////////////////////////////////////////////////

    public void bind_text(ObservableValue<String> prop){
        statusBar.textProperty().bind(prop);
    }

    public void unbind_text(){
        statusBar.textProperty().unbind();
    }

    public void setText(Set<AbstractDrawLink> selectedLinks, Set<AbstractDrawNode> selectedNodes, Set<DrawSensor> selectedSensors, Set<AbstractDrawNode> selectedActuators){
        String str = "links {" +
                OTMUtils.comma_format(selectedLinks.stream().map(x->x.id).collect(toList())) +
                "} , nodes {" +
                OTMUtils.comma_format(selectedNodes.stream().map(x->x.id).collect(toList())) +
                "} , sensors {" +
                OTMUtils.comma_format(selectedSensors.stream().map(x->x.id).collect(toList())) +
                "}, actuators {" +
                OTMUtils.comma_format(selectedActuators.stream().map(x->x.id).collect(toList())) +
                "}";
        this.statusBar.setText(str);
    }

    /////////////////////////////////////////////////
    // progress bar
    /////////////////////////////////////////////////

    public void bind_progress(ReadOnlyDoubleProperty prop){
        statusBar.progressProperty().bind(prop);
    }

    public void unbind_progress(){
        statusBar.progressProperty().unbind();
    }

}

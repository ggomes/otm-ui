package otmui.controller;

import api.OTMdev;
import javafx.scene.Scene;
import otmui.ItemPool;
import otmui.MainApp;
import otmui.event.NewScenarioEvent;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import org.controlsfx.control.StatusBar;
import otmui.item.AbstractItem;
import utils.OTMUtils;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class StatusBarController implements Initializable {

    public StatusBar statusBar;
    private MainApp myApp;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public StatusBarController(StatusBar statusBar){
        this.statusBar = statusBar;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }

    public void attach_event_listeners(MainApp myApp) {
        this.myApp = myApp;

        Scene scene = myApp.stage.getScene();
        scene.addEventFilter(NewScenarioEvent.SCENARIO_LOADED, e->loadScenario(e.itempool) );
    }

    public void loadScenario(ItemPool itempool){
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

    public void setText(Map<String,Set<AbstractItem>> selection){
        String str = "links {" +
                OTMUtils.comma_format(selection.get("link").stream().map(x->x.id).collect(toList())) +
                "} , nodes {" +
                OTMUtils.comma_format(selection.get("node").stream().map(x->x.id).collect(toList())) +
                "} , sensors {" +
                OTMUtils.comma_format(selection.get("sensor").stream().map(x->x.id).collect(toList())) +
                "}, actuators {" +
                OTMUtils.comma_format(selection.get("actuator").stream().map(x->x.id).collect(toList())) +
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

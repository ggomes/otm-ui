package otmui.controller;

import javafx.scene.Scene;
import otmui.Data;
import otmui.ItemType;
import otmui.MainApp;
import otmui.event.DoHighlightSelection;
import otmui.event.NewScenarioEvent;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ObservableValue;
import org.controlsfx.control.StatusBar;
import otmui.item.AbstractItem;
import utils.OTMUtils;

import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class StatusBarController {

    public StatusBar statusBar;
    private MainApp myApp;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public StatusBarController(StatusBar statusBar){
        this.statusBar = statusBar;
    }

    public void initialize(MainApp myApp) {
        this.myApp = myApp;

        Scene scene = myApp.stage.getScene();
        scene.addEventFilter(NewScenarioEvent.SCENARIO_LOADED, e->loadScenario(e.data) );
        scene.addEventFilter(DoHighlightSelection.HIGHLIGHT_ANY, e->setText(e.selection) );

    }

    public void loadScenario(Data itempool){
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

    public void setText(Map<ItemType,Set<AbstractItem>> selection){
        String str = "link {" +
                OTMUtils.comma_format(selection.get(ItemType.link).stream().map(x->x.id).collect(toList())) +
                "} , node {" +
                OTMUtils.comma_format(selection.get(ItemType.node).stream().map(x->x.id).collect(toList())) +
                "} , sensor {" +
                OTMUtils.comma_format(selection.get(ItemType.sensor).stream().map(x->x.id).collect(toList())) +
                "}, actuator {" +
                OTMUtils.comma_format(selection.get(ItemType.actuator).stream().map(x->x.id).collect(toList())) +
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

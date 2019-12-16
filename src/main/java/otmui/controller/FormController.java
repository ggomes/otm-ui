package otmui.controller;

import javafx.scene.Scene;
import otmui.MainApp;
import otmui.event.DoOpenFormEvent;
import otmui.item.AbstractItem;
import otmui.view.*;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class FormController {

    private MainApp myApp;

    @FXML
    private ScrollPane dataPane;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public void initialize(MainApp myApp){
        this.myApp = myApp;

        dataPane.setFitToWidth(true);

        Scene scene = myApp.stage.getScene();
        scene.addEventFilter(DoOpenFormEvent.OPEN, e->openForm(e.item));
    }

    public void openForm(AbstractItem item){
        switch(item.getType()){
            case node:
                dataPane.setContent(FormFactory.nodeForm((otmui.item.Node)item,myApp.data));
                break;

            case link:
                dataPane.setContent(FormFactory.linkForm((otmui.item.Link)item,myApp.data));
                break;

            case sensor:
                dataPane.setContent(FormFactory.sensorForm((otmui.item.FixedSensor)item,myApp.data));
                break;

            case actuator:
                dataPane.setContent(FormFactory.actuatorForm((otmui.item.Actuator)item,myApp.data));
                break;

            case controller:
                dataPane.setContent(FormFactory.controllerForm((otmui.item.Controller)item,myApp.data));
                break;

            case commodity:
                dataPane.setContent(FormFactory.commodityForm((otmui.item.Commodity)item,myApp.data));
                break;

            case subnetwork:
                dataPane.setContent(FormFactory.subnetworkForm((otmui.item.Subnetwork)item,myApp.data));
                break;

        }
    }

}

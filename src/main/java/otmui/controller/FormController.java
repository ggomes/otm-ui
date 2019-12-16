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

//        scene.addEventFilter(GraphSelectEvent.CLICK2_LINK, e->graphSecondClickLink(e));
//        scene.addEventFilter(GraphSelectEvent.CLICK2_ACTUATOR, e->graphSecondClickActuator(e));
//        scene.addEventFilter(GraphSelectEvent.CLICK2_NODE, e->graphSecondClickNode(e));
//        scene.addEventFilter(GraphSelectEvent.CLICK2_SENSOR, e->graphSecondClickSensor(e));
//
//
//        scene.addEventFilter(TreeSelectEvent.CLICK2_LINK,e->treeSecondClickLink(e));
//        scene.addEventFilter(TreeSelectEvent.CLICK2_COMMODITY,e->treeSecondClickCommodity(e));
//        scene.addEventFilter(TreeSelectEvent.CLICK2_SUBNETWORK,e->treeSecondClickSubnetwork(e));
//        scene.addEventFilter(TreeSelectEvent.CLICK2_DEMAND,e->treeSecondClickDemand(e));
//        scene.addEventFilter(TreeSelectEvent.CLICK2_SPLIT,e->treeSecondClickSplit(e));
//        scene.addEventFilter(TreeSelectEvent.CLICK2_ACTUATOR,e->treeSecondClickActuator(e));
//        scene.addEventFilter(TreeSelectEvent.CLICK2_CONTROLLER,e->treeSecondClickController(e));
//        scene.addEventFilter(TreeSelectEvent.CLICK2_SENSOR,e->treeSecondClickSensor(e));
//
//        scene.addEventFilter(FormSelectEvent.CLICK2_LINK, e->formSecondClickLink(e));
//        scene.addEventFilter(FormSelectEvent.CLICK2_NODE, e->formSecondClickNode(e));
//        scene.addEventFilter(FormSelectEvent.CLICK2_SUBNETWORK, e->formSecondClickSubnetwork(e));
//        scene.addEventFilter(FormSelectEvent.CLICK2_COMMODITY, e->formSecondClickCommodity(e));
//        scene.addEventFilter(FormSelectEvent.CLICK2_DEMAND, e->formSecondClickDemand(e));
//        scene.addEventFilter(FormSelectEvent.CLICK2_SPLIT, e->formSecondClickSplit(e));
//        scene.addEventFilter(FormSelectEvent.CLICK2_ACTUATOR, e->formSecondClickActuator(e));
//        scene.addEventFilter(FormSelectEvent.CLICK2_CONTROLLER, e->formSecondClickController(e));
//        scene.addEventFilter(FormSelectEvent.CLICK2_SENSOR, e->formSecondClickSensor(e));
    }

    public void openForm(AbstractItem item){
        switch(item.getType()){
            case node:
                NodeData nodeData = new NodeData((otmui.item.Node)item);
                dataPane.setContent(nodeData.getScrollPane());
                break;

            case link:
                LinkData linkData = new LinkData((otmui.item.Link)item);
                dataPane.setContent(linkData.getScrollPane());
                break;

            case sensor:
                SensorData sensorData = new SensorData((otmui.item.FixedSensor)item);
                dataPane.setContent(sensorData.getScrollPane());
                break;

            case actuator:
                ActuatorData actuatorData = new ActuatorData((otmui.item.Actuator)item);
                dataPane.setContent(actuatorData.getScrollPane());
                break;

            case controller:
                ControllerData controllerData = new ControllerData((otmui.item.Controller)item);
                dataPane.setContent(controllerData.getScrollPane());
                break;

            case commodity:
                CommodityData commodityData = new CommodityData((otmui.item.Commodity)item);
                dataPane.setContent(commodityData.getScrollPane());
                break;

            case subnetwork:
                SubnetworkData subnetworkData = new SubnetworkData((otmui.item.Subnetwork)item);
                dataPane.setContent(subnetworkData.getScrollPane());
                break;

        }
    }

    /////////////////////////////////////////////////
    // show
    /////////////////////////////////////////////////

//    public void formSecondClickNode(FormSelectEvent e){
//        showNodeData(myApp.otm.scenario.network.nodes.get(e.itemId));
//        e.consume();
//    }
//
//    public void formSecondClickSubnetwork(FormSelectEvent e){
//        showSubnewtorkData(myApp.otm.scenario.subnetworks.get(e.itemId));
//        e.consume();
//    }
//
//    public void formSecondClickDemand(FormSelectEvent e){
//        showDemandData(e.itemId,myApp.demands.get(e.itemId));
//        e.consume();
//    }
//
//    public void formSecondClickSplit(FormSelectEvent e){
//        showSplitData(e.itemId,myApp.splits.get(e.itemId));
//        e.consume();
//    }
//
//    public void formSecondClickCommodity(FormSelectEvent e){
//        showCommodityData(myApp.otm.scenario.commodities.get(e.itemId));
//        e.consume();
//    }
//
//    public void formSecondClickController(FormSelectEvent e){
//        showControllerData(myApp.otm.scenario.controllers.get(e.itemId));
//        e.consume();
//    }
//
//    public void formSecondClickSensor(FormSelectEvent e){
//        showSensorData(myApp.otm.scenario.sensors.get(e.itemId));
//        e.consume();
//    }

//    public void formSecondClickActuator(FormSelectEvent e){
//        showActuatorData(myApp.otm.scenario.actuators.get(e.itemId));
//        e.consume();
//    }

    ////////////////////////////////////////////////////////////////


//    private static String getItemName(TreeSelectEvent e){
//        return (String)e.treeitem.getValue();
//    }
}

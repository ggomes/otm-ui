package otmui.controller;

import actuator.AbstractActuator;
import api.info.DemandInfo;
import api.info.SplitInfo;
import javafx.scene.Scene;
import otmui.MainApp;
import otmui.event.TreeSingleClickEvent;
import otmui.view.*;
import commodity.Subnetwork;
import control.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import sensor.AbstractSensor;

import java.util.Set;

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

    /////////////////////////////////////////////////
    // show
    /////////////////////////////////////////////////

//    private void graphSecondClickLink(GraphSelectEvent e){
//        BaseLink drawLink = (BaseLink) e.getSelected();
//        common.Link link = myApp.otm.scenario.network.links.get(drawLink.id);
//        showLinkData(link);
//        e.consume();
//    }
//
//    public void graphSecondClickNode(GraphSelectEvent e){
//        BaseNode drawNode = (BaseNode) e.getSelected();
//        common.Node node = myApp.otm.scenario.network.nodes.get(drawNode.id);
//        myApp.formController.showNodeData(node);
//        e.consume();
//    }
//
//    public void graphSecondClickSensor(GraphSelectEvent e){
//        BaseSensor drawSensor = (BaseSensor) e.getSelected();
//        AbstractSensor sensor = myApp.otm.scenario.sensors.get(drawSensor.id);
//        myApp.formController.showSensorData(sensor);
//        e.consume();
//    }
//
//    public void graphSecondClickActuator(GraphSelectEvent e){
//        BaseNode drawActuator = (BaseNode) e.getSelected();
//        AbstractActuator actuator = myApp.otm.scenario.actuators.get(drawActuator.id);
//        myApp.formController.showActuatorData(actuator);
//        e.consume();
//    }


//    public void treeSecondClickLink(TreeSelectEvent e){
//        TypeId typeid = myApp.itempool.getTypeId((String)e.treeitem.getValue());
//        showLinkData(myApp.otm.scenario.network.links.get(typeid.id));
////        e.consume();
//    }
//
//    public void treeSecondClickCommodity(TreeSelectEvent e){
//        TypeId typeid = myApp.itempool.getTypeId((String)e.treeitem.getValue());
//        showCommodityData(myApp.otm.scenario.commodities.get(typeid.id));
////        e.consume();
//    }
//
//    public void treeSecondClickSubnetwork(TreeSelectEvent e){
//        TypeId typeid = myApp.itempool.getTypeId((String)e.treeitem.getValue());
//        showSubnewtorkData(myApp.otm.scenario.subnetworks.get(typeid.id));
////        e.consume();
//    }

    public void treeSecondClickDemand(TreeSingleClickEvent e){
//        TypeId typeid = myApp.itempool.getTypeId((String)e.treeitem.getValue());
//        showDemandData(id,myApp.demands.get(typeid.id));
//        e.consume();
    }

    public void treeSecondClickSplit(TreeSingleClickEvent e){
//        long id = Maps.name2id_split(getItemName(e));
//        showSplitData(id,myApp.splits.get(id));
//        e.consume();
    }

//    public void treeSecondClickActuator(TreeSelectEvent e){
//        TypeId typeid = myApp.itempool.getTypeId((String)e.treeitem.getValue());
//        showActuatorData(myApp.otm.scenario.actuators.get(typeid.id));
////        e.consume();
//    }
//
//    public void treeSecondClickController(TreeSelectEvent e){
//        TypeId typeid = myApp.itempool.getTypeId((String)e.treeitem.getValue());
//        showControllerData(myApp.otm.scenario.controllers.get(typeid.id));
////        e.consume();
//    }
//
//    public void treeSecondClickSensor(TreeSelectEvent e){
//        TypeId typeid = myApp.itempool.getTypeId((String)e.treeitem.getValue());
//        showSensorData(myApp.otm.scenario.sensors.get(typeid.id));
////        e.consume();
//    }

//    public void formSecondClickLink(FormSelectEvent e){
//        showLinkData(myApp.otm.scenario.network.links.get(e.itemId));
//        e.consume();
//    }
//
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

    public void showLinkData(common.Link link){
        LinkData edgeData = new LinkData(link,myApp.otm);
        dataPane.setContent(edgeData.getScrollPane());
    }

    public void showNodeData(common.Node node){
        NodeData nodeData = new NodeData(node,myApp.otm);
        dataPane.setContent(nodeData.getScrollPane());
    }

    public void showCommodityData(commodity.Commodity commodity){
        System.out.println("COMMODITY");
//        CommodityData commodityData = new CommodityData(commodity,myApp.otm);
//        dataPane.setContent(commodityData.getScrollPane());
    }

    public void showSubnewtorkData(Subnetwork subnetwork){
        SubnetworkData subnetworkData = new SubnetworkData(subnetwork,myApp.otm);
        dataPane.setContent(subnetworkData.getScrollPane());
    }

    public void showDemandData(long link_id,Set<DemandInfo> demands){
        DemandData demandData = new DemandData(link_id,demands,myApp.otm);
        dataPane.setContent(demandData.getScrollPane());
    }

    public void showSplitData(long node_id,Set<SplitInfo> splits){
        SplitData splitData = new SplitData(node_id,splits,myApp.otm);
        dataPane.setContent(splitData.getScrollPane());
    }

    public void showActuatorData(AbstractActuator actuator){
        ActuatorData actuatorData = new ActuatorData(actuator);
        dataPane.setContent(actuatorData.getScrollPane());
    }

    public void showControllerData(AbstractController controller){
        ControllerData controllerData = new ControllerData(controller);
        dataPane.setContent(controllerData.getScrollPane());
    }

    public void showSensorData(AbstractSensor sensor){
        SensorData sensorData = new SensorData(sensor);
        dataPane.setContent(sensorData.getScrollPane());
    }

//    private static String getItemName(TreeSelectEvent e){
//        return (String)e.treeitem.getValue();
//    }
}

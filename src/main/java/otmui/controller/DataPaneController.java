package otmui.controller;

import actuator.AbstractActuator;
import api.info.DemandInfo;
import api.info.SplitInfo;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import otmui.MainApp;
import otmui.Maps;
import otmui.event.GraphSelectEvent;
import otmui.event.TreeSelectEvent;
import otmui.item.AbstractLink;
import otmui.item.AbstractNode;
import otmui.item.DrawSensor;
import otmui.view.*;
import commodity.Subnetwork;
import control.AbstractController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import sensor.AbstractSensor;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class DataPaneController implements Initializable {

    private MainApp myApp;

    @FXML
    private ScrollPane dataPane;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataPane.setFitToWidth(true);
    }

    public void attach_event_listeners(MainApp myApp){
        this.myApp = myApp;

        Scene scene = myApp.stage.getScene();
        scene.addEventFilter(GraphSelectEvent.CLICK2_LINK, e->graphSecondClickLink(e));
        scene.addEventFilter(GraphSelectEvent.CLICK2_ACTUATOR, e->graphSecondClickActuator(e));
        scene.addEventFilter(GraphSelectEvent.CLICK2_NODE, e->graphSecondClickNode(e));
        scene.addEventFilter(GraphSelectEvent.CLICK2_SENSOR, e->graphSecondClickSensor(e));


        scene.addEventFilter(TreeSelectEvent.CLICK2_LINK,e->treeSecondClickLink(e));
        scene.addEventFilter(TreeSelectEvent.CLICK2_COMMODITY,e->treeSecondClickCommodity(e));
        scene.addEventFilter(TreeSelectEvent.CLICK2_SUBNETWORK,e->treeSecondClickSubnetwork(e));
        scene.addEventFilter(TreeSelectEvent.CLICK2_DEMAND,e->treeSecondClickDemand(e));
        scene.addEventFilter(TreeSelectEvent.CLICK2_SPLIT,e->treeSecondClickSplit(e));
        scene.addEventFilter(TreeSelectEvent.CLICK2_ACTUATOR,e->treeSecondClickActuator(e));
        scene.addEventFilter(TreeSelectEvent.CLICK2_CONTROLLER,e->treeSecondClickController(e));
        scene.addEventFilter(TreeSelectEvent.CLICK2_SENSOR,e->treeSecondClickSensor(e));
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

    private void graphSecondClickLink(GraphSelectEvent e){
        AbstractLink drawLink = (AbstractLink) e.getSelected();
        common.Link link = myApp.otm.scenario.network.links.get(drawLink.id);
        showLinkData(link);
        e.consume();
    }


    public void graphSecondClickNode(GraphSelectEvent e){
        AbstractNode drawNode = (AbstractNode) e.getSelected();
        common.Node node = myApp.otm.scenario.network.nodes.get(drawNode.id);
        myApp.datapaneController.showNodeData(node);
        e.consume();
    }

    public void graphSecondClickSensor(GraphSelectEvent e){
        DrawSensor drawSensor = (DrawSensor) e.getSelected();
        AbstractSensor sensor = myApp.otm.scenario.sensors.get(drawSensor.id);
        myApp.datapaneController.showSensorData(sensor);
        e.consume();
    }

    public void graphSecondClickActuator(GraphSelectEvent e){
        AbstractNode drawActuator = (AbstractNode) e.getSelected();
        AbstractActuator actuator = myApp.otm.scenario.actuators.get(drawActuator.id);
        myApp.datapaneController.showActuatorData(actuator);
        e.consume();
    }


    public void treeSecondClickLink(TreeSelectEvent e){
        Long linkid = Maps.name2linkid.getFromFirst(getItemName(e));
        common.Link link = myApp.otm.scenario.network.links.get(linkid);
        showLinkData(link);
//        e.consume();
    }

    public void treeSecondClickCommodity(TreeSelectEvent e){
        long id = Maps.name2commodityid.getFromFirst(getItemName(e));
        showCommodityData(myApp.otm.scenario.commodities.get(id));
//        e.consume();
    }

    public void treeSecondClickSubnetwork(TreeSelectEvent e){
        long id = Maps.name2subnetworkid.getFromFirst(getItemName(e));
        showSubnewtorkData(myApp.otm.scenario.subnetworks.get(id));
//        e.consume();
    }

    public void treeSecondClickDemand(TreeSelectEvent e){
        long id = Maps.name2demandid.getFromFirst(getItemName(e));
        showDemandData(id,myApp.demands.get(id));
//        e.consume();
    }

    public void treeSecondClickSplit(TreeSelectEvent e){
        long id = Maps.name2splitid.getFromFirst(getItemName(e));
        showSplitData(id,myApp.splits.get(id));
//        e.consume();
    }

    public void treeSecondClickActuator(TreeSelectEvent e){
        long id = Maps.name2actuatorid.getFromFirst(getItemName(e));
        showActuatorData(myApp.otm.scenario.actuators.get(id));
//        e.consume();
    }

    public void treeSecondClickController(TreeSelectEvent e){
        long id = Maps.name2controllerid.getFromFirst(getItemName(e));
        showControllerData(myApp.otm.scenario.controllers.get(id));
//        e.consume();
    }

    public void treeSecondClickSensor(TreeSelectEvent e){
        long id = Maps.name2sensorid.getFromFirst(getItemName(e));
        showSensorData(myApp.otm.scenario.sensors.get(id));
//        e.consume();
    }

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

    protected void showLinkData(common.Link link){
        LinkData edgeData = new LinkData(link,myApp.otm);
        dataPane.setContent(edgeData.getScrollPane());
    }

    protected void showNodeData(common.Node node){
        NodeData nodeData = new NodeData(node,myApp.otm);
        dataPane.setContent(nodeData.getScrollPane());
    }

    protected void showCommodityData(commodity.Commodity commodity){
        CommodityData commodityData = new CommodityData(commodity,myApp.otm);
        dataPane.setContent(commodityData.getScrollPane());
    }

    protected void showSubnewtorkData(Subnetwork subnetwork){
        SubnetworkData subnetworkData = new SubnetworkData(subnetwork,myApp.otm);
        dataPane.setContent(subnetworkData.getScrollPane());
    }

    protected void showDemandData(long link_id,Set<DemandInfo> demands){
        DemandData demandData = new DemandData(link_id,demands,myApp.otm);
        dataPane.setContent(demandData.getScrollPane());
    }

    protected void showSplitData(long node_id,Set<SplitInfo> splits){
        SplitData splitData = new SplitData(node_id,splits,myApp.otm);
        dataPane.setContent(splitData.getScrollPane());
    }

    protected void showActuatorData(AbstractActuator actuator){
        ActuatorData actuatorData = new ActuatorData(actuator);
        dataPane.setContent(actuatorData.getScrollPane());
    }

    protected void showControllerData(AbstractController controller){
        ControllerData controllerData = new ControllerData(controller);
        dataPane.setContent(controllerData.getScrollPane());
    }

    protected void showSensorData(AbstractSensor sensor){
        SensorData sensorData = new SensorData(sensor);
        dataPane.setContent(sensorData.getScrollPane());
    }

    private static String getItemName(TreeSelectEvent e){
        TreeView treeView = (TreeView) e.getSelected();
        TreeItem item = (TreeItem) treeView.getSelectionModel().getSelectedItem();
        return (String)item.getValue();
    }
}

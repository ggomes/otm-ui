package otmui.controller;

import actuator.AbstractActuator;
import otmui.MainApp;
//import otmui.model.*;
import otmui.view.*;
import commodity.Subnetwork;
import control.AbstractController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import sensor.AbstractSensor;

import java.net.URL;
import java.util.ResourceBundle;

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

    public void setApp(MainApp myApp){
        this.myApp = myApp;
    }

    public void reset(){

    }

    /////////////////////////////////////////////////
    // show
    /////////////////////////////////////////////////

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

//    protected void showDemandData(DemandsForLink demandsForLink){
//        DemandData demandData = new DemandData(demandsForLink,myApp.otm);
//        dataPane.setContent(demandData.getScrollPane());
//    }

//    protected void showSplitData(SplitsForNode splitsForNode){
//        SplitData splitData = new SplitData(splitsForNode,myApp.otm);
//        dataPane.setContent(splitData.getScrollPane());
//    }

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

}

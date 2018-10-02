/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */

package otmui.controller;

import actuator.AbstractActuator;
import otmui.MainApp;
import otmui.model.*;
import otmui.view.*;
import commodity.Subnetwork;
import control.AbstractController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;

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

    public void loadScenario(Scenario scenario){
    }

    /////////////////////////////////////////////////
    // show
    /////////////////////////////////////////////////

    protected void showLinkData(Link link){
        LinkData edgeData = new LinkData(link);
        dataPane.setContent(edgeData.getScrollPane());
    }

    protected void showNodeData(otmui.model.Node node){
        NodeData nodeData = new NodeData(node,myApp.scenario);
        dataPane.setContent(nodeData.getScrollPane());
    }

    protected void showCommodityData(commodity.Commodity commodity){
        CommodityData commodityData = new CommodityData(commodity,myApp.scenario);
        dataPane.setContent(commodityData.getScrollPane());
    }

    protected void showSubnewtorkData(Subnetwork subnetwork){
        SubnetworkData subnetworkData = new SubnetworkData(subnetwork,myApp.scenario);
        dataPane.setContent(subnetworkData.getScrollPane());
    }

    protected void showDemandData(DemandsForLink demandsForLink){
        DemandData demandData = new DemandData(demandsForLink,myApp.scenario);
        dataPane.setContent(demandData.getScrollPane());
    }

    protected void showSplitData(SplitsForNode splitsForNode){
        SplitData splitData = new SplitData(splitsForNode);
        dataPane.setContent(splitData.getScrollPane());
    }

    protected void showActuatorData(AbstractActuator actuator){
        ActuatorData actuatorData = new ActuatorData(actuator);
        dataPane.setContent(actuatorData.getScrollPane());
    }

    protected void showControllerData(AbstractController controller){
        ControllerData controllerData = new ControllerData(controller,myApp.scenario);
        dataPane.setContent(controllerData.getScrollPane());
    }

    protected void showSensorData(Sensor sensor){
        SensorData sensorData = new SensorData(sensor,myApp.scenario);
        dataPane.setContent(sensorData.getScrollPane());
    }

}

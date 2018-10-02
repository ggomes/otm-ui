/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */

package otmui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import actuator.AbstractActuator;
import otmui.ElementType;
import otmui.MainApp;
import otmui.Maps;
import otmui.event.TreeSelectEvent;
import otmui.graph.item.AbstractDrawLink;
import otmui.graph.item.DrawSensor;
import otmui.model.*;
import commodity.Subnetwork;
import control.AbstractController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class TreeController implements Initializable {

    private MainApp myApp;

    @FXML
    private TreeView scenariotree;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scenariotree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void setApp(MainApp myApp){
        this.myApp = myApp;
    }

    public void loadScenario(Scenario scenario){

        // populate the tree
        TreeItem<String> rootItem = new TreeItem<>("scenario");
        rootItem.setExpanded(false);

        // commodities
        TreeItem<String> comms = new TreeItem<>(Maps.elementName.getFromFirst(ElementType.COMMODITY));
        rootItem.getChildren().add(comms);
        for (commodity.Commodity comm : scenario.getCommodities())
            comms.getChildren().add(new TreeItem<>(Maps.name2commodityid.getFromSecond(comm.getId())));

        // links
        TreeItem<String> lks = new TreeItem<>(Maps.elementName.getFromFirst(ElementType.LINK));
        rootItem.getChildren().add(lks);
        for (Link l : scenario.getLinks())
            lks.getChildren().add(new TreeItem<>(Maps.name2linkid.getFromSecond(l.getId())));

        // subnetwork
        TreeItem<String> snt = new TreeItem<>(Maps.elementName.getFromFirst(ElementType.SUBNETWORK));
        rootItem.getChildren().add(snt);
        for (Subnetwork s : scenario.getSubnetworks())
            snt.getChildren().add(new TreeItem<>(Maps.name2subnetworkid.getFromSecond(s.getId())));

        // demands
        TreeItem<String> dmd = new TreeItem<>(Maps.elementName.getFromFirst(ElementType.DEMAND));
        rootItem.getChildren().add(dmd);
        for (DemandsForLink d : scenario.getDemandsForLinks())
            dmd.getChildren().add(new TreeItem<>(Maps.name2demandid.getFromSecond(d.get_link_id())));

        // splitsForNode
        TreeItem<String> spt = new TreeItem<>(Maps.elementName.getFromFirst(ElementType.SPLIT));
        rootItem.getChildren().add(spt);
        for (SplitsForNode s : scenario.getSplits())
            spt.getChildren().add(new TreeItem<>(Maps.name2splitid.getFromSecond(s.getId())));

        // actuators
        TreeItem<String> act = new TreeItem<>(Maps.elementName.getFromFirst(ElementType.ACTUATOR));
        rootItem.getChildren().add(act);
        for (AbstractActuator a : scenario.getActuators())
            act.getChildren().add(new TreeItem<>(Maps.name2actuatorid.getFromSecond(a.getId())));

        // controllers
        TreeItem<String> cnt = new TreeItem<>(Maps.elementName.getFromFirst(ElementType.CONTROLLER));
        rootItem.getChildren().add(cnt);
        for(AbstractController c : scenario.getControllers())
            cnt.getChildren().add(new TreeItem<>(Maps.name2controllerid.getFromSecond(c.getId())));

        // sensors
        TreeItem<String> sns = new TreeItem<>(Maps.elementName.getFromFirst(ElementType.SENSOR));
        rootItem.getChildren().add(sns);
        for (Sensor s : scenario.getSensors())
            sns.getChildren().add(new TreeItem<>(Maps.name2sensorid.getFromSecond(s.get_id())));


        scenariotree.setRoot(rootItem);

    }

    /////////////////////////////////////////////////
    // focusing
    /////////////////////////////////////////////////

    public void focusGraph(ActionEvent e){
        myApp.graphpaneController.focusGraphOnSelection();
    }

    /////////////////////////////////////////////////
    // mouse events
    /////////////////////////////////////////////////

    public void mouseClick(MouseEvent mouseEvent){

        // only register left click
        if (!mouseEvent.getButton().equals(MouseButton.PRIMARY))
            return;

        int clickcount = mouseEvent.getClickCount();

        // fire event for first click
        if(clickcount==1)
            Event.fireEvent(mouseEvent.getTarget(),new TreeSelectEvent(TreeSelectEvent.CLICK1,mouseEvent));

        if(clickcount==2){
            TreeItem item = (TreeItem) myApp.scenarioTreeController.getTreeView().getSelectionModel().getSelectedItem();
            if(item==null)
                return;
            TreeItem parent = item.getParent();
            if(parent!=null) {
                ElementType elementType = Maps.elementName.getFromSecond((String) parent.getValue());
                if(elementType!=null)
                    switch (elementType) {
                        case LINK:
                            Event.fireEvent(mouseEvent.getTarget(),new TreeSelectEvent(TreeSelectEvent.CLICK2_LINK,mouseEvent));
                            break;
                        case COMMODITY:
                            Event.fireEvent(mouseEvent.getTarget(),new TreeSelectEvent(TreeSelectEvent.CLICK2_COMMODITY,mouseEvent));
                            break;
                        case SUBNETWORK:
                            Event.fireEvent(mouseEvent.getTarget(),new TreeSelectEvent(TreeSelectEvent.CLICK2_SUBNETWORK,mouseEvent));
                            break;
                        case SPLIT:
                            Event.fireEvent(mouseEvent.getTarget(),new TreeSelectEvent(TreeSelectEvent.CLICK2_SPLIT,mouseEvent));
                            break;
                        case DEMAND:
                            Event.fireEvent(mouseEvent.getTarget(),new TreeSelectEvent(TreeSelectEvent.CLICK2_DEMAND,mouseEvent));
                            break;
                        case ACTUATOR:
                            Event.fireEvent(mouseEvent.getTarget(),new TreeSelectEvent(TreeSelectEvent.CLICK2_ACTUATOR,mouseEvent));
                            break;
                        case CONTROLLER:
                            Event.fireEvent(mouseEvent.getTarget(),new TreeSelectEvent(TreeSelectEvent.CLICK2_CONTROLLER,mouseEvent));
                            break;
                        case SENSOR:
                            Event.fireEvent(mouseEvent.getTarget(),new TreeSelectEvent(TreeSelectEvent.CLICK2_SENSOR,mouseEvent));
                            break;

                        default:
                            System.err.println("????");
                    }
            }
        }

        mouseEvent.consume();

    }

    public void highlight(Set<AbstractDrawLink> drawLinks, Set<DrawSensor> drawSensors){

        for(AbstractDrawLink obj : drawLinks) {
            TreeItem linkList = searchItem(scenariotree.getRoot(), "links");
            TreeItem item = searchItem(linkList, Maps.name2linkid.getFromSecond(obj.id));
            if (item != null)
                scenariotree.getSelectionModel().select(scenariotree.getRow(item));
        }

        for(DrawSensor obj : drawSensors) {
            TreeItem sensorList = searchItem(scenariotree.getRoot(), "sensors");
            TreeItem item = searchItem(sensorList, Maps.name2sensorid.getFromSecond(obj.id));
            if (item != null)
                scenariotree.getSelectionModel().select(scenariotree.getRow(item));
        }

    }

    /////////////////////////////////////////////////
    // get / set
    /////////////////////////////////////////////////

    public TreeView getTreeView(){
        return scenariotree;
    }

    public void clearSelection(){
        scenariotree.getSelectionModel().clearSelection();
    }

    /////////////////////////////////////////////////
    // private
    /////////////////////////////////////////////////

    private TreeItem searchItem(TreeItem<String> root, String searchvalue){
        for(TreeItem<String> item : root.getChildren()){
            if(item.getValue().compareTo(searchvalue)==0)
                return item;
        }
        return null;
    }
}

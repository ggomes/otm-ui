/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */

package otmui.controller;

import actuator.AbstractActuator;
import otmui.ElementType;
import otmui.MainApp;
import otmui.Maps;
import otmui.event.FormSelectEvent;
import otmui.event.GraphSelectEvent;
import otmui.event.TreeSelectEvent;
import otmui.graph.item.AbstractDrawLink;
import otmui.graph.item.AbstractDrawNode;
import otmui.graph.item.DrawSensor;
import otmui.model.*;
import commodity.Commodity;
import commodity.Subnetwork;
import control.AbstractController;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SelectionManager {

    public MainApp myApp;

    public Set<AbstractDrawNode> selectedNodes;
    public Set<AbstractDrawLink> selectedLinks;
    public Set<DrawSensor> selectedSensors;

    public SelectionManager(MainApp myApp) {
        this.myApp = myApp;
        this.selectedNodes = new HashSet<>();
        this.selectedLinks = new HashSet<>();
        this.selectedSensors = new HashSet<>();
    }

    /////////////////////////////////////////////////
    // graph clicks
    /////////////////////////////////////////////////

    // if shift is pressed then add to highlighted items.
    // otherwise clear and add to highlighted items
    public void graphFirstClickNode(GraphSelectEvent e){
        AbstractDrawNode drawNode = (AbstractDrawNode) e.getSelected();
        boolean alreadySelected = selectedNodes.contains(drawNode);
        boolean shiftPressed = e.event.isShiftDown();

        if(!shiftPressed)
            clearSelection();

        if(alreadySelected)
            selectedNodes.remove(drawNode);
        else
            selectedNodes.add(drawNode);

        highlightSelection();

        e.consume();
    }

    // if shift is pressed then add to highlighted items.
    // otherwise clear and add to highlighted items
    public void graphFirstClickLink(GraphSelectEvent e){

        AbstractDrawLink drawLink = (AbstractDrawLink) e.getSelected();
        boolean alreadySelected = selectedLinks.contains(drawLink);
        boolean shiftPressed = e.event.isShiftDown();

        if(!shiftPressed)
            clearSelection();

        if(alreadySelected)
            selectedLinks.remove(drawLink);
        else
            selectedLinks.add(drawLink);

        highlightSelection();

        e.consume();
    }

    // if shift is pressed then add to highlighted items.
    // otherwise clear and add to highlighted items
    public void graphFirstClickSensor(GraphSelectEvent e){
        DrawSensor drawSensor = (DrawSensor) e.getSelected();
        boolean alreadySelected = selectedSensors.contains(drawSensor);
        boolean shiftPressed = e.event.isShiftDown();

        if(!shiftPressed)
            clearSelection();

        if(alreadySelected)
            selectedSensors.remove(drawSensor);
        else
            selectedSensors.add(drawSensor);

        highlightSelection();

        e.consume();
    }

    public void graphSecondClickNode(GraphSelectEvent e){
        // open data pane for node
        AbstractDrawNode drawNode = (AbstractDrawNode) e.getSelected();
        Node node = myApp.scenario.getNodeWithId(drawNode.id);
        myApp.datapaneController.showNodeData(node);
        e.consume();
    }

    public void graphSecondClickLink(GraphSelectEvent e){
        // open data pane for link
        AbstractDrawLink drawLink = (AbstractDrawLink) e.getSelected();
        Link link = myApp.scenario.getLinkWithId(drawLink.id);
        myApp.datapaneController.showLinkData(link);
        e.consume();
    }

    public void graphSecondClickSensor(GraphSelectEvent e){
        // open data pane for sensor
        DrawSensor drawSensor = (DrawSensor) e.getSelected();
        Sensor sensor = myApp.scenario.getSensorWithId(drawSensor.id);
        myApp.datapaneController.showSensorData(sensor);
        e.consume();
    }

    /////////////////////////////////////////////////
    // tree clicks
    /////////////////////////////////////////////////

    public void treeFirstClick(TreeSelectEvent e){
        ObservableList<TreeItem> items = myApp.scenarioTreeController.getTreeView().getSelectionModel().getSelectedItems();
        Set<AbstractDrawLink> drawLinks = new HashSet<>();
        Set<AbstractDrawNode> drawNodes = new HashSet<>();
        Set<DrawSensor> drawSensors = new HashSet<>();

        // get all drawLinks and drawNodes that have been selected
        Long id;
        for(TreeItem item : items){
            if(item==null)
                continue;
            TreeItem parent = item.getParent();
            if(parent==null)
                continue;
            ElementType elementType = Maps.elementName.getFromSecond((String) parent.getValue());
            if(elementType!=null)
                switch(elementType){

                    case LINK:
                        id = Maps.name2linkid.getFromFirst((String)item.getValue());
                        Link link = myApp.scenario.getLinkWithId(id);
                        if(link.drawLink !=null)
                            drawLinks.add(link.drawLink);
                        break;

                    case SUBNETWORK:
                        id = Maps.name2subnetworkid.getFromFirst((String)item.getValue());
                        Subnetwork subnetwork = myApp.scenario.getSubnetworkWithId(id);
                        drawLinks.addAll(subnetwork
                                .get_link_ids().stream()
                                .map(x->myApp.scenario.getLinkWithId(x))
                                .filter(Objects::nonNull)
                                .map(x->x.drawLink)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet())
                        );
                        break;

                    case DEMAND:
                        id = Maps.name2demandid.getFromFirst((String)item.getValue());
                        DemandsForLink demandsForLink = myApp.scenario.get_demands_with_link_id(id);
                        Link linkd = myApp.scenario.getLinkWithId(demandsForLink.link_id);
                        if(linkd!=null && linkd.drawLink !=null)
                            drawLinks.add(linkd.drawLink);
                        break;

                    case SPLIT:
                        id = Maps.name2splitid.getFromFirst((String)item.getValue());
                        SplitsForNode splitsForNode = myApp.scenario.getSplitWithId(id);
                        Node nodex = myApp.scenario.getNodeWithId(splitsForNode.node_id);
                        if(nodex!=null && nodex.drawNode !=null)
                            drawNodes.add(nodex.drawNode);
                        break;

                    case ACTUATOR:
                        id = Maps.name2actuatorid.getFromFirst((String)item.getValue());
                        AbstractActuator actuator = myApp.scenario.getActuatorWithId(id);
                        if(actuator.target instanceof common.Node) {
                            Node node = myApp.scenario.getNodeWithId(actuator.target.getId());
                            if(node!=null)
                                drawNodes.add(node.drawNode);
                        }
                        if(actuator.target instanceof common.Link){
                            Link alink = myApp.scenario.getLinkWithId(((common.Node) actuator.target).getId());
                            if(alink!=null)
                                drawLinks.add(alink.drawLink);
                        }
                        break;

                    case CONTROLLER:
                        id = Maps.name2controllerid.getFromFirst((String)item.getValue());
                        AbstractController controller = myApp.scenario.getControllerWithId(id);
                        for(actuator.AbstractActuator bact : controller.actuators) {
                            AbstractActuator act = myApp.scenario.getActuatorWithId(bact.id);
                            if (act == null)
                                continue;
                            if (bact.target instanceof common.Node){
                                Long nodeid = act.target.getId();
                                AbstractDrawNode drawNode = myApp.scenario.getNodeWithId(nodeid).drawNode;
                                drawNodes.add(drawNode);
                            }
                            if(bact.target instanceof common.Link) {
                                AbstractDrawLink drawLink = myApp.scenario.getLinkWithId(act.target.getId()).drawLink;
                                drawLinks.add(drawLink);
                            }
                        }
                        break;

                    case SENSOR:
                        id = Maps.name2sensorid.getFromFirst((String)item.getValue());
                        Sensor sensor = myApp.scenario.getSensorWithId(id);
                        drawSensors.add(sensor.drawSensor);
                        break;
                }
        }

        // set selection
        selectedLinks = drawLinks;
        selectedNodes = drawNodes;
        selectedSensors = drawSensors;

        // send selection to the graph
        highlightSelection();

    }

    public void treeSecondClickLink(TreeSelectEvent e){
        Long linkid = Maps.name2linkid.getFromFirst(getItemName(e));
        Link link = myApp.scenario.getLinkWithId(linkid);
        myApp.datapaneController.showLinkData(link);
//        e.consume();
    }

    public void treeSecondClickCommodity(TreeSelectEvent e){
        long id = Maps.name2commodityid.getFromFirst(getItemName(e));
        myApp.datapaneController.showCommodityData(myApp.scenario.getCommodityWithId(id));
//        e.consume();
    }

    public void treeSecondClickSubnetwork(TreeSelectEvent e){
        long id = Maps.name2subnetworkid.getFromFirst(getItemName(e));
        myApp.datapaneController.showSubnewtorkData(myApp.scenario.getSubnetworkWithId(id));
//        e.consume();
    }

    public void treeSecondClickDemand(TreeSelectEvent e){
        long id = Maps.name2demandid.getFromFirst(getItemName(e));
        myApp.datapaneController.showDemandData(myApp.scenario.get_demands_with_link_id(id));
//        e.consume();
    }

    public void treeSecondClickSplit(TreeSelectEvent e){
        long id = Maps.name2splitid.getFromFirst(getItemName(e));
        myApp.datapaneController.showSplitData(myApp.scenario.getSplitWithId(id));
//        e.consume();
    }

    public void treeSecondClickActuator(TreeSelectEvent e){
        long id = Maps.name2actuatorid.getFromFirst(getItemName(e));
        myApp.datapaneController.showActuatorData(myApp.scenario.getActuatorWithId(id));
//        e.consume();
    }

    public void treeSecondClickController(TreeSelectEvent e){
        long id = Maps.name2controllerid.getFromFirst(getItemName(e));
        myApp.datapaneController.showControllerData(myApp.scenario.getControllerWithId(id));
//        e.consume();
    }

    public void treeSecondClickSensor(TreeSelectEvent e){
        long id = Maps.name2sensorid.getFromFirst(getItemName(e));
        myApp.datapaneController.showSensorData(myApp.scenario.getSensorWithId(id));
//        e.consume();
    }

    /////////////////////////////////////////////////
    // data forms
    /////////////////////////////////////////////////

    /** First click: setText ---------------- **/

    public void formFirstClickLink(Long itemId){
        clearSelection();

        Link link = myApp.scenario.getLinkWithId(itemId);
        if(link!=null)
            selectedLinks.add(link.drawLink);

        highlightSelection();
    }

    public void formFirstClickNode(Long itemId){

        clearSelection();

        // setText this node
        Node node = myApp.scenario.getNodeWithId(itemId);
        if(node!=null)
            selectedNodes.add(node.drawNode);

        highlightSelection();
    }

    public void formFirstClickSubnetwork(Long itemId){
        clearSelection();
        Set<AbstractDrawLink> drawLinks = new HashSet<>();
        Subnetwork subnetwork = myApp.scenario.getSubnetworkWithId(itemId);
        drawLinks.addAll(subnetwork
                .get_link_ids().stream()
                .map(x->myApp.scenario.getLinkWithId(x))
                .filter(Objects::nonNull)
                .map(x->x.drawLink)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
        );
        selectedLinks.addAll(drawLinks);
        highlightSelection();
    }

    public void formFirstClickDemand(Long itemId){
        clearSelection();
        DemandsForLink demandsForLink = myApp.scenario.get_demands_with_link_id(itemId);
        Link link = myApp.scenario.getLinkWithId(demandsForLink.link_id);
        selectedLinks.add(link.drawLink);
        highlightSelection();
    }

    public void formFirstClickSplit(Long itemId){
        clearSelection();
        SplitsForNode splitsForNode = myApp.scenario.getSplitWithId(itemId);
        Node node = myApp.scenario.getNodeWithId(splitsForNode.node_id);
        selectedNodes.add(node.drawNode);
        highlightSelection();
    }

    public void formFirstClickCommodity(Long itemId){
        Commodity com = myApp.scenario.getCommodityWithId(itemId);
        List<Long> subnets = com.get_subnetwork_ids();
        if(subnets.size()==1)
            formFirstClickSubnetwork(subnets.get(0));
        else
            System.err.println("This commodity does not have a unique subnetwork. Please check UI code.");
    }

    public void formFirstClickActuator(Long itemId){
        System.err.println("NOT IMPLEMENTED");
    }

    public void formFirstClickController(Long itemId){
        System.err.println("NOT IMPLEMENTED");
    }

    public void formFirstClickSensor(Long itemId){
        System.err.println("NOT IMPLEMENTED");
    }

    /** Second click: open form ---------------- **/

    public void formSecondClickLink(FormSelectEvent e){
        myApp.datapaneController.showLinkData(myApp.scenario.getLinkWithId(e.itemId));
        e.consume();
    }

    public void formSecondClickNode(FormSelectEvent e){
        myApp.datapaneController.showNodeData(myApp.scenario.getNodeWithId(e.itemId));
        e.consume();
    }

    public void formSecondClickSubnetwork(FormSelectEvent e){
        myApp.datapaneController.showSubnewtorkData(myApp.scenario.getSubnetworkWithId(e.itemId));
        e.consume();
    }

    public void formSecondClickDemand(FormSelectEvent e){
        myApp.datapaneController.showDemandData(myApp.scenario.get_demands_with_link_id(e.itemId));
        e.consume();
    }

    public void formSecondClickSplit(FormSelectEvent e){
        myApp.datapaneController.showSplitData(myApp.scenario.getSplitWithId(e.itemId));
        e.consume();
    }

    public void formSecondClickCommodity(FormSelectEvent e){
        myApp.datapaneController.showCommodityData(myApp.scenario.getCommodityWithId(e.itemId));
        e.consume();
    }

    public void formSecondClickController(FormSelectEvent e){
        myApp.datapaneController.showControllerData(myApp.scenario.getControllerWithId(e.itemId));
        e.consume();
    }

    public void formSecondClickSensor(FormSelectEvent e){
        myApp.datapaneController.showSensorData(myApp.scenario.getSensorWithId(e.itemId));
        e.consume();
    }

    public void formSecondClickActuator(FormSelectEvent e){
        myApp.datapaneController.showActuatorData(myApp.scenario.getActuatorWithId(e.itemId));
        e.consume();
    }

    /////////////////////////////////////////////////
    // private
    /////////////////////////////////////////////////

    private void clearSelection(){
        selectedNodes.forEach(x->x.unhighlight());
        selectedNodes.clear();
        selectedLinks.forEach(x->x.unhighlight());
        selectedLinks.clear();
        selectedSensors.forEach(x->x.unhighlight());
        selectedSensors.clear();

        myApp.scenarioTreeController.clearSelection();
    }

    private void highlightSelection(){
        myApp.graphpaneController.highlight(selectedLinks,selectedNodes,selectedSensors);
        myApp.scenarioTreeController.highlight(selectedLinks,selectedSensors);
        myApp.statusbarController.setText(selectedLinks,selectedNodes,selectedSensors);
    }

    private static String getItemName(TreeSelectEvent e){
        TreeView treeView = (TreeView) e.getSelected();
        TreeItem item = (TreeItem) treeView.getSelectionModel().getSelectedItem();
        return (String)item.getValue();
    }

}

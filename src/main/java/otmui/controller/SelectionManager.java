package otmui.controller;

import actuator.AbstractActuator;
import otmui.ElementType;
import otmui.MainApp;
import otmui.Maps;
import otmui.event.FormSelectEvent;
import otmui.event.GraphSelectEvent;
import otmui.event.TreeSelectEvent;
import otmui.graph.Graph;
import otmui.graph.item.AbstractDrawLink;
import otmui.graph.item.AbstractDrawNode;
import otmui.graph.item.DrawSensor;
import commodity.Commodity;
import commodity.Subnetwork;
import control.AbstractController;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import sensor.AbstractSensor;

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
    public Set<AbstractDrawNode> selectedActuators;

    public SelectionManager(MainApp myApp) {
        this.myApp = myApp;
        this.selectedNodes = new HashSet<>();
        this.selectedLinks = new HashSet<>();
        this.selectedSensors = new HashSet<>();
        this.selectedActuators = new HashSet<>();
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

    // if shift is pressed then add to highlighted items.
    // otherwise clear and add to highlighted items
    public void graphFirstClickActuator(GraphSelectEvent e){
        AbstractDrawNode drawActuator = (AbstractDrawNode) e.getSelected();
        boolean alreadySelected = selectedActuators.contains(drawActuator);
        boolean shiftPressed = e.event.isShiftDown();

        if(!shiftPressed)
            clearSelection();

        if(alreadySelected)
            selectedActuators.remove(drawActuator);
        else
            selectedActuators.add(drawActuator);

        highlightSelection();

        e.consume();
    }

    public void graphSecondClickNode(GraphSelectEvent e){
        AbstractDrawNode drawNode = (AbstractDrawNode) e.getSelected();
        common.Node node = myApp.otm.scenario.network.nodes.get(drawNode.id);
        myApp.datapaneController.showNodeData(node);
        e.consume();
    }

    public void graphSecondClickLink(GraphSelectEvent e){
        AbstractDrawLink drawLink = (AbstractDrawLink) e.getSelected();
        common.Link link = myApp.otm.scenario.network.links.get(drawLink.id);
        myApp.datapaneController.showLinkData(link);
        e.consume();
    }

    public void graphSecondClickSensor(GraphSelectEvent e){
        DrawSensor drawSensor = (DrawSensor) e.getSelected();
        AbstractSensor sensor = myApp.otm.scenario.sensors.get(drawSensor.id);
        myApp.datapaneController.showSensorData(sensor);
        e.consume();
    }

    public void graphSecondClickActuator(GraphSelectEvent e){
        AbstractDrawNode drawActuator = (AbstractDrawNode) e.getSelected();
        AbstractActuator actuator = myApp.otm.scenario.actuators.get(drawActuator.id);
        myApp.datapaneController.showActuatorData(actuator);
        e.consume();
    }

    /////////////////////////////////////////////////
    // tree clicks
    /////////////////////////////////////////////////

    public void treeFirstClick(TreeSelectEvent e){
        ObservableList<TreeItem> items = myApp.treeController.getTreeView().getSelectionModel().getSelectedItems();
        Set<AbstractDrawLink> drawLinks = new HashSet<>();
        Set<AbstractDrawNode> drawNodes = new HashSet<>();
        Set<DrawSensor> drawSensors = new HashSet<>();
        Set<AbstractDrawNode> drawActuators = new HashSet<>();

        runner.Scenario scenario = myApp.otm.scenario;
        Graph graph = myApp.graphpaneController.graphContainer.get_graph();

        // get all drawLinks and drawNodes that have been selected
        Long id, target_id;
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
                        if(graph.drawnodes.containsKey(id))
                            drawLinks.add(graph.drawlinks.get(id));
                        break;

                    case SUBNETWORK:
                        id = Maps.name2subnetworkid.getFromFirst((String)item.getValue());
                        Subnetwork subnetwork = scenario.subnetworks.get(id);
                        drawLinks.addAll(subnetwork
                                .get_link_ids().stream()
                                .map(x->graph.drawlinks.get(x))
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet())
                        );
                        break;

//                    case DEMAND:
//                        id = Maps.name2demandid.getFromFirst((String)item.getValue());
//                        AbstractDemandProfile profile = scenario.data_demands.get(id).source.link.getId();
//                        Link linkd = scenario.getLinkWithId(demandsForLink.link_id);
//                        if(linkd!=null && linkd.drawLink !=null)
//                            drawLinks.add(linkd.drawLink);
//                        break;
//
//                    case SPLIT:
//                        id = Maps.name2splitid.getFromFirst((String)item.getValue());
//                        SplitsForNode splitsForNode = scenario.getSplitWithId(id);
//                        Node nodex = scenario.getNodeWithId(splitsForNode.node_id);
//                        if(nodex!=null && nodex.drawNode !=null)
//                            drawNodes.add(nodex.drawNode);
//                        break;

                    case ACTUATOR:
                        id = Maps.name2actuatorid.getFromFirst((String)item.getValue());
                        AbstractActuator actuator = scenario.actuators.get(id);
                        target_id = actuator.target.getId();
                        if(actuator.target instanceof common.Node) {
                            if(graph.drawnodes.containsKey(target_id))
                                drawNodes.add(graph.drawnodes.get(target_id));
                        }
                        if(actuator.target instanceof common.Link){
                            if(graph.drawlinks.containsKey(target_id))
                                drawLinks.add(graph.drawlinks.get(target_id));
                        }
                        break;

                    case CONTROLLER:
                        id = Maps.name2controllerid.getFromFirst((String)item.getValue());
                        AbstractController controller = scenario.controllers.get(id);
                        for(actuator.AbstractActuator bact : controller.actuators.values()) {
                            AbstractActuator act = scenario.actuators.get(bact.id);
                            if (act == null)
                                continue;
                            target_id = act.target.getId();
                            if (bact.target instanceof common.Node)
                                drawNodes.add(graph.drawnodes.get(target_id));
                            if(bact.target instanceof common.Link)
                                drawLinks.add(graph.drawlinks.get(target_id));
                        }
                        break;

                    case SENSOR:
                        id = Maps.name2sensorid.getFromFirst((String)item.getValue());
                        drawSensors.add(graph.drawsensors.get(id));
                        break;
                }
        }

        // set selection
        selectedLinks = drawLinks;
        selectedNodes = drawNodes;
        selectedSensors = drawSensors;
        selectedActuators = drawActuators;

        // send selection to the graph
        highlightSelection();

    }

    public void treeSecondClickLink(TreeSelectEvent e){
        Long linkid = Maps.name2linkid.getFromFirst(getItemName(e));
        common.Link link = myApp.otm.scenario.network.links.get(linkid);
        myApp.datapaneController.showLinkData(link);
//        e.consume();
    }

    public void treeSecondClickCommodity(TreeSelectEvent e){
        long id = Maps.name2commodityid.getFromFirst(getItemName(e));
        myApp.datapaneController.showCommodityData(myApp.otm.scenario.commodities.get(id));
//        e.consume();
    }

    public void treeSecondClickSubnetwork(TreeSelectEvent e){
        long id = Maps.name2subnetworkid.getFromFirst(getItemName(e));
        myApp.datapaneController.showSubnewtorkData(myApp.otm.scenario.subnetworks.get(id));
//        e.consume();
    }

    public void treeSecondClickDemand(TreeSelectEvent e){
        long id = Maps.name2demandid.getFromFirst(getItemName(e));
        myApp.datapaneController.showDemandData(id,myApp.demands.get(id));
//        e.consume();
    }

    public void treeSecondClickSplit(TreeSelectEvent e){
        long id = Maps.name2splitid.getFromFirst(getItemName(e));
        myApp.datapaneController.showSplitData(id,myApp.splits.get(id));
//        e.consume();
    }

    public void treeSecondClickActuator(TreeSelectEvent e){
        long id = Maps.name2actuatorid.getFromFirst(getItemName(e));
        myApp.datapaneController.showActuatorData(myApp.otm.scenario.actuators.get(id));
//        e.consume();
    }

    public void treeSecondClickController(TreeSelectEvent e){
        long id = Maps.name2controllerid.getFromFirst(getItemName(e));
        myApp.datapaneController.showControllerData(myApp.otm.scenario.controllers.get(id));
//        e.consume();
    }

    public void treeSecondClickSensor(TreeSelectEvent e){
        long id = Maps.name2sensorid.getFromFirst(getItemName(e));
        myApp.datapaneController.showSensorData(myApp.otm.scenario.sensors.get(id));
//        e.consume();
    }

    /////////////////////////////////////////////////
    // data forms
    /////////////////////////////////////////////////

    /** First click: setText ---------------- **/

    public void formFirstClickLink(Long itemId){
        clearSelection();
        Graph graph = myApp.graphpaneController.graphContainer.get_graph();
        selectedLinks.add(graph.drawlinks.get(itemId));
        highlightSelection();
    }

    public void formFirstClickNode(Long itemId){
        clearSelection();
        Graph graph = myApp.graphpaneController.graphContainer.get_graph();
        selectedNodes.add(graph.drawnodes.get(itemId));
        highlightSelection();
    }

    public void formFirstClickSubnetwork(Long itemId){
        clearSelection();
        Set<AbstractDrawLink> drawLinks = new HashSet<>();
        Subnetwork subnetwork = myApp.otm.scenario.subnetworks.get(itemId);
        Graph graph = myApp.graphpaneController.graphContainer.get_graph();
        drawLinks.addAll(subnetwork
                .get_link_ids().stream()
                .map(x->graph.drawlinks.get(x))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
        );
        selectedLinks.addAll(drawLinks);
        highlightSelection();
    }

    public void formFirstClickDemand(Long itemId){
        clearSelection();
        selectedLinks.add(myApp.graphpaneController.graphContainer.get_graph().drawlinks.get(itemId));
        highlightSelection();
    }

    public void formFirstClickSplit(Long itemId){
        clearSelection();
        selectedNodes.add(myApp.graphpaneController.graphContainer.get_graph().drawnodes.get(itemId));
        highlightSelection();
    }

    public void formFirstClickCommodity(Long itemId){
        Commodity com = myApp.otm.scenario.commodities.get(itemId);
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
        myApp.datapaneController.showLinkData(myApp.otm.scenario.network.links.get(e.itemId));
        e.consume();
    }

    public void formSecondClickNode(FormSelectEvent e){
        myApp.datapaneController.showNodeData(myApp.otm.scenario.network.nodes.get(e.itemId));
        e.consume();
    }

    public void formSecondClickSubnetwork(FormSelectEvent e){
        myApp.datapaneController.showSubnewtorkData(myApp.otm.scenario.subnetworks.get(e.itemId));
        e.consume();
    }

    public void formSecondClickDemand(FormSelectEvent e){
        myApp.datapaneController.showDemandData(e.itemId,myApp.demands.get(e.itemId));
        e.consume();
    }

    public void formSecondClickSplit(FormSelectEvent e){
        myApp.datapaneController.showSplitData(e.itemId,myApp.splits.get(e.itemId));
        e.consume();
    }

    public void formSecondClickCommodity(FormSelectEvent e){
        myApp.datapaneController.showCommodityData(myApp.otm.scenario.commodities.get(e.itemId));
        e.consume();
    }

    public void formSecondClickController(FormSelectEvent e){
        myApp.datapaneController.showControllerData(myApp.otm.scenario.controllers.get(e.itemId));
        e.consume();
    }

    public void formSecondClickSensor(FormSelectEvent e){
        myApp.datapaneController.showSensorData(myApp.otm.scenario.sensors.get(e.itemId));
        e.consume();
    }

    public void formSecondClickActuator(FormSelectEvent e){
        myApp.datapaneController.showActuatorData(myApp.otm.scenario.actuators.get(e.itemId));
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
        selectedActuators.forEach(x->x.unhighlight());
        selectedActuators.clear();

        myApp.treeController.clearSelection();
    }

    private void highlightSelection(){
        myApp.graphpaneController.highlight(selectedLinks,selectedNodes,selectedSensors,selectedActuators);
        myApp.treeController.highlight(selectedLinks,selectedSensors,selectedActuators);
        myApp.statusbarController.setText(selectedLinks,selectedNodes,selectedSensors,selectedActuators);
    }

    private static String getItemName(TreeSelectEvent e){
        TreeView treeView = (TreeView) e.getSelected();
        TreeItem item = (TreeItem) treeView.getSelectionModel().getSelectedItem();
        return (String)item.getValue();
    }

}

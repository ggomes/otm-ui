package otmui.controller;

import javafx.scene.Scene;
import otmui.ElementType;
import otmui.MainApp;
import otmui.Maps;
import otmui.event.FormSelectEvent;
import otmui.event.GraphSelectEvent;
import otmui.event.TreeSelectEvent;
import otmui.graph.Graph;
import commodity.Commodity;
import commodity.Subnetwork;
import control.AbstractController;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import otmui.item.*;

import java.util.*;
import java.util.stream.Collectors;

public class SelectionManager {

    public MainApp myApp;

//    public Set<AbstractDrawNode> selectedNodes;
//    public Set<AbstractDrawLink> selectedLinks;
//    public Set<DrawSensor> selectedSensors;
//    public Set<AbstractDrawActuator> selectedActuators;

    public Map<Class,Set<AbstractItem>> selection;

    public SelectionManager() {
        selection = new HashMap<>();
        selection.put(AbstractNode.class,new HashSet<>());
        selection.put(AbstractLink.class,new HashSet<>());
        selection.put(DrawSensor.class,new HashSet<>());
        selection.put(AbstractActuator.class,new HashSet<>());

//        this.selectedNodes = new HashSet<>();
//        this.selectedLinks = new HashSet<>();
//        this.selectedSensors = new HashSet<>();
//        this.selectedActuators = new HashSet<>();
    }

    public void attach_event_listeners(MainApp myApp){
        this.myApp = myApp;

        Scene scene = myApp.stage.getScene();

        scene.addEventFilter(GraphSelectEvent.CLICK1, e->graphFirstClick(e));
        scene.addEventFilter(TreeSelectEvent.CLICK1, e->treeFirstClick(e));

        // form first click
        scene.addEventFilter(FormSelectEvent.CLICK1, e->formFirstClick(e));

//        scene.addEventFilter(FormSelectEvent.CLICK1_LINK, e->formFirstClickLink(e.itemId));
//        scene.addEventFilter(FormSelectEvent.CLICK1_NODE, e->formFirstClickNode(e.itemId));
//        scene.addEventFilter(FormSelectEvent.CLICK1_SUBNETWORK, e->formFirstClickSubnetwork(e.itemId));
//        scene.addEventFilter(FormSelectEvent.CLICK1_COMMODITY, e->formFirstClickCommodity(e.itemId));
//        scene.addEventFilter(FormSelectEvent.CLICK1_DEMAND, e->formFirstClickDemand(e.itemId));
//        scene.addEventFilter(FormSelectEvent.CLICK1_SPLIT, e->formFirstClickSplit(e.itemId));
//        scene.addEventFilter(FormSelectEvent.CLICK1_ACTUATOR, e->formFirstClickActuator(e.itemId));
//        scene.addEventFilter(FormSelectEvent.CLICK1_CONTROLLER, e->formFirstClickController(e.itemId));
//        scene.addEventFilter(FormSelectEvent.CLICK1_SENSOR, e->formFirstClickSensor(e.itemId));

    }

    public void graphFirstClick(GraphSelectEvent e){


        // clear if shift is not pressed
        boolean shift_pressed = e.event.isShiftDown();
        boolean selected;

        AbstractItem item = (AbstractItem) e.getSelected();
        Set<AbstractItem> selectionPool = selection.get(item.getClass());
//        AbstractDrawItem x = (AbstractDrawItem) item;

//        if(item instanceof AbstractDrawNode){
//            selectionPool = selectedNodes;
//            x = (AbstractDrawItem) item;
//        }

//        if(item instanceof AbstractDrawNode){
//            AbstractDrawNode x = (AbstractDrawNode) item;
//            selected = selectedNodes.contains(x);
//            if(selected)
//                if(shift_pressed)
//                    selectedNodes.remove(x);
//                else
//                    clearSelection();
//            else
//                if(shift_pressed)
//                    selectedNodes.add(x);
//                else
//                    clearSelection();
//                    selectedNodes.add(x);
//        }

//        if(item instanceof AbstractDrawLink){
//            AbstractDrawLink x = (AbstractDrawLink) item;
//            if(selectedLinks.contains(x))
//                selectedLinks.remove(x);
//            else
//                selectedLinks.add(x);
//        }
//
//        if(item instanceof DrawSensor){
//            DrawSensor x = (DrawSensor) item;
//            if(selectedSensors.contains(x))
//                selectedSensors.remove(x);
//            else
//                selectedSensors.add(x);
//        }
//
//        if(item instanceof AbstractDrawActuator){
//            AbstractDrawActuator x = (AbstractDrawActuator) item;
//            if(selectedActuators.contains(x))
//                selectedActuators.remove(x);
//            else
//                selectedActuators.add(x);
//        }


        selected = selectionPool.contains(item);
        if(selected)
            if(shift_pressed)
                selectionPool.remove(item);
            else
                clearSelection();
        else
        if(shift_pressed)
            selectionPool.add(item);
        else
            clearSelection();
        selectionPool.add(item);

        highlightSelection();

        e.consume();




//        ObservableList<TreeItem> items = myApp.treeController.getTreeView().getSelectionModel().getSelectedItems();
//        Set<AbstractDrawLink> drawLinks = new HashSet<>();
//        Set<AbstractDrawNode> drawNodes = new HashSet<>();
//        Set<DrawSensor> drawSensors = new HashSet<>();
//        Set<AbstractDrawNode> drawActuators = new HashSet<>();

//        runner.Scenario scenario = myApp.otm.scenario;
//        Graph graph = myApp.graphpaneController.graphContainer.get_graph();

        // get all drawLinks and drawNodes that have been selected
//        Long id, target_id;
//        for(TreeItem item : items){
//            if(item==null)
//                continue;
//            TreeItem parent = item.getParent();
//            if(parent==null)
//                continue;
//            ElementType elementType = Maps.elementName.getFromSecond((String) parent.getValue());
//            if(elementType!=null)
//                switch(elementType){
//
//                    case LINK:
//                        id = Maps.name2linkid.getFromFirst((String)item.getValue());
//                        if(graph.drawnodes.containsKey(id))
//                            drawLinks.add(graph.drawlinks.get(id));
//                        break;
//
//                    case SUBNETWORK:
//                        id = Maps.name2subnetworkid.getFromFirst((String)item.getValue());
//                        Subnetwork subnetwork = scenario.subnetworks.get(id);
//                        drawLinks.addAll(subnetwork
//                                .get_link_ids().stream()
//                                .map(x->graph.drawlinks.get(x))
//                                .filter(Objects::nonNull)
//                                .collect(Collectors.toSet())
//                        );
//                        break;
//
////                    case DEMAND:
////                        id = Maps.name2demandid.getFromFirst((String)item.getValue());
////                        AbstractDemandProfile profile = scenario.data_demands.get(id).source.link.getId();
////                        Link linkd = scenario.getLinkWithId(demandsForLink.link_id);
////                        if(linkd!=null && linkd.drawLink !=null)
////                            drawLinks.add(linkd.drawLink);
////                        break;
////
////                    case SPLIT:
////                        id = Maps.name2splitid.getFromFirst((String)item.getValue());
////                        SplitsForNode splitsForNode = scenario.getSplitWithId(id);
////                        Node nodex = scenario.getNodeWithId(splitsForNode.node_id);
////                        if(nodex!=null && nodex.drawNode !=null)
////                            drawNodes.add(nodex.drawNode);
////                        break;
//
//                    case ACTUATOR:
//                        id = Maps.name2actuatorid.getFromFirst((String)item.getValue());
//                        AbstractActuator actuator = scenario.actuators.get(id);
//                        target_id = actuator.target.getId();
//                        if(actuator.target instanceof common.Node) {
//                            if(graph.drawnodes.containsKey(target_id))
//                                drawNodes.add(graph.drawnodes.get(target_id));
//                        }
//                        if(actuator.target instanceof common.Link){
//                            if(graph.drawlinks.containsKey(target_id))
//                                drawLinks.add(graph.drawlinks.get(target_id));
//                        }
//                        break;
//
//                    case CONTROLLER:
//                        id = Maps.name2controllerid.getFromFirst((String)item.getValue());
//                        AbstractController controller = scenario.controllers.get(id);
//                        for(actuator.AbstractActuator bact : controller.actuators.values()) {
//                            AbstractActuator act = scenario.actuators.get(bact.id);
//                            if (act == null)
//                                continue;
//                            target_id = act.target.getId();
//                            if (bact.target instanceof common.Node)
//                                drawNodes.add(graph.drawnodes.get(target_id));
//                            if(bact.target instanceof common.Link)
//                                drawLinks.add(graph.drawlinks.get(target_id));
//                        }
//                        break;
//
//                    case SENSOR:
//                        id = Maps.name2sensorid.getFromFirst((String)item.getValue());
//                        drawSensors.add(graph.drawsensors.get(id));
//                        break;
//                }
//        }
//
//        // set selection
//        selectedLinks = drawLinks;
//        selectedNodes = drawNodes;
//        selectedSensors = drawSensors;
//        selectedActuators = drawActuators;
//
//        // send selection to the graph
//        highlightSelection();
    }

    public void treeFirstClick(TreeSelectEvent e){
        ObservableList<TreeItem> items = myApp.treeController.getTreeView().getSelectionModel().getSelectedItems();
        Set<AbstractLink> drawLinks = new HashSet<>();
        Set<AbstractNode> drawNodes = new HashSet<>();
        Set<DrawSensor> drawSensors = new HashSet<>();
        Set<AbstractActuator> drawActuators = new HashSet<>();

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
                        actuator.AbstractActuator actuator = scenario.actuators.get(id);
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
                            actuator.AbstractActuator act = scenario.actuators.get(bact.id);
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

//        // set selection
//        selectedLinks = drawLinks;
//        selectedNodes = drawNodes;
//        selectedSensors = drawSensors;
//        selectedActuators = drawActuators;

        // send selection to the graph
        highlightSelection();

    }

    public void formFirstClick(FormSelectEvent e){

//        Object item = e.getItem();
//
//        // always clear
//        clearSelection();
//
//        if(item instanceof AbstractDrawNode)
//            selectedNodes.add((AbstractDrawNode) item);
//
//        if(item instanceof AbstractDrawLink)
//            selectedLinks.add((AbstractDrawLink)item);
//
//        if(item instanceof DrawSensor)
//            selectedSensors.add((DrawSensor) item);
//
//        if(item instanceof AbstractDrawActuator)
//            selectedActuators.add((AbstractDrawActuator) item);
//
//        highlightSelection();
//
//        e.consume();

    }


    public void formFirstClickSubnetwork(Long itemId){
//        clearSelection();
//        Set<AbstractDrawLink> drawLinks = new HashSet<>();
//        Subnetwork subnetwork = myApp.otm.scenario.subnetworks.get(itemId);
//        Graph graph = myApp.graphpaneController.graphContainer.get_graph();
//        drawLinks.addAll(subnetwork
//                .get_link_ids().stream()
//                .map(x->graph.drawlinks.get(x))
//                .filter(Objects::nonNull)
//                .collect(Collectors.toSet())
//        );
//        selectedLinks.addAll(drawLinks);
//        highlightSelection();
    }

//    public void formFirstClickDemand(Long itemId){
//        clearSelection();
//        selectedLinks.add(myApp.graphpaneController.graphContainer.get_graph().drawlinks.get(itemId));
//        highlightSelection();
//    }
//
//    public void formFirstClickSplit(Long itemId){
//        clearSelection();
//        selectedNodes.add(myApp.graphpaneController.graphContainer.get_graph().drawnodes.get(itemId));
//        highlightSelection();
//    }

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

    /////////////////////////////////////////////////
    // private
    /////////////////////////////////////////////////

    private void clearSelection(){
        for(Set<AbstractItem> X : selection.values()){
            X.forEach(x->x.unhighlight());
            X.clear();
        }


//        selectedNodes.forEach(x->x.unhighlight());
//        selectedNodes.clear();
//        selectedLinks.forEach(x->x.unhighlight());
//        selectedLinks.clear();
//        selectedSensors.forEach(x->x.unhighlight());
//        selectedSensors.clear();
//        selectedActuators.forEach(x->x.unhighlight());
//        selectedActuators.clear();

        myApp.treeController.clearSelection();
    }

    private void highlightSelection(){
        myApp.graphpaneController.highlight(selection);
        myApp.treeController.highlight(selection);
        myApp.statusbarController.setText(selection);
    }


}

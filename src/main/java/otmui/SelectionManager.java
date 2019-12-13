package otmui;

import actuator.AbstractActuator;
import commodity.Subnetwork;
import control.AbstractController;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import otmui.event.FormSelectEvent;
import otmui.event.GraphSelectEvent;
import otmui.event.TreeDoubleClickEvent;
import otmui.event.TreeSingleClickEvent;
import commodity.Commodity;
import otmui.item.*;
import sensor.AbstractSensor;

import java.util.*;

public class SelectionManager {

    public MainApp myApp;
    public Map<ItemType,Set<AbstractItem>> selection;

    public SelectionManager(MainApp myApp) {

        this.myApp = myApp;

        clear_selection();

        // event listeners
        Scene scene = myApp.stage.getScene();
        scene.addEventFilter(GraphSelectEvent.CLICK1, e-> graphSingleClick(e));
        scene.addEventFilter(TreeSingleClickEvent.TREE_SINGLE, e-> treeSingleClick(e));
        scene.addEventFilter(TreeDoubleClickEvent.TREE_DOUBLE, e->treeDoubleClick(e));
        scene.addEventFilter(FormSelectEvent.CLICK1, e->formFirstClick(e));
    }

    private void clear_selection(){
        selection = new HashMap<>();
        for(ItemType type : myApp.data.items.keySet())
            selection.put(type,new HashSet<>());
    }

    public void graphSingleClick(GraphSelectEvent e){

        AbstractItem item = (AbstractItem) e.getSelected();
        Set<AbstractItem> selectionPool = selection.get(item.getType());

        boolean selected = selectionPool.contains(item);
        boolean shift_pressed = e.event.isShiftDown();
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
    }

    public void treeSingleClick(TreeSingleClickEvent e){

        if(e.items ==null)
            return;

        clear_selection();

        ObservableList<TreeItem> treeitems = e.items;
        for(TreeItem treeitem : treeitems){
            if(!treeitem.isLeaf())
                continue;
            TypeId typeId = myApp.data.getTypeId((String) treeitem.getValue());
            AbstractItem item = myApp.data.getItem(typeId);
            selection.get(typeId.type).add(item);
        }

        highlightSelection();

        e.consume();
    }

    public void treeDoubleClick(TreeDoubleClickEvent e) {

        clear_selection();

        if(!e.item.isLeaf())
            return;

        TypeId typeId = myApp.data.getTypeId((String) e.item.getValue());
        AbstractItem item = myApp.data.getItem(typeId);
        selection.get(typeId.type).add(item);

        highlightSelection();

        switch(typeId.type){
            case node:
                common.Node node = myApp.otm.scenario.network.nodes.get(typeId.id);
                myApp.formController.showNodeData(node);
                break;

            case link:
                common.Link link = myApp.otm.scenario.network.links.get(typeId.id);
                myApp.formController.showLinkData(link);
                break;

            case sensor:
                AbstractSensor sensor = myApp.otm.scenario.sensors.get(typeId.id);
                myApp.formController.showSensorData(sensor);
                break;

            case actuator:
                AbstractActuator actuator = myApp.otm.scenario.actuators.get(typeId.id);
                myApp.formController.showActuatorData(actuator);
                break;

            case controller:
                AbstractController controller = myApp.otm.scenario.controllers.get(typeId.id);
                myApp.formController.showControllerData( controller);
                break;

            case commodity:
                commodity.Commodity commodity = myApp.otm.scenario.commodities.get(typeId.id);
                myApp.formController.showCommodityData(commodity);
                break;

            case subnetwork:
                Subnetwork subnetwork = myApp.otm.scenario.subnetworks.get(typeId.id);
                myApp.formController.showSubnewtorkData(subnetwork);
                break;

//            case demand:
//                long link_id,
//                Set<DemandInfo > demands
//                myApp.formController.showDemandData( link_id,demands);
//                break;
//
//            case split:
//                long node_id,Set<SplitInfo > splits
//                myApp.formController.showSplitData( node_id,splits);
//                break;
        }
    }





















    public void formFirstClick(FormSelectEvent e){

        clear_selection();


        Object item = e.getItem();

        System.out.println(item.getClass());


//        TypeId typeId = myApp.itempool.getTypeId((String) e.item.getValue());
//        AbstractItem item = myApp.itempool.getItem(typeId);
//        selection.get(typeId.type).add(item);

//        highlightSelection();



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

        highlightSelection();

        e.consume();

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
        System.out.println("COMMENTED: clearSelection");

//        for(Set<AbstractItem> X : selection.values()){
//            X.forEach(x->x.unhighlight());
//            X.clear();
//        }

        myApp.treeController.clearSelection();
    }

    private void highlightSelection(){
        myApp.graphController.highlight(selection);
        myApp.treeController.highlight(selection);
        myApp.statusbarController.setText(selection);
    }


}

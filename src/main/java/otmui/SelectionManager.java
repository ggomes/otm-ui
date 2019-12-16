package otmui;

import actuator.AbstractActuator;
import commodity.Subnetwork;
import control.AbstractController;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import otmui.event.*;
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
        scene.addEventFilter(GraphClickEvent.CLICK1, e-> graphClick1(e));
        scene.addEventFilter(GraphClickEvent.CLICK2, e-> graphClick2(e));
        scene.addEventFilter(TreeClickEvent.CLICK1, e-> treeClick1(e));
        scene.addEventFilter(TreeClickEvent.CLICK2, e-> treeClick2(e));
        scene.addEventFilter(FormSelectEvent.CLICK1, e-> formClick1(e));
        scene.addEventFilter(FormSelectEvent.CLICK2, e-> formClick2(e));
    }

    private void clear_selection(){
        selection = new HashMap<>();
        for(ItemType type : myApp.data.items.keySet())
            selection.put(type,new HashSet<>());
    }

    public void graphClick1(GraphClickEvent e){
        Set<AbstractItem> selectionPool = selection.get(e.item.getType());

        boolean selected = selectionPool.contains(e.item);
        boolean shift_pressed = e.event.isShiftDown();
        if(selected)
            if(shift_pressed)
                selectionPool.remove(e.item);
            else
                clearSelection();
        else
        if(shift_pressed)
            selectionPool.add(e.item);
        else
            clearSelection();
        selectionPool.add(e.item);

        e.consume();
        Event.fireEvent(myApp.stage.getScene(),new DoHighlightSelection(DoHighlightSelection.HIGHLIGHT_ANY,selection));
    }

    public void graphClick2(GraphClickEvent e){

        clear_selection();

        AbstractItem item = e.item;
        selection.get(item.getType()).add(item);

        e.consume();
        Event.fireEvent(myApp.stage.getScene(),new DoHighlightSelection(DoHighlightSelection.HIGHLIGHT_ANY,selection));
        Event.fireEvent(myApp.stage.getScene(),new DoOpenFormEvent(DoOpenFormEvent.OPEN,item));
    }

    public void treeClick1(TreeClickEvent e){

        if(e.items ==null)
            return;

        clear_selection();

        ObservableList<TreeItem> treeitems = e.items;
        for(TreeItem treeitem : treeitems){
            if(!treeitem.isLeaf())
                continue;
            TypeId typeId = myApp.data.getTypeId((String) treeitem.getValue());
            if(typeId.type==null)
                continue;
            AbstractItem item = myApp.data.getItem(typeId);
            selection.get(typeId.type).add(item);
        }

        e.consume();
        Event.fireEvent(myApp.stage.getScene(),new DoHighlightSelection(DoHighlightSelection.HIGHLIGHT_ANY,selection));
    }

    public void treeClick2(TreeClickEvent e) {

        clear_selection();

        if(!e.item.isLeaf())
            return;

        TypeId typeId = myApp.data.getTypeId((String) e.item.getValue());
        AbstractItem item = myApp.data.getItem(typeId);
        selection.get(typeId.type).add(item);

        e.consume();
        Event.fireEvent(myApp.stage.getScene(),new DoHighlightSelection(DoHighlightSelection.HIGHLIGHT_ANY,selection));
        Event.fireEvent(myApp.stage.getScene(),new DoOpenFormEvent(DoOpenFormEvent.OPEN,item));
    }

    public void formClick1(FormSelectEvent e){

        System.out.println("SelectionManager formClick1");


//        clear_selection();
//
//
//        Object item = e.getItem();
//
//        TypeId typeId = myApp.itempool.getTypeId((String) e.item.getValue());
//        AbstractItem item = myApp.itempool.getItem(typeId);
//        selection.get(typeId.type).add(item);
//
//        highlightSelection();
//
//
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
//        e.consume();
//        Event.fireEvent(myApp.stage.getScene(),new DoHighlightSelection(DoHighlightSelection.HIGHLIGHT_ANY,selection));

    }

    public void formClick2(FormSelectEvent e){
        System.out.println("SelectionManager formClick2");
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

//    private void highlightSelection(){
//        myApp.graphController.highlight(selection);
//        myApp.treeController.highlight(selection);
//        myApp.statusbarController.setText(selection);
//    }


}

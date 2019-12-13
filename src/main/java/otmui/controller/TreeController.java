package otmui.controller;

import java.util.*;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import otmui.Data;
import otmui.ItemType;
import otmui.MainApp;
import otmui.event.*;
import otmui.item.AbstractItem;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class TreeController {

    private MainApp myApp;

    @FXML
    private TreeView scenariotree;

    private Map<ItemType,Map<Long,TreeItem<String>>> tree;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public void initialize(MainApp myApp){
        this.myApp = myApp;

        scenariotree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Scene scene = myApp.stage.getScene();
        scene.addEventFilter(NewScenarioEvent.SCENARIO_LOADED, e->loadScenario(e.data) );
        scene.addEventFilter(NewElementEvent.NEW_NODE, e->add_node(e.item));
//        scene.addEventFilter(DeleteElementEvent.REMOVE_LINK, e->remove_link((common.Link)e.item));
    }

    /////////////////////////////////////////////////
    // event handlers
    /////////////////////////////////////////////////

    private void loadScenario(Data data){
        tree = new HashMap<>();

        // populate the tree
        TreeItem<String> rootItem = new TreeItem<>("scenario");
        rootItem.setExpanded(false);

////////////////////////
        for(ItemType type : ItemType.values()){

            if(!data.items.containsKey(type))
                continue;

            // dont put nodes in the tree
            if(type.equals(ItemType.node))
                continue;

            TreeItem<String> treebranch = new TreeItem<>(Data.itemNames.AtoB(type));
            Map<Long,TreeItem<String>> leaves = new HashMap<>();
            tree.put(type,leaves);
            rootItem.getChildren().add(treebranch);

            for (AbstractItem x : data.items.get(type).values()) {
                TreeItem item = new TreeItem<>(x.getName());
                leaves.put(x.id, item);
                treebranch.getChildren().add(item);
            }
        }
        ////////////////////////


//        // commodities
//        TreeItem<String> comms_tree = new TreeItem<>(Data.itemNames.AtoB(ItemType.commodity));
//        Map<Long,TreeItem<String>> comms_map = new HashMap<>();
//        tree.put(ItemType.commodity,comms_map);
//        rootItem.getChildren().add(comms_tree);
//        for (AbstractItem comm : data.items.get(ItemType.commodity).values()) {
//            TreeItem item  = new TreeItem<>(comm.getName());
//            comms_map.put(comm.id,item);
//            comms_tree.getChildren().add(item);
//        }
//
//        // links
//        TreeItem<String> links_tree = new TreeItem<>(Data.itemNames.AtoB(ItemType.link));
//        Map<Long,TreeItem<String>> links_map = new HashMap<>();
//        tree.put(ItemType.link,links_map);
//        rootItem.getChildren().add(links_tree);
//        for (AbstractItem link : data.items.get(ItemType.link).values()) {
//            TreeItem item  =new TreeItem<>(link.getName());
//            links_map.put(link.id,item);
//            links_tree.getChildren().add(item);
//        }
//
//        // subnetwork
//        TreeItem<String> subnets_tree = new TreeItem<>(Data.itemNames.AtoB(ItemType.subnetwork));
//        Map<Long,TreeItem<String>> subnets_map = new HashMap<>();
//        tree.put(ItemType.subnetwork,subnets_map);
//        rootItem.getChildren().add(subnets_tree);
//        for (AbstractItem subnet : data.items.get(ItemType.subnetwork).values()) {
//            TreeItem item  =new TreeItem<>(Data.getName(subnet));
//            subnets_map.put(subnet.id,item);
//            subnets_tree.getChildren().add(item);
//        }
//
//        // demands
////        TreeItem<String> demands_tree = new TreeItem<>(ItemPool.itemNames.AtoB(ItemType.demand));
////        Map<Long,TreeItem<String>> demands_map = new HashMap<>();
////        tree.put(ItemType.demand,demands_map);
////        rootItem.getChildren().add(demands_tree);
////        for (AbstractDemandProfile profile : otm.scenario.data_demands.values()) {
////            TreeItem item  =new TreeItem<>(ItemPool.getName(profile));
////            demands_map.put(profile.source.link.getId(),item);
////            demands_tree.getChildren().add(item);
////        }
//
//        // TODO splitsForNode
////        spt = new TreeItem<>(Maps.elementName.getFromFirst(ElementType.SPLIT));
////        tree.put(ItemType.,);
////        rootItem.getChildren().add(spt);
////        for (SplitsForNode s : scenario.getSplits())
////            spt.getChildren().add(new TreeItem<>(Maps.name2splitid.getFromSecond(s.getId())));
//
//        // actuators
//        TreeItem<String> actuators_tree = new TreeItem<>(Data.itemNames.AtoB(ItemType.actuator));
//        Map<Long,TreeItem<String>> actuators_map = new HashMap<>();
//        tree.put(ItemType.actuator,actuators_map);
//        rootItem.getChildren().add(actuators_tree);
//        for (AbstractActuator act : otm.scenario.actuators.values()) {
//            TreeItem item  =new TreeItem<>(Data.getName(act));
//            actuators_map.put(act.getId(),item);
//            actuators_tree.getChildren().add(new TreeItem<>(Data.getName(act)));
//        }
//
//        // controllers
//        TreeItem<String> controllers_tree = new TreeItem<>(Data.itemNames.AtoB(ItemType.controller));
//        Map<Long,TreeItem<String>> controllers_map = new HashMap<>();
//        tree.put(ItemType.controller,controllers_map);
//        rootItem.getChildren().add(controllers_tree);
//        for(AbstractController cntr : otm.scenario.controllers.values()) {
//            TreeItem item  =new TreeItem<>(Data.getName(cntr));
//            controllers_map.put(cntr.getId(),item);
//            controllers_tree.getChildren().add(new TreeItem<>(Data.getName(cntr)));
//        }
//
//        // sensors
//        TreeItem<String> sensors_tree = new TreeItem<>(Data.itemNames.AtoB(ItemType.sensor));
//        Map<Long,TreeItem<String>> sensors_map = new HashMap<>();
//        tree.put(ItemType.sensor,sensors_map);
//        rootItem.getChildren().add(sensors_tree);
//        for (AbstractSensor sens : otm.scenario.sensors.values()) {
//            TreeItem item = new TreeItem<>(Data.getName(sens));
//            sensors_map.put(sens.getId(), item);
//            sensors_tree.getChildren().add(new TreeItem<>(Data.getName(sens)));
//        }

        scenariotree.setRoot(rootItem);

    }

    public void mouseClick(MouseEvent mouseEvent){

        // only register left click
        if (!mouseEvent.getButton().equals(MouseButton.PRIMARY))
            return;

        int clickcount = mouseEvent.getClickCount();
        ObservableList<TreeItem> items = (ObservableList<TreeItem>) getTreeView().getSelectionModel().getSelectedItems();

        // fire event for first click
        if(clickcount==1) {
            Event.fireEvent(mouseEvent.getTarget(), new TreeSingleClickEvent(TreeSingleClickEvent.TREE_SINGLE, items, mouseEvent));
        }

        if(clickcount==2){
            TreeItem item = (TreeItem) getTreeView().getSelectionModel().getSelectedItem();
            if(item==null)
                return;
            Event.fireEvent(mouseEvent.getTarget(),new TreeDoubleClickEvent(TreeDoubleClickEvent.TREE_DOUBLE,item,mouseEvent));


//            TreeItem parent = item.getParent();
//            if(parent!=null) {
//                ItemType itemType = ItemPool.itemNames.BtoA((String) parent.getValue());
//                if(itemType !=null)
//                    switch (itemType) {
//                        case link:
//                            Event.fireEvent(mouseEvent.getTarget(),new TreeDoubleClickEvent(TreeDoubleClickEvent.CLICK2_LINK,item,mouseEvent));
//                            break;
//                        case commodity:
//                            Event.fireEvent(mouseEvent.getTarget(),new TreeDoubleClickEvent(TreeDoubleClickEvent.CLICK2_COMMODITY,item,mouseEvent));
//                            break;
//                        case subnetwork:
//                            Event.fireEvent(mouseEvent.getTarget(),new TreeDoubleClickEvent(TreeDoubleClickEvent.CLICK2_SUBNETWORK,item,mouseEvent));
//                            break;
//                        case split:
//                            Event.fireEvent(mouseEvent.getTarget(),new TreeDoubleClickEvent(TreeDoubleClickEvent.CLICK2_SPLIT,item,mouseEvent));
//                            break;
//                        case demand:
//                            Event.fireEvent(mouseEvent.getTarget(),new TreeDoubleClickEvent(TreeDoubleClickEvent.CLICK2_DEMAND,item,mouseEvent));
//                            break;
//                        case actuator:
//                            Event.fireEvent(mouseEvent.getTarget(),new TreeDoubleClickEvent(TreeDoubleClickEvent.CLICK2_ACTUATOR,item,mouseEvent));
//                            break;
//                        case controller:
//                            Event.fireEvent(mouseEvent.getTarget(),new TreeDoubleClickEvent(TreeDoubleClickEvent.CLICK2_CONTROLLER,item,mouseEvent));
//                            break;
//                        case sensor:
//                            Event.fireEvent(mouseEvent.getTarget(),new TreeDoubleClickEvent(TreeDoubleClickEvent.CLICK2_SENSOR,item,mouseEvent));
//                            break;
//
//                        default:
//                            System.err.println("????");
//                    }
//            }
        }

        mouseEvent.consume();

    }

    private void reset(){
        clearSelection();
    }

    public void add_node(Object o){
        System.out.println("HELLO!!!");
//        links_tree.getChildren().add(new TreeItem<>(Maps.name2linkid.getFromSecond(link.getId())));
    }


//    public void add_link(common.Link link){
////        links_tree.getChildren().add(new TreeItem<>(ItemPool.getName(link)));
//    }

//    public void remove_link(common.Link link){
//        TreeItem item = new TreeItem<>(link.get);
////        links_tree.getChildren().remove(item);
//    }

    /////////////////////////////////////////////////
    // focusing and hihglighting
    /////////////////////////////////////////////////

    public void focusGraph(ActionEvent e){
        myApp.graphController.focusGraphOnSelection();
    }

    public void highlight(Map<ItemType,Set<AbstractItem>> selection){
        MultipleSelectionModel<TreeItem<String>> model = scenariotree.getSelectionModel();
        for(Set<AbstractItem> X : selection.values())
            X.forEach(item->model.select(tree.get(item.getType()).get(item.id)));
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

}

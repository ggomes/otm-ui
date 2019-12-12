package otmui.controller;

import java.net.URL;
import java.util.*;

import actuator.AbstractActuator;
import api.OTMdev;
import javafx.scene.Scene;
import otmui.ItemPool;
import otmui.ItemType;
import otmui.MainApp;
import otmui.event.DeleteElementEvent;
import otmui.event.NewElementEvent;
import otmui.event.NewScenarioEvent;
import otmui.event.TreeSelectEvent;
import otmui.item.AbstractItem;
import commodity.Subnetwork;
import control.AbstractController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import profiles.AbstractDemandProfile;
import sensor.AbstractSensor;

public class TreeController implements Initializable {

    private MainApp myApp;

    @FXML
    private TreeView scenariotree;

    private TreeItem<String> comms_tree;
    private TreeItem<String> links_tree;
    private TreeItem<String> subnets_tree;
    private TreeItem<String> demands_tree;
    private TreeItem<String> actuators_tree;
    private TreeItem<String> controllers_tree;
    private TreeItem<String> sensors_tree;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scenariotree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void attach_event_listeners(MainApp myApp){
        this.myApp = myApp;
        Scene scene = myApp.stage.getScene();
        scene.addEventFilter(NewScenarioEvent.SCENARIO_LOADED, e->loadScenario(e.otm) );
        scene.addEventFilter(NewElementEvent.NEW_NODE, e->add_node(e.item));
        scene.addEventFilter(DeleteElementEvent.REMOVE_LINK, e->remove_link((common.Link)e.item));
    }

    /////////////////////////////////////////////////
    // event triggered
    /////////////////////////////////////////////////

    private void loadScenario(OTMdev otm){

        // populate the tree
        TreeItem<String> rootItem = new TreeItem<>("scenario");
        rootItem.setExpanded(false);

        // commodities
        comms_tree = new TreeItem<>(ItemPool.itemNames.AtoB(ItemType.commodity));
        rootItem.getChildren().add(comms_tree);
        for (commodity.Commodity comm : otm.scenario.commodities.values())
            comms_tree.getChildren().add(new TreeItem<>(ItemPool.getName(comm)));

        // links
        links_tree = new TreeItem<>(ItemPool.itemNames.AtoB(ItemType.link));
        rootItem.getChildren().add(links_tree);
        List<common.Link> links = new ArrayList<>();
        links.addAll(otm.scenario.network.links.values());
        // TODO Collections.sort(links);
        for (common.Link link : links)
            links_tree.getChildren().add(new TreeItem<>(ItemPool.getName(link)));

        // subnetwork
        subnets_tree = new TreeItem<>(ItemPool.itemNames.AtoB(ItemType.subnetwork));
        rootItem.getChildren().add(subnets_tree);
        for (Subnetwork subnet : otm.scenario.subnetworks.values())
            subnets_tree.getChildren().add(new TreeItem<>(ItemPool.getName(subnet)));

        // demands
        demands_tree = new TreeItem<>(ItemPool.itemNames.AtoB(ItemType.demand));
        rootItem.getChildren().add(demands_tree);
        for (AbstractDemandProfile profile : otm.scenario.data_demands.values())
            demands_tree.getChildren().add(new TreeItem<>(ItemPool.getName(profile)));

        // TODO splitsForNode
//        spt = new TreeItem<>(Maps.elementName.getFromFirst(ElementType.SPLIT));
//        rootItem.getChildren().add(spt);
//        for (SplitsForNode s : scenario.getSplits())
//            spt.getChildren().add(new TreeItem<>(Maps.name2splitid.getFromSecond(s.getId())));

        // actuators
        actuators_tree = new TreeItem<>(ItemPool.itemNames.AtoB(ItemType.actuator));
        rootItem.getChildren().add(actuators_tree);
        for (AbstractActuator act : otm.scenario.actuators.values())
            actuators_tree.getChildren().add(new TreeItem<>(ItemPool.getName(act)));

        // controllers
        controllers_tree = new TreeItem<>(ItemPool.itemNames.AtoB(ItemType.controller));
        rootItem.getChildren().add(controllers_tree);
        for(AbstractController cntr : otm.scenario.controllers.values())
            controllers_tree.getChildren().add(new TreeItem<>(ItemPool.getName(cntr)));

        // sensors
        sensors_tree = new TreeItem<>(ItemPool.itemNames.AtoB(ItemType.sensor));
        rootItem.getChildren().add(sensors_tree);
        for (AbstractSensor sens : otm.scenario.sensors.values())
            sensors_tree.getChildren().add(new TreeItem<>(ItemPool.getName(sens)));

        scenariotree.setRoot(rootItem);

    }

    private void reset(){
        clearSelection();
    }

    public void add_node(Object o){
        System.out.println("HELLO!!!");
//        links_tree.getChildren().add(new TreeItem<>(Maps.name2linkid.getFromSecond(link.getId())));
    }


    public void add_link(common.Link link){
        links_tree.getChildren().add(new TreeItem<>(ItemPool.getName(link)));
    }

    public void remove_link(common.Link link){
        TreeItem item = new TreeItem<>(ItemPool.getName(link));
        links_tree.getChildren().remove(item);
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
        TreeItem item = (TreeItem) getTreeView().getSelectionModel().getSelectedItem();

        // fire event for first click
        if(clickcount==1)
            Event.fireEvent(mouseEvent.getTarget(),new TreeSelectEvent(TreeSelectEvent.CLICK1,item,mouseEvent));

        if(clickcount==2){
            if(item==null)
                return;
            TreeItem parent = item.getParent();
            if(parent!=null) {
                ItemType itemType = ItemPool.itemNames.BtoA((String) parent.getValue());
                if(itemType !=null)
                    switch (itemType) {
                        case link:
                            Event.fireEvent(mouseEvent.getTarget(),new TreeSelectEvent(TreeSelectEvent.CLICK2_LINK,item,mouseEvent));
                            break;
                        case commodity:
                            Event.fireEvent(mouseEvent.getTarget(),new TreeSelectEvent(TreeSelectEvent.CLICK2_COMMODITY,item,mouseEvent));
                            break;
                        case subnetwork:
                            Event.fireEvent(mouseEvent.getTarget(),new TreeSelectEvent(TreeSelectEvent.CLICK2_SUBNETWORK,item,mouseEvent));
                            break;
                        case split:
                            Event.fireEvent(mouseEvent.getTarget(),new TreeSelectEvent(TreeSelectEvent.CLICK2_SPLIT,item,mouseEvent));
                            break;
                        case demand:
                            Event.fireEvent(mouseEvent.getTarget(),new TreeSelectEvent(TreeSelectEvent.CLICK2_DEMAND,item,mouseEvent));
                            break;
                        case actuator:
                            Event.fireEvent(mouseEvent.getTarget(),new TreeSelectEvent(TreeSelectEvent.CLICK2_ACTUATOR,item,mouseEvent));
                            break;
                        case controller:
                            Event.fireEvent(mouseEvent.getTarget(),new TreeSelectEvent(TreeSelectEvent.CLICK2_CONTROLLER,item,mouseEvent));
                            break;
                        case sensor:
                            Event.fireEvent(mouseEvent.getTarget(),new TreeSelectEvent(TreeSelectEvent.CLICK2_SENSOR,item,mouseEvent));
                            break;

                        default:
                            System.err.println("????");
                    }
            }
        }

        mouseEvent.consume();

    }

    public void highlight(Map<String,Set<AbstractItem>> selection){

//        for(Map.Entry<Class,Set<AbstractDrawItem>> e : selection.entrySet()){
//            Class clazz = e.getKey();
//            Set<AbstractDrawItem> X = e.getValue();
//
//
//            for(AbstractDrawItem item : X){
//                TreeItem linkList = searchItem(scenariotree.getRoot(), "links");
//                TreeItem item = searchItem(linkList, Maps.name2linkid.getFromSecond(obj.id));
//                if (item != null)
//                    scenariotree.getSelectionModel().select(scenariotree.getRow(item));
//            }
//        }


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

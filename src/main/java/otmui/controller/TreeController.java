package otmui.controller;

import java.net.URL;
import java.util.*;

import actuator.AbstractActuator;
import api.OTMdev;
import javafx.scene.Scene;
import otmui.ElementType;
import otmui.MainApp;
import otmui.Maps;
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
        scene.addEventFilter(NewScenarioEvent.SCENARIO_LOADED_OTM, e->loadScenario(e.otmdev) );
        scene.addEventFilter(NewElementEvent.NEW_NODE, e->add_node(e.item));
        scene.addEventFilter(DeleteElementEvent.REMOVE_LINK, e->remove_link((common.Link)e.item));
    }

    /////////////////////////////////////////////////
    // event triggered
    /////////////////////////////////////////////////

    private void loadScenario(OTMdev otmdev){

        // populate the tree
        TreeItem<String> rootItem = new TreeItem<>("scenario");
        rootItem.setExpanded(false);

        // commodities
        comms_tree = new TreeItem<>(Maps.elementName.getFromFirst(ElementType.COMMODITY));
        rootItem.getChildren().add(comms_tree);
        for (commodity.Commodity comm : otmdev.scenario.commodities.values())
            comms_tree.getChildren().add(new TreeItem<>(Maps.name2commodityid.getFromSecond(comm.getId())));

        // links
        links_tree = new TreeItem<>(Maps.elementName.getFromFirst(ElementType.LINK));
        rootItem.getChildren().add(links_tree);
        List<common.Link> links = new ArrayList<>();
        links.addAll(otmdev.scenario.network.links.values());
        // TODO Collections.sort(links);
        for (common.Link l : links)
            links_tree.getChildren().add(new TreeItem<>(Maps.name2linkid.getFromSecond(l.getId())));

        // subnetwork
        subnets_tree = new TreeItem<>(Maps.elementName.getFromFirst(ElementType.SUBNETWORK));
        rootItem.getChildren().add(subnets_tree);
        for (Subnetwork s : otmdev.scenario.subnetworks.values())
            subnets_tree.getChildren().add(new TreeItem<>(Maps.name2subnetworkid.getFromSecond(s.getId())));

        // demands
        demands_tree = new TreeItem<>(Maps.elementName.getFromFirst(ElementType.DEMAND));
        rootItem.getChildren().add(demands_tree);
        for (AbstractDemandProfile profile : otmdev.scenario.data_demands.values())
            demands_tree.getChildren().add(new TreeItem<>(Maps.name2demandid.getFromSecond(profile.source.link.getId())));

        // TODO splitsForNode
//        spt = new TreeItem<>(Maps.elementName.getFromFirst(ElementType.SPLIT));
//        rootItem.getChildren().add(spt);
//        for (SplitsForNode s : scenario.getSplits())
//            spt.getChildren().add(new TreeItem<>(Maps.name2splitid.getFromSecond(s.getId())));

        // actuators
        actuators_tree = new TreeItem<>(Maps.elementName.getFromFirst(ElementType.ACTUATOR));
        rootItem.getChildren().add(actuators_tree);
        for (AbstractActuator a : otmdev.scenario.actuators.values())
            actuators_tree.getChildren().add(new TreeItem<>(Maps.name2actuatorid.getFromSecond(a.getId())));

        // controllers
        controllers_tree = new TreeItem<>(Maps.elementName.getFromFirst(ElementType.CONTROLLER));
        rootItem.getChildren().add(controllers_tree);
        for(AbstractController c : otmdev.scenario.controllers.values())
            controllers_tree.getChildren().add(new TreeItem<>(Maps.name2controllerid.getFromSecond(c.getId())));

        // sensors
        sensors_tree = new TreeItem<>(Maps.elementName.getFromFirst(ElementType.SENSOR));
        rootItem.getChildren().add(sensors_tree);
        for (AbstractSensor s : otmdev.scenario.sensors.values())
            sensors_tree.getChildren().add(new TreeItem<>(Maps.name2sensorid.getFromSecond(s.id)));

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
        links_tree.getChildren().add(new TreeItem<>(Maps.name2linkid.getFromSecond(link.getId())));
    }

    public void remove_link(common.Link link){
        TreeItem item = new TreeItem<>(Maps.name2linkid.getFromSecond(link.getId()));
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

        // fire event for first click
        if(clickcount==1)
            Event.fireEvent(mouseEvent.getTarget(),new TreeSelectEvent(TreeSelectEvent.CLICK1,mouseEvent));

        if(clickcount==2){
            TreeItem item = (TreeItem) myApp.treeController.getTreeView().getSelectionModel().getSelectedItem();
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

    public void highlight(Map<Class,Set<AbstractItem>> selection){

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

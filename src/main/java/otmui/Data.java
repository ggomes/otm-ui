package otmui;

import actuator.AbstractActuator;
import api.OTMdev;
import commodity.Path;
import error.OTMException;
import javafx.event.Event;
import javafx.scene.shape.Shape;
import keys.DemandType;
import keys.KeyCommodityDemandTypeId;
import otmui.event.DoAddItem;
import otmui.event.DoRemoveItem;
import otmui.item.*;
import otmui.view.FactoryComponent;
import profiles.AbstractDemandProfile;
import sensor.AbstractSensor;

import java.util.*;

import static java.util.stream.Collectors.toSet;

public class Data {

    MainApp myApp;

    // items contains, for each item type (link, node, etc), a map id->object
    public Map<ItemType, Map<Long,AbstractItem>> items;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public Data(MainApp myApp, GlobalParameters params) throws OTMException {
        this.myApp = myApp;
        items = new HashMap<>();

        OTMdev otm = myApp.otm;

        // links
        Map<Long, AbstractItem> links = new HashMap<>();
        items.put(ItemType.link,links);
        for(common.Link link : otm.scenario.network.links.values())
            links.put(link.getId(), FactoryItem.makeLink(link,params));

        // nodes
        Map<Long, AbstractItem> nodes = new HashMap<>();
        items.put(ItemType.node,nodes);
        for(common.Node node : otm.scenario.network.nodes.values())
            nodes.put(node.getId(), FactoryItem.makeNode(node,params));

        // actuators
        Map<Long, AbstractItem> actuators = new HashMap<>();
        items.put(ItemType.actuator,actuators);
        for (actuator.AbstractActuator actuator : otm.scenario.actuators.values())
            actuators.put(actuator.getId(), FactoryItem.makeActuator(otm,actuator,params));

        // sensors
        Map<Long, AbstractItem> sensors = new HashMap<>();
        items.put(ItemType.sensor,sensors);
        for (AbstractSensor sensor : otm.scenario.sensors.values())
            sensors.put(sensor.getId(), FactoryItem.makeSensor(sensor,params));

        // controllers
        Map<Long, AbstractItem> controllers = new HashMap<>();
        items.put(ItemType.controller,controllers);
        for (control.AbstractController ctrl : otm.scenario.controllers.values())
            controllers.put(ctrl.getId(),new otmui.item.Controller(ctrl,this));

        // commodities
        Map<Long, AbstractItem> commodities = new HashMap<>();
        items.put(ItemType.commodity,commodities);
        for (commodity.Commodity comm : otm.scenario.commodities.values())
            commodities.put(comm.getId(),new otmui.item.Commodity(comm));

        // subnetworks
        Map<Long, AbstractItem> subnetworks = new HashMap<>();
        items.put(ItemType.subnetwork,subnetworks);
        for (commodity.Subnetwork subnet : otm.scenario.subnetworks.values())
            subnetworks.put(subnet.getId(),new otmui.item.Subnetwork(subnet,this));

        // demands
        for (Map.Entry<KeyCommodityDemandTypeId, AbstractDemandProfile> e : otm.scenario.data_demands.entrySet()){

            long link_or_subnetwork_id = e.getKey().link_or_subnetwork_id;
            DemandType demandType = e.getKey().demandType;

            Long link_id;
            if(demandType.equals(DemandType.pathfull)){
                commodity.Subnetwork subnetwork = otm.scenario.subnetworks.get(link_or_subnetwork_id);
                link_id = subnetwork.isPath() ? ((Path)subnetwork).ordered_links.get(0).getId() : null;
            } else
                link_id = link_or_subnetwork_id;

            if(link_id==null)
                continue;

            ((Link)items.get(ItemType.link).get(link_id)).add_demand(e.getKey().commodity_id,e.getValue());
        }

        // splits
        for (common.Node cnode : otm.scenario.network.nodes.values())
            if(cnode.splits!=null)
                ((otmui.item.Node) items.get(ItemType.node).get(cnode.getId())).splits = cnode.splits;

    }

    /////////////////////////////////////////////////
    // get
    /////////////////////////////////////////////////

    public List<Shape> getShapes(){
        List<Shape> allshapes = new ArrayList<>();

        allshapes.addAll(items.get(ItemType.link).values().stream()
                .flatMap(x->((AbstractGraphItem)x).shapegroup.stream())
                .collect(toSet()) );

        allshapes.addAll(items.get(ItemType.node).values().stream()
                .flatMap(x->((AbstractGraphItem)x).shapegroup.stream())
                .collect(toSet()) );

        allshapes.addAll(items.get(ItemType.actuator).values().stream()
                .flatMap(x->((AbstractGraphItem)x).shapegroup.stream())
                .collect(toSet()) );

        allshapes.addAll(items.get(ItemType.sensor).values().stream()
                .flatMap(x->((AbstractGraphItem)x).shapegroup.stream())
                .collect(toSet()) );

        return allshapes;
    }

    public TypeId getTypeId(String itemName) {
        return new TypeId(itemName);
    }

    public AbstractItem getItem(TypeId typeId) {
        return items.get(typeId.type).get(typeId.id);
    }

    /////////////////////////////////
    // create and delete items
    /////////////////////////////////

    public otmui.item.Node insert_node(float xcoord, float ycoord, otmui.item.Actuator actuator){

        common.Scenario scenario = myApp.otm.scenario;
        long id = scenario.network.nodes.keySet().stream().max(Comparator.naturalOrder()).get() + 1;
        common.Node cnode = new common.Node(scenario.network,id,xcoord,ycoord,false);

        // add to OTM scenario
        scenario.network.nodes.put(id,cnode);

        // create ui item
        otmui.item.Node node = FactoryItem.makeNode(cnode,myApp.params);
        myApp.data.items.get(ItemType.node).put(node.id,node);

        // attach the actuator
        if(actuator!=null){
            node.node.actuator = actuator.actuator;
            actuator.actuator.target = node.node;
        }

        // fire event
        Event.fireEvent(myApp.stage.getScene(),new DoAddItem(DoAddItem.ADD_ITEM, node));

        return node;
    }

    public boolean delete_item(AbstractItem item) {
        switch(item.getType()){
            case node:
                otmui.item.Node node = (otmui.item.Node) item;

                if(!node.node.in_links.isEmpty() || !node.node.out_links.isEmpty()) {
                    FactoryComponent.warning_dialog("Non-isolated nodes cannot be deleted.");
                    return false;
                }

                if(node.node.actuator!=null)
                    delete_item(items.get(ItemType.actuator).get(node.node.actuator.id));

                // remove from scenario
                myApp.otm.scenario.network.nodes.remove(node.id);

                // remove from items
                items.get(ItemType.node).remove(node.id);

                break;

            case link:
                otmui.item.Link link = (otmui.item.Link) item;

//                if(link.link.actuator_fd!=null)
//                    delete_item(items.get(ItemType.actuator).get(link.link.actuator_fd.id));
//                if(link.link.ramp_meter!=null)
//                    delete_item(items.get(ItemType.actuator).get(link.link.ramp_meter.id));

                // remove from scenario
                link.link.start_node.out_links.remove(link.id);
                link.link.end_node.in_links.remove(link.id);
                myApp.otm.scenario.network.links.remove(link.id);

                // remove from items
                items.get(ItemType.link).remove(link.id);

                break;

            case sensor:
                otmui.item.FixedSensor sensor = (otmui.item.FixedSensor) item;

                // remove from scenario
                myApp.otm.scenario.sensors.remove(sensor.id);

                // remove from items
                items.get(ItemType.sensor).remove(sensor.id);

                break;

            case actuator:
                otmui.item.Actuator actuator = (otmui.item.Actuator) item;
                AbstractActuator act = actuator.actuator;

                if(act.target instanceof common.Node)
                    ((common.Node)act.target).actuator = null;

//                if(act.target instanceof common.Link) {
//                    if(((common.Link) act.target).ramp_meter==act)
//                        ((common.Link) act.target).ramp_meter=null;
//                    if(((common.Link) act.target).actuator_fd==act)
//                        ((common.Link) act.target).actuator_fd=null;
//                }

                // remove from scenario
                myApp.otm.scenario.actuators.remove(actuator.id);

                // remove from items
                items.get(ItemType.actuator).remove(actuator.id);

                break;
        }

        Event.fireEvent(myApp.stage.getScene(),new DoRemoveItem(DoRemoveItem.REMOVE_ITEM, item));

        return true;
    }

    /////////////////////////////////
    // bounding box
    /////////////////////////////////

    public Double getMinX(){
        return items.get(ItemType.node).values().stream()
                .mapToDouble(n->((AbstractGraphItem)n).xpos)
                .min()
                .getAsDouble();
    }

    public Double getMinY(){
        return items.get(ItemType.node).values().stream()
                .mapToDouble(n->((AbstractGraphItem)n).ypos)
                .min()
                .getAsDouble();
    }

    public Double getMaxX(){
        return items.get(ItemType.node).values().stream()
                .mapToDouble(n->((AbstractGraphItem)n).xpos)
                .max()
                .getAsDouble();
    }

    public Double getMaxY(){
        return items.get(ItemType.node).values().stream()
                .mapToDouble(n->((AbstractGraphItem)n).ypos)
                .max()
                .getAsDouble();
    }

    public Double getWidth(){
        return getMaxX()-getMinX();
    }

    public Double getHeight(){
        return getMaxY()-getMinY();
    }


}

package otmui;

import api.OTMdev;
import commodity.Path;
import error.OTMException;
import javafx.scene.shape.Shape;
import keys.DemandType;
import keys.KeyCommodityDemandTypeId;
import otmui.item.*;
import profiles.AbstractDemandProfile;
import sensor.AbstractSensor;

import java.util.*;

import static java.util.stream.Collectors.toSet;

public class Data {

    OTMdev otm;

    // items contains, for each item type (link, node, etc), a map id->object
    public Map<ItemType, Map<Long,AbstractItem>> items;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public Data(OTMdev otm, GlobalParameters params) throws OTMException {
        this.otm = otm;
        items = new HashMap<>();

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
                link_id = subnetwork.is_path ? ((Path)subnetwork).ordered_links.get(0).getId() : null;
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

    public Set<Shape> getShapes(){
        Set<Shape> allshapes = new HashSet<>();

        allshapes.addAll(items.get(ItemType.node).values().stream()
                .flatMap(x->((AbstractGraphItem)x).shapegroup.stream())
                .collect(toSet()) );


        allshapes.addAll(items.get(ItemType.link).values().stream()
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

        // TODO MAKE THIS WORK FOR DEMANDS AND SPLITS
    }

    public AbstractItem getItem(TypeId typeId) {
        return items.get(typeId.type).get(typeId.id);
    }

    /////////////////////////////////
    // delete
    /////////////////////////////////

    public void delete_item(AbstractItem item){
        switch(item.getType()){
            case node:
                otmui.item.Node node = (otmui.item.Node) item;

                if(node.node.actuator!=null)
                    delete_item(items.get(ItemType.actuator).get(node.node.actuator.id));

                // remove from scenario
                for(common.Link link : node.node.out_links.values()){
                    link.start_node = null;
                    link.is_source = true;
                }
                for(common.Link link : node.node.in_links.values()){
                    link.end_node = null;
                    link.is_sink = true;
                }
                otm.scenario.network.nodes.remove(node.id);

                // remove from items
                items.get(ItemType.node).remove(node.id);

                break;

            case link:
                otmui.item.Link link = (otmui.item.Link) item;

                if(link.link.actuator_fd!=null)
                    delete_item(items.get(ItemType.actuator).get(link.link.actuator_fd.id));
                if(link.link.ramp_meter!=null)
                    delete_item(items.get(ItemType.actuator).get(link.link.ramp_meter.id));

                // remove from scenario
                link.link.start_node.out_links.remove(link.id);
                link.link.end_node.in_links.remove(link.id);
                otm.scenario.network.links.remove(link.id);

                // remove from items
                items.get(ItemType.link).remove(link.id);

                break;

            case sensor:
                otmui.item.FixedSensor sensor = (otmui.item.FixedSensor) item;

                // remove from scenario
                otm.scenario.sensors.remove(sensor.id);

                // remove from items
                items.get(ItemType.sensor).remove(sensor.id);

                break;

            case actuator:
                otmui.item.Actuator actuator = (otmui.item.Actuator) item;

                // remove from scenario
                otm.scenario.actuators.remove(actuator.id);

                // remove from items
                items.get(ItemType.actuator).remove(actuator.id);

                break;
        }

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

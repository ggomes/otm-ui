package otmui;

import api.OTMdev;
import commodity.Path;
import error.OTMException;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import keys.DemandType;
import keys.KeyCommodityDemandTypeId;
import otmui.item.*;
import otmui.utils.BijectiveMap;
import profiles.AbstractDemandProfile;
import sensor.AbstractSensor;

import java.util.*;

import static java.util.stream.Collectors.toSet;

public class Data {

//    public static BijectiveMap<ItemType, String> itemNames = new BijectiveMap();
//
//    static {
//        itemNames.put(ItemType.node, "node");
//        itemNames.put(ItemType.link, "link");
//        itemNames.put(ItemType.commodity, "type");
//        itemNames.put(ItemType.subnetwork, "subnetwork");
//        itemNames.put(ItemType.demand, "demand");
//        itemNames.put(ItemType.split, "split");
//        itemNames.put(ItemType.actuator, "actuator");
//        itemNames.put(ItemType.controller, "controller");
//        itemNames.put(ItemType.sensor, "sensor");
//    }

    // items contains, for each item type (link, node, etc), a map id->object
    public Map<ItemType, Map<Long,AbstractItem>> items;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public Data(OTMdev otm, GlobalParameters params) throws OTMException {

        float node_size = params.node_size.floatValue();
        float lane_width_meters = params.lane_width_meters.getValue();
        float link_offset = params.link_offset.getValue();
        GlobalParameters.RoadColorScheme road_color_scheme = (GlobalParameters.RoadColorScheme) params.road_color_scheme.getValue();

        items = new HashMap<>();

        // links
        Map<Long, AbstractItem> links = new HashMap<>();
        items.put(ItemType.link,links);
        for(common.Link link : otm.scenario.network.links.values())
            links.put(link.getId(), makeDrawLink(link, lane_width_meters, link_offset, road_color_scheme));

        // nodes
        Map<Long, AbstractItem> nodes = new HashMap<>();
        items.put(ItemType.node,nodes);
        for(common.Node node : otm.scenario.network.nodes.values())
            nodes.put(node.getId(), makeDrawNode(node,node_size));

        // actuators
        Map<Long, AbstractItem> actuators = new HashMap<>();
        items.put(ItemType.actuator,actuators);
        for (actuator.AbstractActuator actuator : otm.scenario.actuators.values())
            actuators.put(actuator.getId(),makeDrawActuator(otm,actuator,((float)node_size)*0.7f));

        // sensors
        Map<Long, AbstractItem> sensors = new HashMap<>();
        items.put(ItemType.sensor,sensors);
        for (AbstractSensor sensor : otm.scenario.sensors.values())
            sensors.put(sensor.getId(),makeDrawSensor(sensor, lane_width_meters, link_offset));

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

    /////////////////////////////////////////////////
    // makers
    /////////////////////////////////////////////////

    public static Node makeDrawNode(common.Node node, float radius) {
        return node==null ?
                new Node(null,0f,0f,radius, Color.BLACK, 0f) :
                new Node(node,node.xcoord,-node.ycoord,radius, Color.DODGERBLUE, 0f);
    }

    public static Link makeDrawLink(common.Link link, float lane_width, float link_offset, GlobalParameters.RoadColorScheme road_color_scheme) throws OTMException {

        Link drawLink;
        switch(link.model.getClass().getSimpleName()){

            case "BaseModel":
            case "ModelSpatialQ":
            case "ModelNewell":
                drawLink = new LinkSpaceQ(link,
                        lane_width,
                        link_offset,
                        road_color_scheme);
                break;

            case "ModelCTM":
                drawLink = new LinkCTM(link,
                        lane_width,
                        link_offset,
                        road_color_scheme );
                break;

            default:
                throw new OTMException("Link model type not supported.");
        }

        return drawLink;
    }

    public static Actuator makeDrawActuator(OTMdev otmdev, actuator.AbstractActuator actuator, float size) {

        if (actuator==null)
            return new StopSign(null,0f,0f,size, 0f);

        common.Node node = otmdev.scenario.network.nodes.get(actuator.target.getId());

        switch (actuator.getType()) {
            case signal:
                return new StopSign(actuator, node.xcoord, -node.ycoord, size, 4f);
            case stop:
                return new StopSign(actuator, node.xcoord, -node.ycoord, size, 0f );
            default:
                return new StopSign(actuator, node.xcoord, -node.ycoord, size, 1f);
        }
    }

    public static FixedSensor makeDrawSensor(AbstractSensor sensor, float lane_width, float link_offset) throws OTMException {
//        return sensor==null ? new BaseSensor() : new BaseSensor(sensor, lane_width, link_offset);
        return new FixedSensor(sensor, lane_width, link_offset);
    }

    ///////////////
    // get Name
    ///////////////

    public static String getName(AbstractItem item) {
        return String.format("%s %d", item.getName(), item.id);
    }

    public TypeId getTypeId(String itemName) {
        return new TypeId(itemName);

        // TODO MAKE THIS WORK FOR DEMANDS AND SPLITS
    }

    public AbstractItem getItem(TypeId typeId) {
        return items.get(typeId.type).get(typeId.id);
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

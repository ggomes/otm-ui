package otmui;

import actuator.AbstractActuator;
import api.OTMdev;
import commodity.Subnetwork;
import control.AbstractController;
import error.OTMException;
import javafx.scene.paint.Color;
import otmui.item.*;
import otmui.utils.BijectiveMap;
import profiles.AbstractDemandProfile;
import sensor.AbstractSensor;

import java.util.HashMap;
import java.util.Map;

public class ItemPool {

    public static BijectiveMap<ItemType, String> itemNames = new BijectiveMap();

    static {
        itemNames.put(ItemType.link, "links");
        itemNames.put(ItemType.commodity, "vehicle types");
        itemNames.put(ItemType.subnetwork, "subnetworks");
        itemNames.put(ItemType.demand, "demands");
        itemNames.put(ItemType.split, "splits");
        itemNames.put(ItemType.actuator, "actuators");
        itemNames.put(ItemType.controller, "controllers");
        itemNames.put(ItemType.sensor, "sensors");
    }

    // items contains, for each item type (link, node, etc), a map id->object
    public Map<ItemType, Map<Long, AbstractItem>> items;

    ///////////////
    // construction
    ///////////////

    public ItemPool(OTMdev otm, GlobalParameters params) throws OTMException {

        float node_size = params.node_size.floatValue();
        float lane_width_meters = params.lane_width_meters.getValue();
        float link_offset = params.link_offset.getValue();
        GlobalParameters.RoadColorScheme road_color_scheme = (GlobalParameters.RoadColorScheme) params.road_color_scheme.getValue();

        items = new HashMap<>();

        // create nodes
        Map<Long, AbstractItem> nodes = new HashMap<>();
        items.put(ItemType.node,nodes);
        for(common.Node node : otm.scenario.network.nodes.values())
            nodes.put(node.getId(), makeDrawNode(node,node_size));

        // create links
        Map<Long, AbstractItem> links = new HashMap<>();
        items.put(ItemType.link,links);
        for(common.Link link : otm.scenario.network.links.values())
            links.put(link.getId(), makeDrawLink(link, lane_width_meters, link_offset, road_color_scheme));

        // create actuators
        Map<Long, AbstractItem> actuators = new HashMap<>();
        items.put(ItemType.actuator,actuators);
        for (actuator.AbstractActuator actuator : otm.scenario.actuators.values())
            actuators.put(actuator.getId(),makeDrawActuator(otm,actuator,((float)node_size)*0.7f));

        // create sensors
        Map<Long, AbstractItem> sensors = new HashMap<>();
        items.put(ItemType.sensor,sensors);
        for (AbstractSensor sensor : otm.scenario.sensors.values())
            sensors.put(sensor.getId(),makeDrawSensor(sensor, lane_width_meters, link_offset));
    }

    /////////////////////////////////////////////////
    // makers
    /////////////////////////////////////////////////

    public static BaseNode makeDrawNode(common.Node node, float radius) {
        return node==null ?
                new BaseNode(-1L,0f,0f,radius, Color.BLACK, 0f) :
                new BaseNode(node.getId(),node.xcoord,-node.ycoord,radius, Color.DODGERBLUE, 0f);
    }

    public static BaseLink makeDrawLink(common.Link link, float lane_width, float link_offset, GlobalParameters.RoadColorScheme road_color_scheme) throws OTMException {

        BaseLink drawLink;
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

    public static BaseActuator makeDrawActuator(OTMdev otmdev, actuator.AbstractActuator actuator, float size) {

        if (actuator==null)
            return new StopSign(-1L,0f,0f,size, 0f);

        common.Node node = otmdev.scenario.network.nodes.get(actuator.target.getId());

        switch (actuator.getType()) {
            case signal:
                return new StopSign(actuator.getId(), node.xcoord, -node.ycoord, size, 4f);
            case stop:
                return new StopSign(actuator.getId(), node.xcoord, -node.ycoord, size, 0f );
            default:
                return new StopSign(actuator.getId(), node.xcoord, -node.ycoord, size, 1f);
        }
    }

    public static BaseSensor makeDrawSensor(AbstractSensor sensor, float lane_width, float link_offset) throws OTMException {
        return sensor==null ? new BaseSensor() : new BaseSensor(sensor, lane_width, link_offset);
    }

//    public void clear() {
//        drawnodes = new HashMap<>(); // <id,item>
//        drawlinks = new HashMap<>(); // <id,item>
//        drawactuators = new HashMap<>(); // <id,item>
//        drawsensors = new HashMap<>(); // <id,item>
//    }

    ///////////////
    // get Name
    ///////////////

    public static String getName(AbstractItem item) {
        return String.format("%s %d", item.getName(), item.id);
    }

    public static String getName(commodity.Commodity item) {
        return String.format("%s %d", itemNames.AtoB(ItemType.commodity), item.getId());
    }

    public static String getName(common.Link item) {
        return String.format("%s %d", itemNames.AtoB(ItemType.link), item.getId());
    }

    public static String getName(Subnetwork item) {
        return String.format("%s %d", itemNames.AtoB(ItemType.subnetwork), item.getId());
    }

    public static String getName(AbstractDemandProfile item) {
        return String.format("%s %d", itemNames.AtoB(ItemType.demand), item.source.link.getId());
    }

    public static String getName(AbstractActuator item) {
        return String.format("%s %d", itemNames.AtoB(ItemType.actuator), item.getId());
    }

    public static String getName(AbstractController item) {
        return String.format("%s %d", itemNames.AtoB(ItemType.controller), item.getId());
    }

    public static String getName(AbstractSensor item) {
        return String.format("%s %d", itemNames.AtoB(ItemType.sensor), item.getId());
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
                .mapToDouble(n->((AbstractPointItem)n).xpos)
                .min()
                .getAsDouble();
    }

    public Double getMinY(){
        return items.get(ItemType.node).values().stream()
                .mapToDouble(n->((AbstractPointItem)n).ypos)
                .min()
                .getAsDouble();
    }

    public Double getMaxX(){
        return items.get(ItemType.node).values().stream()
                .mapToDouble(n->((AbstractPointItem)n).xpos)
                .max()
                .getAsDouble();
    }

    public Double getMaxY(){
        return items.get(ItemType.node).values().stream()
                .mapToDouble(n->((AbstractPointItem)n).ypos)
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

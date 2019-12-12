package otmui.graph;

import api.OTMdev;
import javafx.scene.paint.Color;
import otmui.GlobalParameters;
import error.OTMException;
import otmui.item.*;
import sensor.AbstractSensor;

import java.util.*;

public class Graph {

    public float node_size;
    public boolean view_nodes;
    public boolean view_actuators;
    public float lane_width_meters;
    public float link_offset;
    public GlobalParameters.RoadColorScheme road_color_scheme;

    public Map<Long, AbstractNode> drawnodes; // <id,item>
    public Map<Long, AbstractLink> drawlinks; // <id,item>
    public Map<Long,DrawSensor> drawsensors; // <id,item>
    public Map<Long, AbstractActuator> drawactuators; // <id,item>

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public Graph(OTMdev otmdev, GlobalParameters params) throws OTMException {

        clear();

        this.node_size = params.node_size.floatValue();
        this.view_nodes = params.view_nodes.getValue();
        this.view_actuators = params.view_actuators.getValue();
        this.lane_width_meters = params.lane_width_meters.getValue();;
        this.link_offset = params.link_offset.getValue();;
        this.road_color_scheme = (GlobalParameters.RoadColorScheme) params.road_color_scheme.getValue();;

        // create nodes and links
        otmdev.scenario.network.nodes.values().forEach(x->add_node(x));

        for(common.Link link : otmdev.scenario.network.links.values())
            add_link(link);

        // create actuators
        for (actuator.AbstractActuator actuator : otmdev.scenario.actuators.values()) {
            AbstractActuator drawActuator = makeDrawActuator(otmdev,actuator,((float)node_size)*0.7f);
            drawactuators.put(drawActuator.id,drawActuator);
        }

        // create sensors
        for (AbstractSensor sensor : otmdev.scenario.sensors.values()) {
            DrawSensor drawSensor = makeDrawSensor(sensor, lane_width_meters, link_offset);
            drawsensors.put(drawSensor.id,drawSensor);
        }

    }

    /////////////////////////////////////////////////
    // getters
    /////////////////////////////////////////////////

    public Collection<AbstractNode> getNodes(){
        return drawnodes.values();
    }

    public Collection<AbstractActuator> getActuators(){
        return drawactuators.values();
    }

    public Collection<AbstractLink> getLinks(){
        return drawlinks.values();
    }

    public Collection<DrawSensor> getSensors(){
        return drawsensors.values();
    }

    public Double getMinX(){
        return drawnodes.values().stream().mapToDouble(AbstractNode::getXPos).min().getAsDouble();
    }

    public Double getMinY(){
        return drawnodes.values().stream().mapToDouble(AbstractNode::getYPos).min().getAsDouble();
    }

    public Double getMaxX(){
        return drawnodes.values().stream().mapToDouble(AbstractNode::getXPos).max().getAsDouble();
    }

    public Double getMaxY(){
        return drawnodes.values().stream().mapToDouble(AbstractNode::getYPos).max().getAsDouble();
    }

    public Double getWidth(){
        return getMaxX()-getMinX();
    }

    public Double getHeight(){
        return getMaxY()-getMinY();
    }

    /////////////////////////////////////////////////
    // adders
    /////////////////////////////////////////////////

    public void add_node(common.Node node){
        drawnodes.put(node.getId(), makeDrawNode(node));
    }

    public void add_link(common.Link link) throws OTMException {
        drawlinks.put(link.getId(), makeDrawLink(link, lane_width_meters, link_offset, road_color_scheme, drawnodes));
    }

    /////////////////////////////////////////////////
    // makers
    /////////////////////////////////////////////////

    public AbstractNode makeDrawNode(common.Node node) {
        return makeDrawNode(node, node_size);
    }

    public static AbstractNode makeDrawNode(common.Node node, float radius) {
        return node==null ?
                new NodeCircle(-1L,0f,0f,radius, Color.BLACK, 0f) :
                new NodeCircle(node.getId(),node.xcoord,-node.ycoord,radius, Color.DODGERBLUE, 0f);
    }

    public static AbstractLink makeDrawLink(common.Link link, float lane_width, float link_offset, GlobalParameters.RoadColorScheme road_color_scheme, Map<Long, AbstractNode> nodes) throws OTMException {

        AbstractLink drawLink;
        switch(link.model.getClass().getSimpleName()){

            case "BaseModel":
            case "ModelSpatialQ":
            case "ModelNewell":
                drawLink = new LinkSpaceQ(link,
                        nodes.get(link.start_node.getId()),
                        nodes.get(link.end_node.getId()),
                        lane_width,
                        link_offset,
                        road_color_scheme);
                break;

            case "ModelCTM":
                drawLink = new LinkCTM(link,
                        nodes.get(link.start_node.getId()),
                        nodes.get(link.end_node.getId()),
                        lane_width,
                        link_offset,
                        road_color_scheme );
                break;

            default:
                throw new OTMException("Link model type not supported.");
        }

        return drawLink;
    }

    public static AbstractActuator makeDrawActuator(OTMdev otmdev, actuator.AbstractActuator actuator, float size) {

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

    public static DrawSensor makeDrawSensor(AbstractSensor sensor, float lane_width, float link_offset) throws OTMException {
        return sensor==null ? new DrawSensor() : new DrawSensor(sensor, lane_width, link_offset);
    }

    public void clear() {
        drawnodes = new HashMap<>(); // <id,item>
        drawlinks = new HashMap<>(); // <id,item>
        drawactuators = new HashMap<>(); // <id,item>
        drawsensors = new HashMap<>(); // <id,item>
    }

}
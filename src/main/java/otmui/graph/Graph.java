package otmui.graph;

import actuator.AbstractActuator;
import api.OTMdev;
import javafx.scene.paint.Color;
import otmui.GlobalParameters;
import otmui.graph.item.*;
import error.OTMException;
import sensor.AbstractSensor;

import java.util.*;

public class Graph {

    public float node_size;
    public boolean view_nodes;
    public boolean view_actuators;
    public float lane_width_meters;
    public float link_offset;
    public GlobalParameters.RoadColorScheme road_color_scheme;

    public Map<Long,AbstractDrawNode> drawnodes; // <id,item>
    public Map<Long,AbstractDrawLink> drawlinks; // <id,item>
    public Map<Long,DrawSensor> drawsensors; // <id,item>
    public Map<Long,AbstractDrawNode> drawactuators; // <id,item>

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
        for (AbstractActuator actuator : otmdev.scenario.actuators.values()) {
            AbstractDrawNode drawActuator = makeDrawActuator(otmdev,actuator,((float)node_size)*0.7f);
//            actuator.drawActuator = drawActuator;
            drawactuators.put(drawActuator.id,drawActuator);
        }

        // create sensors
        for (AbstractSensor sensor : otmdev.scenario.sensors.values()) {
            DrawSensor drawSensor = makeDrawSensor(sensor, lane_width_meters, link_offset);
//            sensor.drawSensor = drawSensor;
            drawsensors.put(drawSensor.id,drawSensor);
        }

    }

//    public Graph(Scenario scenario, GlobalParameters params) throws OTMException {
//        clear();
//
//        this.node_size = params.node_size.floatValue();
//        this.view_nodes = params.view_nodes.getValue();
//        this.view_actuators = params.view_actuators.getValue();
//        this.lane_width_meters = params.lane_width_meters.getValue();;
//        this.link_offset = params.link_offset.getValue();;
//        this.road_color_scheme = (GlobalParameters.RoadColorScheme) params.road_color_scheme.getValue();;
//
//        // create nodes and links
//        scenario.getNodes().forEach(x->add_node(x));
//
//        for(Link link : scenario.getLinks())
//            add_link(link);
//
//        // create actuators
//        for (otmui.model.Actuator actuator : scenario.getActuators()) {
//            AbstractDrawNode drawActuator = makeDrawActuator(otmdev,actuator,((float)node_size)*0.7f);
//            actuator.drawActuator = drawActuator;
//            actuators.put(drawActuator.id,drawActuator);
//        }
//
//        // create sensors
//        for (otmui.model.Sensor sensor : scenario.getSensors()) {
//            DrawSensor drawSensor = makeDrawSensor(sensor, lane_width_meters, link_offset);
//            sensor.drawSensor = drawSensor;
//            sensors.put(drawSensor.id,drawSensor);
//        }
//
//    }

    /////////////////////////////////////////////////
    // getters
    /////////////////////////////////////////////////

    public Collection<AbstractDrawNode> getNodes(){
        return drawnodes.values();
    }

    public Collection<AbstractDrawNode> getActuators(){
        return drawactuators.values();
    }

    public Collection<AbstractDrawLink> getLinks(){
        return drawlinks.values();
    }

    public Collection<DrawSensor> getSensors(){
        return drawsensors.values();
    }

    public Double getMinX(){
        return drawnodes.values().stream().mapToDouble(AbstractDrawNode::getXPos).min().getAsDouble();
    }

    public Double getMinY(){
        return drawnodes.values().stream().mapToDouble(AbstractDrawNode::getYPos).min().getAsDouble();
    }

    public Double getMaxX(){
        return drawnodes.values().stream().mapToDouble(AbstractDrawNode::getXPos).max().getAsDouble();
    }

    public Double getMaxY(){
        return drawnodes.values().stream().mapToDouble(AbstractDrawNode::getYPos).max().getAsDouble();
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
        AbstractDrawNode drawNode = makeDrawNode(node);
//        node.drawNode = drawNode;
        drawnodes.put( drawNode.id, drawNode);
    }

    public void add_link(common.Link link) throws OTMException {
        AbstractDrawLink drawLink = makeDrawLink(link, lane_width_meters, link_offset, road_color_scheme, drawnodes);
//        link.drawLink = drawLink;
        drawlinks.put(drawLink.id, drawLink);
    }

    /////////////////////////////////////////////////
    // makers
    /////////////////////////////////////////////////

    public AbstractDrawNode makeDrawNode(common.Node node) {
        return makeDrawNode(node, node_size);
    }

    public static AbstractDrawNode makeDrawNode(common.Node node, float radius) {
        return node==null ?
                new DrawNodeCircle(-1L,0f,0f,radius, Color.BLACK, 0f) :
                new DrawNodeCircle(node.getId(),node.xcoord,-node.ycoord,radius, Color.DODGERBLUE, 0f);
    }

    public static AbstractDrawLink makeDrawLink(common.Link link, float lane_width, float link_offset, GlobalParameters.RoadColorScheme road_color_scheme, Map<Long,AbstractDrawNode> nodes) throws OTMException {

        AbstractDrawLink drawLink;
        switch(link.model.getClass().getSimpleName()){

            case "BaseModel":
            case "ModelSpatialQ":
            case "ModelNewell":
                drawLink = new DrawLinkSpaceQ(link,
                        nodes.get(link.start_node.getId()),
                        nodes.get(link.end_node.getId()),
                        lane_width,
                        link_offset,
                        road_color_scheme);
                break;

            case "ModelCTM":
                drawLink = new DrawLinkCTM(link,
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

    public static AbstractDrawNode makeDrawActuator(OTMdev otmdev,AbstractActuator actuator, float size) {

        if (actuator==null)
            return new DrawStopSign(-1L,0f,0f,size, 0f);

        common.Node node = otmdev.scenario.network.nodes.get(actuator.target.getId());

        switch (actuator.getType()) {
            case signal:
                return new DrawStopSign(actuator.getId(), node.xcoord, -node.ycoord, size, 4f);
            case stop:
                return new DrawStopSign(actuator.getId(), node.xcoord, -node.ycoord, size, 0f );
            default:
                return new DrawStopSign(actuator.getId(), node.xcoord, -node.ycoord, size, 1f);
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
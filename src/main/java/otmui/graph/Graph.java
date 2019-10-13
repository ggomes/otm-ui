package otmui.graph;

import javafx.scene.paint.Color;
import otmui.GlobalParameters;
import otmui.graph.color.AbstractColormap;
import otmui.graph.item.*;
import otmui.model.Link;
import otmui.model.Node;
import otmui.model.Scenario;
import error.OTMException;

import java.util.*;

public class Graph {

    public float node_size;
    public boolean view_nodes;
    public boolean view_actuators;
    public float lane_width_meters;
    public float link_offset;
    public GlobalParameters.ColorScheme color_map;

    public Map<Long,AbstractDrawNode> nodes; // <id,item>
    public Map<Long,AbstractDrawLink> links; // <id,item>
    public Map<Long,DrawSensor> sensors; // <id,item>
    public Map<Long,AbstractDrawNode> actuators; // <id,item>

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public Graph(Scenario scenario, GlobalParameters params) throws OTMException {
        clear();

        this.node_size = params.node_size.floatValue();
        this.view_nodes = params.view_nodes.getValue();
        this.view_actuators = params.view_actuators.getValue();
        this.lane_width_meters = params.lane_width_meters.getValue();;
        this.link_offset = params.link_offset.getValue();;
        this.color_map = (GlobalParameters.ColorScheme) params.color_map.getValue();;

        // create the colormap
        AbstractColormap colormap = params.get_colormap();

        // create nodes
        for (Node node : scenario.getNodes()) {
            AbstractDrawNode drawNode = makeDrawNode(node,node_size);
            node.drawNode = drawNode;
            nodes.put( drawNode.id, drawNode);
        }

        // create links
        float lane_width_meters = params.lane_width_meters.floatValue();
        float link_offset = params.link_offset.floatValue();
        for (Link link : scenario.getLinks()) {
            AbstractDrawLink drawLink = makeDrawLink(link, lane_width_meters, link_offset, colormap, nodes);
            link.drawLink = drawLink;
            links.put(drawLink.id, drawLink);
        }

        // create actuators
        for (otmui.model.Actuator actuator : scenario.getActuators()) {
            AbstractDrawNode drawActuator = makeDrawActuator(actuator,node_size);
            actuator.drawActuator = drawActuator;
            actuators.put(drawActuator.id,drawActuator);
        }

        // create sensors
        for (otmui.model.Sensor sensor : scenario.getSensors()) {
            DrawSensor drawSensor = makeDrawSensor(sensor, lane_width_meters, link_offset);
            sensor.drawSensor = drawSensor;
            sensors.put(drawSensor.id,drawSensor);
        }

    }

    /////////////////////////////////////////////////
    // getters
    /////////////////////////////////////////////////

    public Collection<AbstractDrawNode> getNodes(){
        return nodes.values();
    }

    public Collection<AbstractDrawNode> getActuators(){
        return actuators.values();
    }

    public Collection<AbstractDrawLink> getLinks(){
        return links.values();
    }

    public Collection<DrawSensor> getSensors(){
        return sensors.values();
    }

    public Double getMinX(){
        return nodes.values().stream().mapToDouble(AbstractDrawNode::getXPos).min().getAsDouble();
    }

    public Double getMinY(){
        return nodes.values().stream().mapToDouble(AbstractDrawNode::getYPos).min().getAsDouble();
    }

    public Double getMaxX(){
        return nodes.values().stream().mapToDouble(AbstractDrawNode::getXPos).max().getAsDouble();
    }

    public Double getMaxY(){
        return nodes.values().stream().mapToDouble(AbstractDrawNode::getYPos).max().getAsDouble();
    }

    public Double getWidth(){
        return getMaxX()-getMinX();
    }

    public Double getHeight(){
        return getMaxY()-getMinY();
    }

    /////////////////////////////////////////////////
    // private
    /////////////////////////////////////////////////

    private static AbstractDrawNode makeDrawNode(otmui.model.Node node, float radius) {
        return node==null ?
                new DrawNodeCircle(-1L,0f,0f,radius, Color.BLACK, 0f) :
                new DrawNodeCircle(node.getId(),node.getXcoord(),-node.getYcoord(),radius, Color.DODGERBLUE, 0f);
    }

    private static AbstractDrawLink makeDrawLink(otmui.model.Link link, float lane_width, float link_offset, AbstractColormap colormap,Map<Long,AbstractDrawNode> nodes) throws OTMException {

        AbstractDrawLink drawLink;
        switch(link.bLink.model.getClass().getSimpleName()){

            case "Model_None":
            case "Model_PQ":
            case "Model_Micro":
                drawLink = new DrawLinkPQ(link,
                        nodes.get(link.getStartNodeId()),
                        nodes.get(link.getEndNodeId()),
                        lane_width,
                        link_offset,
                        colormap);
                break;

            case "Model_CTM":
                drawLink = new DrawLinkCTM(link,
                        nodes.get(link.getStartNodeId()),
                        nodes.get(link.getEndNodeId()),
                        lane_width,
                        link_offset,
                        colormap );
                break;

            default:
                throw new OTMException("Link model type not supported.");
        }

        return drawLink;
    }

    private static AbstractDrawNode makeDrawActuator(otmui.model.Actuator actuator, float size) {

        if (actuator==null)
            return new DrawNodeOctagon(-1L,0f,0f,size, Color.BLACK, 0f);

        switch (actuator.type) {
            case signal:
                return new DrawNodeCircle(actuator.getId(), actuator.getXcoord(), -actuator.getYcoord(), size, Color.GREEN, 4f);
            case stop:
                return new DrawNodeOctagon(actuator.getId(), actuator.getXcoord(), -actuator.getYcoord(), size, Color.RED, 0f );
            case other:
                return new DrawNodeOctagon(actuator.getId(), actuator.getXcoord(), -actuator.getYcoord(), size, Color.GREEN, 1f);
        }

        return null;

    }

    private static DrawSensor makeDrawSensor(otmui.model.Sensor sensor, float lane_width, float link_offset) {
        return sensor==null ? new DrawSensor() : new DrawSensor(sensor, lane_width, link_offset);
    }

    private void clear() {
        nodes = new HashMap<>(); // <id,item>
        links = new HashMap<>(); // <id,item>
        actuators = new HashMap<>(); // <id,item>
        sensors = new HashMap<>(); // <id,item>
    }

}
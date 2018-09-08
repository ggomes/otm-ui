package otmui.graph;

import otmui.GlobalParameters;
import otmui.graph.color.AbstractColormap;
import otmui.graph.item.*;
import otmui.model.Link;
import otmui.model.Node;
import otmui.model.Scenario;
import error.OTMException;

import java.util.*;

public class Graph {

    public Map<Long,AbstractDrawNode> nodes; // <id,item>
    public Map<Long,AbstractDrawLink> links; // <id,item>
    public Map<Long,DrawSensor> sensors; // <id,item>

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public Graph(Scenario scenario, GlobalParameters params) throws OTMException {
        clear();

        // create the colormap
        AbstractColormap colormap = params.get_colormap();

        // create nodes
        for (Node node : scenario.getNodes())
            node.drawNode = makeDrawNode(node,params.node_radius.floatValue());

        // create links
        float lane_width_meters = params.lane_width_meters.floatValue();
        float link_offset = params.link_offset.floatValue();
        for (Link link : scenario.getLinks())
            link.drawLink = makeDrawLink(link,lane_width_meters,link_offset,colormap);

        // create sensors
        for (otmui.model.Sensor sensor : scenario.getSensors())
            sensor.drawSensor = makeDrawSensor(sensor, lane_width_meters, link_offset);

    }

    public AbstractDrawNode makeDrawNode(otmui.model.Node node, float radius) {
        AbstractDrawNode drawNode;
        if (node==null)
            drawNode = new DrawNodeCircle(-1L,0f,0f,radius);
        else
            drawNode = new DrawNodeCircle(node.getId(),node.getXcoord(),-node.getYcoord(),radius);
        nodes.put( drawNode.id, drawNode);
        return drawNode;
    }

    public AbstractDrawLink makeDrawLink(otmui.model.Link link, float lane_width, float link_offset, AbstractColormap colormap) throws OTMException {

        AbstractDrawLink drawLink = null;
        switch(link.bLink.model_type){

            case pq:
                drawLink = new PQDrawLink(link,
                        nodes.get(link.getStartNodeId()),
                        nodes.get(link.getEndNodeId()),
                        lane_width,
                        link_offset,
                        colormap);
                break;

            case ctm:
                drawLink = new CTMDrawLink(link,
                        nodes.get(link.getStartNodeId()),
                        nodes.get(link.getEndNodeId()),
                        lane_width,
                        link_offset,
                        colormap );
                break;

            case mn:
                drawLink = new MNDrawLink( link,
                        nodes.get(link.getStartNodeId()),
                        nodes.get(link.getEndNodeId()),
                        lane_width,
                        link_offset,
                        colormap );
                break;

            default:
                throw new OTMException("Link model type not supported.");
        }

        links.put(drawLink.id, drawLink);

        return drawLink;
    }

    public DrawSensor makeDrawSensor(otmui.model.Sensor sensor, float lane_width, float link_offset) {
        DrawSensor drawSensor;
        if (sensor==null)
            drawSensor = new DrawSensor();
        else
            drawSensor = new DrawSensor(sensor, lane_width, link_offset);

        sensors.put(drawSensor.id,drawSensor);

        return drawSensor;
    }

    public void clear() {
        nodes = new HashMap<>(); // <id,item>
        links = new HashMap<>(); // <id,item>
        sensors = new HashMap<>(); // <id,item>
    }

    /////////////////////////////////////////////////
    // getters
    /////////////////////////////////////////////////

    public Collection<AbstractDrawNode> getNodes(){
        return nodes.values();
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

}
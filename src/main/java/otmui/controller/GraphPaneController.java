package otmui.controller;

import api.OTMdev;
import error.OTMException;
import otmui.GlobalParameters;
import otmui.MainApp;
import otmui.event.GraphSelectEvent;
import otmui.graph.GraphContainer;
import otmui.graph.Graph;
import otmui.graph.color.AbstractColormap;
import otmui.graph.item.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import output.animation.AbstractLinkInfo;
import output.animation.AnimationInfo;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class GraphPaneController implements Initializable {

    public MainApp myApp;
    public GraphContainer graphContainer;

    @FXML
    private AnchorPane graphLayout;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        graphContainer = new GraphContainer(this);
        graphLayout.getChildren().add(graphContainer.scrollPane);

        AnchorPane.setBottomAnchor(graphContainer.scrollPane,0d);
        AnchorPane.setTopAnchor(graphContainer.scrollPane,0d);
        AnchorPane.setLeftAnchor(graphContainer.scrollPane,0d);
        AnchorPane.setRightAnchor(graphContainer.scrollPane,0d);
    }

    public void setApp(MainApp myApp){
        this.myApp = myApp;
    }

    public void loadScenario(OTMdev otmdev) {

        try {

            // convert units
            if (!otmdev.scenario.network.node_positions_in_meters) {
                convert_to_meters(otmdev);
            }

           // create the graph, add to the container
            Graph graph = new Graph(otmdev,myApp.params);
            graphContainer.set_graph(graph);

            // set visibility
            graph.getNodes().forEach(x -> x.set_visible(graph.view_nodes));
            graph.getActuators().forEach(x -> x.set_visible(graph.view_actuators));

            // enable click of drawNodes, and recenter on the canvas
            for (AbstractDrawNode drawNode : graph.getNodes()) {
                drawNode.setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        switch(mouseEvent.getClickCount()) {
                            case 1:
                                Event.fireEvent(mouseEvent.getTarget(), new GraphSelectEvent(GraphSelectEvent.CLICK1_NODE, mouseEvent));
                                break;
                            case 2:
                                Event.fireEvent(mouseEvent.getTarget(), new GraphSelectEvent(GraphSelectEvent.CLICK2_NODE, mouseEvent));
                                break;
                        }
                    }
                });
            }

            // enable click of drawLink
            for (AbstractDrawLink drawLink : graph.getLinks()) {
                drawLink.setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        switch(mouseEvent.getClickCount()){
                            case 1:
                                Event.fireEvent(mouseEvent.getTarget(), new GraphSelectEvent(GraphSelectEvent.CLICK1_LINK, mouseEvent));
                                break;
                            case 2:
                                Event.fireEvent(mouseEvent.getTarget(), new GraphSelectEvent(GraphSelectEvent.CLICK2_LINK, mouseEvent));
                                break;
                        }
                    }
                });
            }

            // enable click of drawActuator
            for (AbstractDrawNode drawActuator : graph.getActuators()) {
                drawActuator.setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        switch(mouseEvent.getClickCount()){
                            case 1:
                                Event.fireEvent(mouseEvent.getTarget(), new GraphSelectEvent(GraphSelectEvent.CLICK1_ACTUATOR, mouseEvent));
                                break;
                            case 2:
                                Event.fireEvent(mouseEvent.getTarget(), new GraphSelectEvent(GraphSelectEvent.CLICK2_ACTUATOR, mouseEvent));
                                break;
                        }
                    }
                });
            }

            // enable click of drawSensor
            for (DrawSensor drawSensor : graph.getSensors()) {
                drawSensor.setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        switch(mouseEvent.getClickCount()){
                            case 1:
                                Event.fireEvent(mouseEvent.getTarget(), new GraphSelectEvent(GraphSelectEvent.CLICK1_SENSOR, mouseEvent));
                                break;
                            case 2:
                                Event.fireEvent(mouseEvent.getTarget(), new GraphSelectEvent(GraphSelectEvent.CLICK2_SENSOR, mouseEvent));
                                break;
                        }
                    }
                });
            }

        } catch (OTMException e) {
            e.printStackTrace();
        }

    }

    public void reset(){
        reset_link_color();
    }

    private static void convert_to_meters(OTMdev otmdev) {

        Collection<common.Node> nodes = otmdev.scenario.network.nodes.values();
        Collection<common.Link> links = otmdev.scenario.network.links.values();

        double R = 6378137.0;  // Radius of Earth in meters
        double conv = Math.PI / 180.0;
        double clat = nodes.stream().mapToDouble(n -> n.ycoord).average().getAsDouble()* conv;
        double clon = nodes.stream().mapToDouble(n -> n.xcoord).average().getAsDouble()* conv;
        double cos2 = Math.pow(Math.cos(clat),2);

        for(common.Node node : nodes){
            double lon = node.xcoord * conv;
            double lat = node.ycoord * conv;
            double dx = Math.acos(1-cos2*(1-Math.cos(lon-clon)))*R;
            node.xcoord = (float) (lon<clon ? -dx : dx);
            node.ycoord = (float) ( (lat - clat) * R );
        }

        for(common.Link link : links){
            for(common.Point point : link.shape){
                double lon = point.x * conv;
                double lat = point.y* conv;
                double dx = Math.acos(1-cos2*(1-Math.cos(lon-clon)))*R;
                point.x = (float) (lon<clon ? -dx : dx);
                point.y = (float) ( (lat - clat) * R );
            }
        }

        otmdev.scenario.network.node_positions_in_meters = true;
    }

    /////////////////////////////////////////////////
    // drawing
    /////////////////////////////////////////////////

    public void paintLinkShapes(Set<Long> link_ids){
        Graph graph = graphContainer.get_graph();
        graph.getLinks().stream()
                .filter(l->link_ids.contains(l))
                .forEach(x -> x.paintShape(graph.link_offset,graph.lane_width_meters,graph.road_color_scheme));
    }

    public void paintLinkShapes() {
        Graph graph = graphContainer.get_graph();
        float new_width = myApp.params.lane_width_meters.floatValue();
        float new_offset = myApp.params.link_offset.floatValue();
        GlobalParameters.RoadColorScheme new_color_scheme = (GlobalParameters.RoadColorScheme) myApp.params.road_color_scheme.getValue();
        if (Math.abs(new_width-graph.lane_width_meters)>0.1f
                || Math.abs(new_offset-graph.link_offset)>0.1f
                || !new_color_scheme.equals(graph.road_color_scheme) ) {
            graph.getLinks().forEach(x -> x.paintShape(new_offset,new_width,new_color_scheme));
            graph.lane_width_meters = new_width;
            graph.link_offset = new_offset;
            graph.road_color_scheme = new_color_scheme;
        }
    }

    public void paintLinkColors() {
        Graph graph = graphContainer.get_graph();
        GlobalParameters.RoadColorScheme new_color_map = (GlobalParameters.RoadColorScheme) myApp.params.road_color_scheme.getValue();
        if(!new_color_map.equals(graph.road_color_scheme)){
            graph.getLinks().forEach(x -> x.paintColor(new_color_map));
            graph.road_color_scheme = new_color_map;
        }
    }

    public void paintNodeShapes() {

        Graph graph = graphContainer.get_graph();

        // set node size
        float new_size = myApp.params.node_size.floatValue();
        if (Math.abs(new_size-graph.node_size)>0.1f) {
            graph.getNodes().forEach(x -> x.set_size(new_size));
            graph.node_size = new_size;
        }

        // set node visible
        boolean new_view_nodes = myApp.params.view_nodes.getValue();
        if(new_view_nodes!=graph.view_nodes) {
            graph.getNodes().forEach(x -> x.set_visible(new_view_nodes));
            graph.view_nodes = new_view_nodes;
        }
    }

    public void paintActuators() {

        Graph graph = graphContainer.get_graph();

        // set node visible
        boolean new_view_actuators = myApp.params.view_actuators.getValue();
        if(new_view_actuators!=graph.view_actuators) {
            graph.getActuators().forEach(x -> x.set_visible(new_view_actuators));
            graph.view_actuators = new_view_actuators;
        }

    }

    /////////////////////////////////////////////////
    // context menu
    /////////////////////////////////////////////////

    public void merge_nodes(){

        System.out.println("MERGING NODES");


//        Scenario scn = myApp.scenario;
////        Set<Long> node_ids = myApp.selectionManager.selectedNodes.stream().map(x->x.id).collect(toSet());
//        Set<Long> node_ids = new HashSet<>();
//
//        node_ids.add(243670957L);
//        node_ids.add(53085641L);
//        node_ids.add(243670955L);
//
//        // set of scenario nodes
//        Set<Node> nodes = node_ids.stream().map(id->scn.getNodeWithId(id)).collect(toSet());
//
//        // TODO the nodes mustn't have more than one actuator
//
//        float xcoord = (float) nodes.stream().mapToDouble(n->n.getXcoord()).average().getAsDouble();
//        float ycoord = (float) nodes.stream().mapToDouble(n->n.getYcoord()).average().getAsDouble();
//        Node new_node = scn.create_node(xcoord,ycoord);
//
//        // set of adjacent links
//        Set<Long> links = nodes.stream().flatMap(n->n.getInLinkIds().stream()).collect(toSet());
//        links.addAll(nodes.stream().flatMap(n->n.getOutLinkIds().stream()).collect(toSet()));
//
//        // process internal and external links
//        for(Long link_id : links){
//            Link link = scn.getLinkWithId(link_id);
//            boolean starts_at = node_ids.contains(link.getStartNodeId());
//            boolean ends_at = node_ids.contains(link.getEndNodeId());
//            if(starts_at && ends_at)
//                scn.delete_link(link);
//            else if(starts_at)
//                link.setStartNode(new_node);
//            else if(ends_at)
//                link.setEndNode(new_node);
//        }
//
//        // delete nodes from scenario
//        for(Node node : nodes) {
//
//            long node_id = node.getId();
//
//            if(!scn.network.nodes.containsKey(node_id))
//                continue;
//
//            // remove from the network
//            scn.network.nodes.remove(node_id);
//
//            for(common.Link link : node.cnode.in_links.values())
//                link.end_node = null;
//            for(common.Link link : node.cnode.out_links.values())
//                link.start_node = null;
//            scn.otm.scenario.network.nodes.remove(node_id);
//
//            // remove actuators
//            if(node.actuator!=null)
//                scn.actuators.remove(node.actuator.id);
//
//            // remove splits
//            if(scn.splits!=null)
//                scn.splits.remove(node_id);
//
//        }
//
//        // delete nodes from ui
//        graphContainer.pane.getChildren().removeAll(nodes.stream().map(n->n.drawNode).collect(Collectors.toSet()));
//
//        // create draw node
////        graphContainer.add_node(new_node);
    }

    public void merge_links(){
        System.out.println("merge_links");
        Set<Long> link_ids = myApp.selectionManager.selectedLinks.stream().map(x->x.id).collect(toSet());
        System.out.println(link_ids);
    }

    /////////////////////////////////////////////////
    // highlighting
    /////////////////////////////////////////////////

    public void highlightNode(AbstractDrawNode drawNode){
        drawNode.highlight();
    }

    public void unhighlightNode(AbstractDrawNode drawNode){
        drawNode.unhighlight();
    }

    public void highlightLink(AbstractDrawLink drawLink){
        drawLink.highlight();
    }

    public void unhighlightLink(AbstractDrawLink drawLink){
        drawLink.unhighlight();
    }

    public void highlightSensor(DrawSensor drawSensor){
        drawSensor.highlight();
    }

    public void unhighlightSensor(DrawSensor drawSensor){
        drawSensor.unhighlight();
    }

    public void highlightActuator(AbstractDrawNode drawActuator){
        drawActuator.highlight();
    }

    public void unhighlightActuator(AbstractDrawNode drawActuator){
        drawActuator.unhighlight();
    }

    public void unhighlightAll(){
        Graph graph = graphContainer.get_graph();
        if(graph.getLinks()!=null)
            graph.getLinks().forEach(x-> unhighlightLink(x));
        if(graph.getNodes()!=null)
            graph.getNodes().forEach(x-> unhighlightNode(x));
        if(graph.getSensors()!=null)
            graph.getSensors().forEach(x-> unhighlightSensor(x));
        if(graph.getActuators()!=null)
            graph.getActuators().forEach(x-> unhighlightActuator(x));
    }

    public void highlight(Set<AbstractDrawLink> selectLinks, Set<AbstractDrawNode> selectNodes, Set<DrawSensor> selectSensors,Set<AbstractDrawNode> selectActuators){
        unhighlightAll();
        selectLinks.forEach(x-> highlightLink(x));
        selectNodes.forEach(x-> highlightNode(x));
        selectSensors.forEach(x-> highlightSensor(x));
        selectActuators.forEach(x-> highlightActuator(x));
    }

    /////////////////////////////////////////////////
    // focusing the graph
    /////////////////////////////////////////////////

    public void focusGraphOnSelection(){

        Set<Double> allX = new HashSet<>();
        Set<Double> allY = new HashSet<>();

        Set<AbstractDrawNode> drawNodes = myApp.selectionManager.selectedNodes;
        Set<AbstractDrawLink> drawLinks = myApp.selectionManager.selectedLinks;
        Set<DrawSensor> drawSensors = myApp.selectionManager.selectedSensors;
        Set<AbstractDrawNode> drawActuators = myApp.selectionManager.selectedActuators;

        if(drawLinks.isEmpty() && drawNodes.isEmpty() && drawSensors.isEmpty() && drawActuators.isEmpty())
            return;

        // collect node positions
        allX.addAll(drawNodes.stream().map(x->x.getXPos()).collect(toSet()));
        allY.addAll(drawNodes.stream().map(x->x.getYPos()).collect(toSet()));

        // collect sensor positions
        allX.addAll(drawSensors.stream().map(x->x.getXPos()).collect(toSet()));
        allY.addAll(drawSensors.stream().map(x->x.getYPos()).collect(toSet()));

        // collect actuator positions
        allX.addAll(drawActuators.stream().map(x->x.getXPos()).collect(toSet()));
        allY.addAll(drawActuators.stream().map(x->x.getYPos()).collect(toSet()));

        // collect link start positions
        allX.addAll(drawLinks.stream().map(x->x.getStartPosX()).collect(toSet()));
        allY.addAll(drawLinks.stream().map(x->x.getStartPosY()).collect(toSet()));

        // collect link end positions
        allX.addAll(drawLinks.stream().map(x->x.getEndPosX()).collect(toSet()));
        allY.addAll(drawLinks.stream().map(x->x.getEndPosY()).collect(toSet()));

        if(allX.isEmpty() || allY.isEmpty())
            return;

        double cX = allX.stream().mapToDouble(x -> x).average().getAsDouble();
        double cY = allY.stream().mapToDouble(x -> x).average().getAsDouble();

//        graphContainer.scrollPane.setLayoutX(100 + graphContainer.scrollPane.getLayoutX());
//        graphContainer.canvas.setLayoutX(100 + graphContainer.canvas.getLayoutX());
//        graphContainer.pane.setLayoutX(100 + graphContainer.pane.getLayoutX());

//        graphContainer.scrollPane.getContent().setTranslateX(100);
//        graphContainer.scrollPane.getContent().setTranslateY(100);


//        graphContainer.scrollPane.layout();
    }

    /////////////////////////////////////////////////
    // animation
    /////////////////////////////////////////////////

    public void reset_link_color(){
        for(AbstractDrawLink drawLink : graphContainer.get_graph().drawlinks.values())
            for (AbstractDrawLanegroup drawLanegroup : drawLink.draw_lanegroups)
                drawLanegroup.unhighlight();
    }

    public void draw_link_state(AnimationInfo info,AbstractColormap colormap){

        if(graphContainer==null)
            return;
        if(graphContainer.get_graph()==null)
            return;

        for(AbstractDrawLink drawLink : graphContainer.get_graph().drawlinks.values()) {
            AbstractLinkInfo linkInfo = info.link_info.get(drawLink.id);
            for (AbstractDrawLanegroup drawLanegroup : drawLink.draw_lanegroups) {
                drawLanegroup.draw_state(linkInfo.lanegroup_info.get(drawLanegroup.id), colormap);
            }
        }

    }

}

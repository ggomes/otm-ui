package otmui.controller;

import api.OTMdev;
import error.OTMException;
import javafx.scene.Scene;
import otmui.GlobalParameters;
import otmui.MainApp;
import otmui.ScenarioModification;
import otmui.event.*;
import otmui.graph.GraphContainer;
import otmui.graph.Graph;
import otmui.graph.color.AbstractColormap;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import otmui.item.*;
import output.animation.AbstractLinkInfo;
import output.animation.AnimationInfo;

import java.net.URL;
import java.util.*;

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

    public void attach_event_listeners(MainApp myApp){
        this.myApp = myApp;

        Scene scene = myApp.stage.getScene();

        scene.addEventFilter(NewScenarioEvent.SCENARIO_LOADED_OTM, e->loadScenario(e.otmdev) );
        scene.addEventFilter(DeleteElementEvent.REMOVE_LINK, e->remove_link((common.Link)e.item));
        scene.addEventFilter(DeleteElementEvent.REMOVE_NODE, e->remove_node((common.Node)e.item));

        scene.addEventFilter(ParameterChange.DRAWLINKSHAPES, e->paintLinkShapes());
        scene.addEventFilter(ParameterChange.DRAWLINKCOLORS, e->paintLinkColors());
        scene.addEventFilter(ParameterChange.DRAWNODESHAPES, e->paintNodeShapes());
//        scene.addEventFilter(ParameterChange.DRAWNODECOLORS, e->paintNodeColors());
        scene.addEventFilter(ParameterChange.DRAWACTUATORS,e->paintActuators());

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
            for (AbstractNode drawNode : graph.getNodes()) {
                drawNode.setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        switch(mouseEvent.getClickCount()) {
                            case 1:
                                Event.fireEvent(mouseEvent.getTarget(), new GraphSelectEvent(GraphSelectEvent.CLICK1, mouseEvent));
                                break;
                            case 2:
                                Event.fireEvent(mouseEvent.getTarget(), new GraphSelectEvent(GraphSelectEvent.CLICK2_NODE, mouseEvent));
                                break;
                        }
                    }
                });
            }

            // enable click of drawLink
            for (AbstractLink drawLink : graph.getLinks()) {
                drawLink.setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        switch(mouseEvent.getClickCount()){
                            case 1:
                                Event.fireEvent(mouseEvent.getTarget(), new GraphSelectEvent(GraphSelectEvent.CLICK1, mouseEvent));
                                break;
                            case 2:
                                Event.fireEvent(mouseEvent.getTarget(), new GraphSelectEvent(GraphSelectEvent.CLICK2_LINK, mouseEvent));
                                break;
                        }
                    }
                });
            }

            // enable click of drawActuator
            for (AbstractActuator drawActuator : graph.getActuators()) {
                drawActuator.setOnMouseClicked(mouseEvent -> {
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                        switch(mouseEvent.getClickCount()){
                            case 1:
                                Event.fireEvent(mouseEvent.getTarget(), new GraphSelectEvent(GraphSelectEvent.CLICK1, mouseEvent));
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
                                Event.fireEvent(mouseEvent.getTarget(), new GraphSelectEvent(GraphSelectEvent.CLICK1, mouseEvent));
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

    public void merge_nodes() {

        try {
            runner.Scenario scenario = myApp.otm.scenario;

//        Set<Long> node_ids = myApp.selectionManager.selectedNodes.stream().map(x->x.id).collect(toSet());

            /////////////////////////////////////////////////////////
            Set<Long> node_ids = new HashSet<>();
            node_ids.add(243670957L);
            node_ids.add(53085641L);
            node_ids.add(243670955L);
            ScenarioModification.delete_actuator(myApp,scenario.network.nodes.get(243670957L).actuator);
            /////////////////////////////////////////////////////////

            // set of scenario nodes
            Set<common.Node> nodes = node_ids.stream().map(id->scenario.network.nodes.get(id)).collect(toSet());

            // Collect actuators
            Set<actuator.AbstractActuator> actuators = nodes.stream()
                    .filter(n->n.actuator!=null)
                    .map(n->n.actuator)
                    .collect(toSet());

            if(actuators.size()>1){
                System.out.println("Aborting: The nodes have multiple actuators. Please remove some first.");
                return;
            }

            actuator.AbstractActuator actuator = actuators.size()==1 ? actuators.iterator().next() : null;

            // Remove the actuator from the node
            if(actuator!=null){
                common.Node node = nodes.stream().filter(n->n.getId().equals(actuator.target.getId())).findFirst().get();
                node.actuator = null;
                actuator.target = null;
            }

            // create new node
            common.Node new_node = ScenarioModification.create_node(myApp,
                    (float) nodes.stream().mapToDouble(n->n.xcoord).average().getAsDouble(),
                    (float) nodes.stream().mapToDouble(n->n.ycoord).average().getAsDouble());

            // attach the actuator
            if(actuator!=null){
                new_node.actuator = actuator;
                actuator.target = new_node;
            }

            // set of adjacent links
            Set<Long> links = nodes.stream().flatMap(n->n.in_links.keySet().stream()).collect(toSet());
            links.addAll(nodes.stream().flatMap(n->n.out_links.keySet().stream()).collect(toSet()));

            // process internal and external links
            for(Long link_id : links){
                common.Link link = scenario.network.links.get(link_id);
                boolean starts_at = node_ids.contains(link.start_node.getId());
                boolean ends_at = node_ids.contains(link.end_node.getId());
                if(starts_at && ends_at)
                    ScenarioModification.delete_link(myApp,link);
                else if(starts_at)
                    link.start_node = new_node;
                else if(ends_at)
                    link.end_node = new_node;
            }

            // delete nodes from scenario
            for(common.Node node : nodes)
                ScenarioModification.delete_node(myApp,node);

//        // delete nodes from ui
//        Graph graph = graphContainer.get_graph();
//
//        graphContainer.pane.getChildren().removeAll(nodes.stream().map(n->n.drawNode).collect(Collectors.toSet()));

            // create draw node
//        graphContainer.add_node(new_node);
        } catch (OTMException e) {
            e.printStackTrace();
        }
    }

    public void merge_links(){
//        System.out.println("merge_links");
//        Set<Long> link_ids = myApp.selectionManager.selectedLinks.stream().map(x->x.id).collect(toSet());
//        System.out.println(link_ids);
    }

    /////////////////////////////////////////////////
    // modifiication
    /////////////////////////////////////////////////

    public void remove_link(common.Link link){
        if(graphContainer.get_graph().drawlinks.containsKey(link.getId())){
            AbstractLink drawLink = graphContainer.get_graph().drawlinks.get(link.getId());
            graphContainer.pane.getChildren().remove(drawLink);
            graphContainer.get_graph().drawlinks.remove(drawLink);
        }
    }

    public void remove_node(common.Node node){
        if(graphContainer.get_graph().drawnodes.containsKey(node.getId())){
            AbstractNode drawNode = graphContainer.get_graph().drawnodes.get(node.getId());
            graphContainer.pane.getChildren().remove(drawNode);
            graphContainer.get_graph().drawnodes.remove(node.getId());
        }
    }

    /////////////////////////////////////////////////
    // highlighting
    /////////////////////////////////////////////////

    public void highlightNode(AbstractNode drawNode){

        System.out.println("Highlight drawnode " + drawNode.getId());


        drawNode.highlight();
    }

    public void unhighlightNode(AbstractNode drawNode){
        drawNode.unhighlight();
    }

    public void highlightLink(AbstractLink drawLink){
        drawLink.highlight();
    }

    public void unhighlightLink(AbstractLink drawLink){
        drawLink.unhighlight();
    }

    public void highlightSensor(DrawSensor drawSensor){
        drawSensor.highlight();
    }

    public void unhighlightSensor(DrawSensor drawSensor){
        drawSensor.unhighlight();
    }

    public void highlightActuator(AbstractActuator drawActuator){
        drawActuator.highlight();
    }

    public void unhighlightActuator(AbstractActuator drawActuator){
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

//    public void highlight(Set<AbstractDrawLink> selectLinks, Set<AbstractDrawNode> selectNodes, Set<DrawSensor> selectSensors,Set<AbstractDrawActuator> selectActuators){
//        unhighlightAll();
//        selectLinks.forEach(x-> highlightLink(x));
//        selectNodes.forEach(x-> highlightNode(x));
//        selectSensors.forEach(x-> highlightSensor(x));
//        selectActuators.forEach(x-> highlightActuator(x));
//    }
    public void highlight(Map<Class,Set<AbstractItem>> selection){
        unhighlightAll();
        for(Set<AbstractItem> X : selection.values())
            X.forEach(x-> x.highlight());
//        selectLinks.forEach(x-> highlightLink(x));
//        selectNodes.forEach(x-> highlightNode(x));
//        selectSensors.forEach(x-> highlightSensor(x));
//        selectActuators.forEach(x-> highlightActuator(x));
    }

    /////////////////////////////////////////////////
    // focusing the graph
    /////////////////////////////////////////////////

    public void focusGraphOnSelection(){
//
//        Set<Double> allX = new HashSet<>();
//        Set<Double> allY = new HashSet<>();

//        Set<AbstractDrawNode> drawNodes = myApp.selectionManager.selectedNodes;
//        Set<AbstractDrawLink> drawLinks = myApp.selectionManager.selectedLinks;
//        Set<DrawSensor> drawSensors = myApp.selectionManager.selectedSensors;
//        Set<AbstractDrawActuator> drawActuators = myApp.selectionManager.selectedActuators;

//        if(drawLinks.isEmpty() && drawNodes.isEmpty() && drawSensors.isEmpty() && drawActuators.isEmpty())
//            return;
//
//        // collect node positions
//        allX.addAll(drawNodes.stream().map(x->x.getXPos()).collect(toSet()));
//        allY.addAll(drawNodes.stream().map(x->x.getYPos()).collect(toSet()));
//
//        // collect sensor positions
//        allX.addAll(drawSensors.stream().map(x->x.getXPos()).collect(toSet()));
//        allY.addAll(drawSensors.stream().map(x->x.getYPos()).collect(toSet()));
//
//        // collect actuator positions
//        allX.addAll(drawActuators.stream().map(x->x.getXPos()).collect(toSet()));
//        allY.addAll(drawActuators.stream().map(x->x.getYPos()).collect(toSet()));
//
//        // collect link start positions
//        allX.addAll(drawLinks.stream().map(x->x.getStartPosX()).collect(toSet()));
//        allY.addAll(drawLinks.stream().map(x->x.getStartPosY()).collect(toSet()));
//
//        // collect link end positions
//        allX.addAll(drawLinks.stream().map(x->x.getEndPosX()).collect(toSet()));
//        allY.addAll(drawLinks.stream().map(x->x.getEndPosY()).collect(toSet()));
//
//        if(allX.isEmpty() || allY.isEmpty())
//            return;
//
//        double cX = allX.stream().mapToDouble(x -> x).average().getAsDouble();
//        double cY = allY.stream().mapToDouble(x -> x).average().getAsDouble();
//
////        graphContainer.scrollPane.setLayoutX(100 + graphContainer.scrollPane.getLayoutX());
////        graphContainer.canvas.setLayoutX(100 + graphContainer.canvas.getLayoutX());
////        graphContainer.pane.setLayoutX(100 + graphContainer.pane.getLayoutX());
//
////        graphContainer.scrollPane.getContent().setTranslateX(100);
////        graphContainer.scrollPane.getContent().setTranslateY(100);
//
//
////        graphContainer.scrollPane.layout();
    }

    /////////////////////////////////////////////////
    // animation
    /////////////////////////////////////////////////

    public void reset_link_color(){
        for(AbstractLink drawLink : graphContainer.get_graph().drawlinks.values())
            for (AbstractDrawLanegroup drawLanegroup : drawLink.draw_lanegroups)
                drawLanegroup.unhighlight();
    }

    public void draw_link_state(AnimationInfo info,AbstractColormap colormap){

        if(graphContainer==null)
            return;
        if(graphContainer.get_graph()==null)
            return;

        for(AbstractLink drawLink : graphContainer.get_graph().drawlinks.values()) {
            AbstractLinkInfo linkInfo = info.link_info.get(drawLink.id);
            for (AbstractDrawLanegroup drawLanegroup : drawLink.draw_lanegroups) {
                drawLanegroup.draw_state(linkInfo.lanegroup_info.get(drawLanegroup.id), colormap);
            }
        }

    }

}

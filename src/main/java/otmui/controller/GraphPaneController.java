package otmui.controller;

import error.OTMException;
import otmui.GlobalParameters;
import otmui.MainApp;
import otmui.event.GraphSelectEvent;
import otmui.graph.GraphContainer;
import otmui.graph.Graph;
import otmui.graph.color.AbstractColormap;
import otmui.graph.item.*;
import otmui.model.Scenario;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import output.animation.AbstractLinkInfo;
import output.animation.AnimationInfo;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

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
        graphContainer = new GraphContainer();
        graphLayout.getChildren().add(graphContainer.scrollPane);

        AnchorPane.setBottomAnchor(graphContainer.scrollPane,0d);
        AnchorPane.setTopAnchor(graphContainer.scrollPane,0d);
        AnchorPane.setLeftAnchor(graphContainer.scrollPane,0d);
        AnchorPane.setRightAnchor(graphContainer.scrollPane,0d);
    }

    public void setApp(MainApp myApp){
        this.myApp = myApp;
    }

    public void loadScenario(Scenario scenario) throws OTMException {

        // create the graph, add to the container
        Graph graph = new Graph(scenario,myApp.params);
        graphContainer.set_graph(graph);

        // set visibility
        graph.getNodes().forEach(x -> x.set_visible(graph.view_nodes));
        graph.getActuators().forEach(x -> x.set_visible(graph.view_actuators));

        // enable double click of drawNodes, and recenter on the canvas
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

        // enable double click of drawLink
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

        // enable double click of drawActuator
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

        // enable double click of drawSensor
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

    }

    public void reset(){
        reset_link_color();
    }

    /////////////////////////////////////////////////
    // drawing
    /////////////////////////////////////////////////

    public void paintLinkShapes() {
        Graph graph = graphContainer.get_graph();
        float new_width = myApp.params.lane_width_meters.floatValue();
        float new_offset = myApp.params.link_offset.floatValue();
        GlobalParameters.RoadColorScheme new_color_map = (GlobalParameters.RoadColorScheme) myApp.params.road_color_scheme.getValue();
        if (Math.abs(new_width-graph.lane_width_meters)>0.1f
                || Math.abs(new_offset-graph.link_offset)>0.1f
                || !new_color_map.equals(graph.road_color_scheme) ) {
            graph.getLinks().forEach(x -> x.paintShape(new_offset,new_width,new_color_map));
            graph.lane_width_meters = new_width;
            graph.link_offset = new_offset;
            graph.road_color_scheme = new_color_map;
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
        allX.addAll(drawNodes.stream().map(x->x.getXPos()).collect(Collectors.toSet()));
        allY.addAll(drawNodes.stream().map(x->x.getYPos()).collect(Collectors.toSet()));

        // collect sensor positions
        allX.addAll(drawSensors.stream().map(x->x.getXPos()).collect(Collectors.toSet()));
        allY.addAll(drawSensors.stream().map(x->x.getYPos()).collect(Collectors.toSet()));

        // collect actuator positions
        allX.addAll(drawActuators.stream().map(x->x.getXPos()).collect(Collectors.toSet()));
        allY.addAll(drawActuators.stream().map(x->x.getYPos()).collect(Collectors.toSet()));

        // collect link start positions
        allX.addAll(drawLinks.stream().map(x->x.getStartPosX()).collect(Collectors.toSet()));
        allY.addAll(drawLinks.stream().map(x->x.getStartPosY()).collect(Collectors.toSet()));

        // collect link end positions
        allX.addAll(drawLinks.stream().map(x->x.getEndPosX()).collect(Collectors.toSet()));
        allY.addAll(drawLinks.stream().map(x->x.getEndPosY()).collect(Collectors.toSet()));

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
        for(AbstractDrawLink drawLink : graphContainer.get_graph().links.values())
            for (AbstractDrawLanegroup drawLanegroup : drawLink.draw_lanegroups)
                drawLanegroup.unhighlight();
    }

    public void draw_link_state(AnimationInfo info,AbstractColormap colormap){

        if(graphContainer==null)
            return;
        if(graphContainer.get_graph()==null)
            return;

        for(AbstractDrawLink drawLink : graphContainer.get_graph().links.values()) {
            AbstractLinkInfo linkInfo = info.link_info.get(drawLink.id);
            for (AbstractDrawLanegroup drawLanegroup : drawLink.draw_lanegroups) {
                drawLanegroup.draw_state(linkInfo.lanegroup_info.get(drawLanegroup.id), colormap);
            }
        }

    }

}

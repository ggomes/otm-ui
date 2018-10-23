/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */

package otmui.controller;

import error.OTMException;
import otmui.MainApp;
import otmui.event.GraphSelectEvent;
import otmui.event.TreeSelectEvent;
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
import output.animation.AbstractLaneGroupInfo;
import output.animation.AbstractLinkInfo;
import output.animation.AnimationInfo;

import java.net.URL;
import java.util.HashSet;
import java.util.Map;
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

        this.graphLayout.addEventHandler(TreeSelectEvent.CLICK1, e->System.out.println("SDSDFSDF"));
    }

    public void setApp(MainApp myApp){
        this.myApp = myApp;
    }

    public void loadScenario(Scenario scenario) throws OTMException {

        // create the graph, add to the container
        Graph graph = new Graph(scenario,myApp.params);
        graphContainer.set_graph(graph);

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

    public void unhighlightAll(){
        Graph graph = graphContainer.get_graph();
        graph.getLinks().forEach(x-> unhighlightLink(x));
        graph.getNodes().forEach(x-> unhighlightNode(x));
        graph.getSensors().forEach(x-> unhighlightSensor(x));
    }

    public void highlight(Set<AbstractDrawLink> selectLinks, Set<AbstractDrawNode> selectNodes, Set<DrawSensor> selectSensors){
        unhighlightAll();
        selectLinks.forEach(x-> highlightLink(x));
        selectNodes.forEach(x-> highlightNode(x));
        selectSensors.forEach(x-> highlightSensor(x));
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

        if(drawLinks.isEmpty() && drawNodes.isEmpty() && drawSensors.isEmpty())
            return;

        // collect node positions
        allX.addAll(drawNodes.stream().map(x->x.getXPos()).collect(Collectors.toSet()));
        allY.addAll(drawNodes.stream().map(x->x.getYPos()).collect(Collectors.toSet()));

        // collect sensor positions
        allX.addAll(drawSensors.stream().map(x->x.getXPos()).collect(Collectors.toSet()));
        allY.addAll(drawSensors.stream().map(x->x.getYPos()).collect(Collectors.toSet()));

        // collect link start positions
        allX.addAll(drawLinks.stream().map(x->x.getStartPosX()).collect(Collectors.toSet()));
        allY.addAll(drawLinks.stream().map(x->x.getStartPosY()).collect(Collectors.toSet()));

        // collect link end positions
        allX.addAll(drawLinks.stream().map(x->x.getEndPosX()).collect(Collectors.toSet()));
        allY.addAll(drawLinks.stream().map(x->x.getEndPosY()).collect(Collectors.toSet()));

//        // compute bounding box
//        Double minX = allX.stream().min(Double::compareTo).get();
//        Double maxX = allX.stream().max(Double::compareTo).get();
//        Double minY = allY.stream().min(Double::compareTo).get();
//        Double maxY = allY.stream().max(Double::compareTo).get();
//
//        System.out.println("minX: " + minX);
//        System.out.println("maxX: " + maxX);
//        System.out.println("minY: " + minY);
//        System.out.println("maxY: " + maxY);

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

package otmui.controller;

import error.OTMException;
import javafx.scene.Scene;
import javafx.scene.shape.Shape;
import otmui.*;
import otmui.event.*;
import otmui.graph.GraphContainer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import otmui.item.*;

import java.util.*;

import static java.util.stream.Collectors.toSet;

public class GraphPaneController {

    public MainApp myApp;
    public GraphContainer graphContainer;

    @FXML
    private AnchorPane graphLayout;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public void initialize(MainApp myApp){
        this.myApp = myApp;

        graphContainer = new GraphContainer(this);
        graphLayout.getChildren().add(graphContainer.scrollPane);

        AnchorPane.setBottomAnchor(graphContainer.scrollPane,0d);
        AnchorPane.setTopAnchor(graphContainer.scrollPane,0d);
        AnchorPane.setLeftAnchor(graphContainer.scrollPane,0d);
        AnchorPane.setRightAnchor(graphContainer.scrollPane,0d);

        // events
        Scene scene = myApp.stage.getScene();

        scene.addEventFilter(NewScenarioEvent.SCENARIO_LOADED, e->loadScenario(e.data) );
        scene.addEventFilter(DoHighlightSelection.HIGHLIGHT_ANY, e->highlight(e.selection) );

//        scene.addEventFilter(DeleteElementEvent.REMOVE_LINK, e->remove_link((common.Link)e.item));
//        scene.addEventFilter(DeleteElementEvent.REMOVE_NODE, e->remove_node((common.Node)e.item));
//
//        scene.addEventFilter(ParameterChange.DRAWLINKSHAPES, e->paintLinkShapes(e.itempool.items.get(ItemType.link).values(),e.params));
//        scene.addEventFilter(ParameterChange.DRAWLINKCOLORS, e->paintLinkColors(e.itempool.items.get(ItemType.link).values(),e.params));
//        scene.addEventFilter(ParameterChange.DRAWNODESHAPES, e->paintNodeShapes(e.itempool.items.get(ItemType.node).values(),e.params));
////        scene.addEventFilter(ParameterChange.DRAWNODECOLORS,                e->paintNodeColors(e.itempool.items.get(ItemType.node).values(),e.params));
//        scene.addEventFilter(ParameterChange.DRAWACTUATORS, e->paintActuators(e.itempool.items.get(ItemType.actuator).values(),e.params));

    }

    public void loadScenario(Data data) {

       // put all items into the pane
        graphContainer.set_items(data);

        // set visibility
//            if(!view_nodes)
//                itempool.items.get(ItemType.node).forEach(x -> x.set_visible(false));
//
//            if(!view_actuators)
//                itempool.items.get(ItemType.actuator).forEach(x -> x.set_visible(false));

        // graph item clicks
        for(ItemType type : Arrays.asList(ItemType.node,ItemType.link,ItemType.actuator,ItemType.sensor))
            for (AbstractItem item : data.items.get(type).values())
                for (Shape shape : ((AbstractGraphItem) item).shapegroup)
                    shape.setOnMouseClicked(mouseEvent -> {
                        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                            switch (mouseEvent.getClickCount()) {
                                case 1:
                                    Event.fireEvent(mouseEvent.getTarget(), new GraphClickEvent(GraphClickEvent.CLICK1, (AbstractGraphItem) item, mouseEvent));
                                    break;
                                case 2:
                                    Event.fireEvent(mouseEvent.getTarget(), new GraphClickEvent(GraphClickEvent.CLICK2, (AbstractGraphItem) item, mouseEvent));
                                    break;
                            }
                        }
                    });
    }

//    public void reset(){
//        reset_link_color();
//    }

    /////////////////////////////////////////////////
    // drawing
    /////////////////////////////////////////////////

    public void paintLinkShapes(Collection<AbstractItem> links,GlobalParameters params){
        links.forEach(x -> ((Link)x).paintShape(params.link_offset(),params.lane_width_meters(),params.road_color_scheme()));
    }

//    public void paintLinkShapes(Set<AbstractItem> links,GlobalParameters params) {
//        float new_width = myApp.params.lane_width_meters.floatValue();
//        float new_offset = myApp.params.link_offset.floatValue();
//        GlobalParameters.RoadColorScheme new_color_scheme = (GlobalParameters.RoadColorScheme) myApp.params.road_color_scheme.getValue();
//        if (Math.abs(new_width-params.lane_width_meters)>0.1f
//                || Math.abs(new_offset-params.link_offset)>0.1f
//                || !new_color_scheme.equals(params.road_color_scheme) ) {
//            graph.getLinks().forEach(x -> x.paintShape(new_offset,new_width,new_color_scheme));
//            graph.lane_width_meters = new_width;
//            graph.link_offset = new_offset;
//            graph.road_color_scheme = new_color_scheme;
//        }
//    }

    public void paintLinkColors(Collection<AbstractItem> links,GlobalParameters params) {
        GlobalParameters.RoadColorScheme new_color_map = (GlobalParameters.RoadColorScheme) myApp.params.road_color_scheme.getValue();
        if(!new_color_map.equals(params.road_color_scheme)){
            links.forEach(x -> ((Link)x).paintColor(new_color_map));
//            params.road_color_scheme = new_color_map;
        }
    }

//    public void paintNodeShapes(Collection<AbstractItem> nodes,GlobalParameters params) {
//        // set node size
//        float new_size = myApp.params.node_size.floatValue();
////        if (Math.abs(new_size-params.node_size)>0.1f) {
//            nodes.forEach(x -> ((AbstractPointItem) x).set_size(new_size));
////            graph.node_size = new_size;
////        }
//
//        // set node visible
//        boolean new_view_nodes = myApp.params.view_nodes.getValue();
////        if(new_view_nodes!=graph.view_nodes) {
//            nodes.forEach(x -> ((AbstractPointItem) x).set_visible(new_view_nodes));
////            graph.view_nodes = new_view_nodes;
////        }
//    }

//    public void paintActuators(Collection<AbstractItem> actuators,GlobalParameters params) {
//        // set node visible
////        boolean new_view_actuators = myApp.params.view_actuators.getValue();
////        if(new_view_actuators!=params.view_actuators) {
//            actuators.forEach(x -> ((AbstractPointItem) x).set_visible(params.view_actuators()));
////            params.view_actuators = new_view_actuators;
////        }
//
//    }

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

//    public void remove_link(common.Link link){
//        if(graphContainer.get_graph().drawlinks.containsKey(link.getId())){
//            BaseLink drawLink = graphContainer.get_graph().drawlinks.get(link.getId());
//            graphContainer.pane.getChildren().remove(drawLink);
//            graphContainer.get_graph().drawlinks.remove(drawLink);
//        }
//    }

//    public void remove_node(common.Node node){
//        if(graphContainer.get_graph().drawnodes.containsKey(node.getId())){
//            BaseNode drawNode = graphContainer.get_graph().drawnodes.get(node.getId());
//            graphContainer.pane.getChildren().remove(drawNode);
//            graphContainer.get_graph().drawnodes.remove(node.getId());
//        }
//    }

    /////////////////////////////////////////////////
    // highlighting
    /////////////////////////////////////////////////

    public void unhighlightAll(){
        myApp.data.items.get(ItemType.node).values().forEach(x->((AbstractGraphItem)x).unhighlight());
        myApp.data.items.get(ItemType.link).values().forEach(x->((AbstractGraphItem)x).unhighlight());
        myApp.data.items.get(ItemType.actuator).values().forEach(x->((AbstractGraphItem)x).unhighlight());
        myApp.data.items.get(ItemType.sensor).values().forEach(x->((AbstractGraphItem)x).unhighlight());
    }

    public void highlight(Map<ItemType,Set<AbstractItem>> items){
        unhighlightAll();
        items.get(ItemType.node).forEach(x->((AbstractGraphItem)x).highlight());
        items.get(ItemType.link).forEach(x->((AbstractGraphItem)x).highlight());
        items.get(ItemType.actuator).forEach(x->((AbstractGraphItem)x).highlight());
        items.get(ItemType.sensor).forEach(x->((AbstractGraphItem)x).highlight());
        items.get(ItemType.subnetwork).forEach(x->((AbstractGraphItem)x).highlight());
    }

    /////////////////////////////////////////////////
    // focusing the graph
    /////////////////////////////////////////////////

    public void focusGraphOnSelection(){
        System.out.println("COMMENTED: focusGraphOnSelection");

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

//    public void reset_link_color(){
//        for(BaseLink drawLink : graphContainer.get_graph().drawlinks.values())
//            for (LaneGroup drawLanegroup : drawLink.draw_lanegroups)
//                drawLanegroup.unhighlight();
//    }
//
//    public void draw_link_state(AnimationInfo info,AbstractColormap colormap){
//
//        if(graphContainer==null)
//            return;
//        if(graphContainer.get_graph()==null)
//            return;
//
//        for(BaseLink drawLink : graphContainer.get_graph().drawlinks.values()) {
//            AbstractLinkInfo linkInfo = info.link_info.get(drawLink.id);
//            for (LaneGroup drawLanegroup : drawLink.draw_lanegroups) {
//                drawLanegroup.draw_state(linkInfo.lanegroup_info.get(drawLanegroup.id), colormap);
//            }
//        }
//
//    }

}

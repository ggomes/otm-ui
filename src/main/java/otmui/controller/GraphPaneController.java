package otmui.controller;

import error.OTMException;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
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

        scene.addEventFilter(DoRemoveItem.REMOVE_ITEM, e->remove_item(e.item));
        scene.addEventFilter(DoAddItem.ADD_ITEM, e->add_item(e.item));

        // parameters
        scene.addEventFilter(ParameterChange.SET_LINK_OFFSET, e->paintLinkShapes());
        scene.addEventFilter(ParameterChange.SET_LINK_WIDTH, e->paintLinkShapes());
        scene.addEventFilter(ParameterChange.SET_LINK_COLOR_SCHEME, e->paintLinkColors());

        scene.addEventFilter(ParameterChange.SET_MAX_DENSITY, e->set_max_density());
        scene.addEventFilter(ParameterChange.VIEW_NODES, e->set_node_visible());
        scene.addEventFilter(ParameterChange.VIEW_ACTUATORS, e->set_actuator_visible());
        scene.addEventFilter(ParameterChange.SET_NODE_SIZES, e->set_node_sizes());

////        scene.addEventFilter(ParameterChange.DRAWNODECOLORS,                e->paintNodeColors(e.itempool.items.get(ItemType.node).values(),e.params));
//        scene.addEventFilter(ParameterChange.DRAWACTUATORS, e->paintActuators(e.itempool.items.get(ItemType.actuator).values(),e.params));

    }

    public void loadScenario(Data data) {
        graphContainer.set_items(data);
    }

    public static void attach_mouse_click_handler(AbstractItem item){
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

    /////////////////////////////////////////////////
    // drawing
    /////////////////////////////////////////////////

    public void paintLinkShapes(){
        float offset = myApp.params.link_offset();
        float width = myApp.params.lane_width_meters();
        GlobalParameters.RoadColorScheme color = myApp.params.road_color_scheme();
        myApp.data.items.get(ItemType.link).values().forEach(x -> ((otmui.item.Link)x).paintShape(offset,width,color));
    }

    public void paintLinkColors() {
        GlobalParameters.RoadColorScheme new_color_map = (GlobalParameters.RoadColorScheme) myApp.params.road_color_scheme.getValue();
        myApp.data.items.get(ItemType.link).values().forEach(x -> ((Link)x).paintColor(new_color_map));
    }


//    public void set_link_width() {
//        float width = myApp.params.lane_width_meters();
//        myApp.data.items.get(ItemType.link).values().forEach(x -> ((otmui.item.Link)x).set_lane_width(width));
//    }
//
//    public void set_link_offset() {
//        float offset = myApp.params.link_offset();
//        myApp.data.items.get(ItemType.link).values().forEach(x -> ((otmui.item.Link)x).set_offset(offset));
//    }

    public void set_max_density() {
        float max_density = myApp.params.max_density_vpkpl();
        myApp.data.items.get(ItemType.link).values().forEach(x -> ((otmui.item.Link)x).set_max_density(max_density));
    }
//
//    public void set_link_color() {
//        GlobalParameters.RoadColorScheme color_scheme = (GlobalParameters.RoadColorScheme) myApp.params.road_color_scheme.getValue();
//        myApp.data.items.get(ItemType.link).values().forEach(x -> ((otmui.item.Link)x).set_link_color(color_scheme));
//    }

    public void set_node_visible() {
        myApp.data.items.get(ItemType.node).values().forEach(x->((AbstractGraphItem)x).set_visible(myApp.params.view_nodes()));
    }

    public void set_actuator_visible() {
        myApp.data.items.get(ItemType.actuator).values().forEach(x->((AbstractGraphItem)x).set_visible(myApp.params.view_actuators()));
    }

    public void set_node_sizes() {
        myApp.data.items.get(ItemType.node).values().forEach(x->((AbstractGraphItem)x).set_size(myApp.params.node_size()));
    }

    /////////////////////////////////////////////////
    // modifiication
    /////////////////////////////////////////////////

    private void remove_item(AbstractItem item){
        this.graphContainer.pane.getChildren().removeAll(((AbstractGraphItem)item).shapegroup);
    }

    private void add_item(AbstractItem item){
        this.graphContainer.pane.getChildren().addAll(((AbstractGraphItem)item).shapegroup);
//        Comparator<javafx.scene.Node> comparator = Comparator.naturalOrder(); // Comparator.comparingInt(javafx.scene.Node::get);
//        FXCollections.sort(graphContainer.pane.getChildren(), comparator);
    }

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

    public void unhighlight(Map<ItemType,Set<AbstractItem>> items){
        items.get(ItemType.node).forEach(x->((AbstractGraphItem)x).unhighlight());
        items.get(ItemType.link).forEach(x->((AbstractGraphItem)x).unhighlight());
        items.get(ItemType.actuator).forEach(x->((AbstractGraphItem)x).unhighlight());
        items.get(ItemType.sensor).forEach(x->((AbstractGraphItem)x).unhighlight());
        items.get(ItemType.subnetwork).forEach(x->((AbstractGraphItem)x).unhighlight());
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

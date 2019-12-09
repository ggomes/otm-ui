package otmui.graph;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import otmui.controller.GraphPaneController;

public class GraphContainer {

    private Graph graph;

    // containers from highest to lowest
    public ZoomableScrollPane scrollPane;
    public Group canvas;
    public Pane pane;

    public GraphContainer(GraphPaneController cntrl) {
        pane = new Pane();
        canvas = new Group();
        canvas.getChildren().add(pane);
        scrollPane = new ZoomableScrollPane(canvas);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        build_context_menu(cntrl);
    }

    private void build_context_menu(GraphPaneController cntrl){
        ContextMenu contextMenu = new ContextMenu();

//        MenuItem menuItem = new MenuItem("Menu Item");

        Menu tools_menu = new Menu("Tools");
        MenuItem merge_nodes = new MenuItem("Merge nodes");
        merge_nodes.setOnAction(e->cntrl.merge_nodes());

        MenuItem merge_links = new MenuItem("Merge links");
        merge_links.setOnAction(e->cntrl.merge_links());
        tools_menu.getItems().addAll(merge_nodes, merge_links);

//        CheckMenuItem checkMenuItem = new CheckMenuItem("Check Menu Item");
//        checkMenuItem.setSelected(true);
//
//        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
//
//        RadioMenuItem radioMenuItem1 = new RadioMenuItem("Radio - Option 1");
//        RadioMenuItem radioMenuItem2 = new RadioMenuItem("Radio - Option 2");
//        ToggleGroup group = new ToggleGroup();
//
//        radioMenuItem1.setToggleGroup(group);
//        radioMenuItem2.setToggleGroup(group);

        // Add MenuItem to ContextMenu
        contextMenu.getItems().add(tools_menu);

        // When user right-click on Circle
        canvas.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                contextMenu.show(canvas, event.getScreenX(), event.getScreenY());
            }
        });
    }

    public double getScale() {
        return scrollPane.getScaleValue();
    }

    public void set_graph(Graph graph){
        this.graph = graph;
        pane.getChildren().clear();
        pane.getChildren().addAll(graph.getLinks());
        pane.getChildren().addAll(graph.getNodes());
        pane.getChildren().addAll(graph.getSensors());
        pane.getChildren().addAll(graph.getActuators());
        scrollPane.zoomToFit(graph);
    }

    public Graph get_graph(){
        return graph;
    }

}

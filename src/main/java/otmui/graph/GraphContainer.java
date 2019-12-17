package otmui.graph;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Pane;
import otmui.Data;
import otmui.controller.GraphPaneController;

public class GraphContainer {

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

        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(e->cntrl.myApp.selectionManager.delete_selected_items());

        MenuItem merge_nodes = new MenuItem("Merge nodes");
        merge_nodes.setOnAction(e->cntrl.myApp.selectionManager.merge_selected_nodes());

        MenuItem merge_links = new MenuItem("Merge links");
        merge_links.setOnAction(e->cntrl.myApp.selectionManager.merge_selected_links());

        contextMenu.getItems().addAll(delete,merge_nodes, merge_links);

        // set right click
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

    public void set_items(Data data){
        pane.getChildren().clear();
        pane.getChildren().addAll(data.getShapes());
        scrollPane.zoomToFit(data);
    }

}

/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.graph;

import javafx.scene.Group;
import javafx.scene.layout.Pane;

public class GraphContainer {

    private Graph graph;

    // containers from highest to lowest
    public ZoomableScrollPane scrollPane;
    public Group canvas;
    public Pane pane;

    public MouseGestures mouseGestures;

    public GraphContainer() {

        pane = new Pane();

        canvas = new Group();
        canvas.getChildren().add(pane);

        scrollPane = new ZoomableScrollPane(canvas);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        mouseGestures = new MouseGestures(this);
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
        scrollPane.zoomToFit(graph);
    }

    public Graph get_graph(){
        return graph;
    }
}

/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.graph;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class MouseGestures {

    public GraphContainer graphContainer;
    final DragContext dragContext = new DragContext();

    public MouseGestures(GraphContainer graphContainer) {
        this.graphContainer = graphContainer;
    }

    public void makeDraggable( final Node node) {
        node.setOnMousePressed(onMousePressedEventHandler);
        node.setOnMouseDragged(onMouseDraggedEventHandler);
    }

    EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
        Node node = (Node) event.getSource();
        double scale = graphContainer.getScale();
        dragContext.x = node.getBoundsInParent().getMinX() * scale - event.getScreenX();
        dragContext.y = node.getBoundsInParent().getMinY()  * scale - event.getScreenY();
        }
    };

    EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            Node node = (Node) event.getSource();

            double offsetX = event.getScreenX() + dragContext.x;
            double offsetY = event.getScreenY() + dragContext.y;

            // adjust the offset in case we are zoomed
            double scale = graphContainer.getScale();

            offsetX /= scale;
            offsetY /= scale;

            node.relocate(offsetX, offsetY);
        }
    };

//    EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
//
//        @Override
//        public void handle(MouseEvent event) {
//
//        }
//    };

    class DragContext {
        double x;
        double y;
    }

}
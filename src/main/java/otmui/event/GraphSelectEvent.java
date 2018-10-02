/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */

package otmui.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;

public class GraphSelectEvent extends Event {

    public MouseEvent event;

    public static final EventType<GraphSelectEvent> GRAPH_ANY = new EventType<>(Event.ANY, "GRAPH_ANY");

    public static final EventType<GraphSelectEvent> CLICK1_NODE = new EventType<>(GRAPH_ANY, "CLICK1_NODE");
    public static final EventType<GraphSelectEvent> CLICK1_LINK = new EventType<>(GRAPH_ANY, "CLICK1_LINK");
    public static final EventType<GraphSelectEvent> CLICK1_SENSOR = new EventType<>(GRAPH_ANY, "CLICK1_SENSOR");

    public static final EventType<GraphSelectEvent> CLICK2_NODE = new EventType<>(GRAPH_ANY, "CLICK2_NODE");
    public static final EventType<GraphSelectEvent> CLICK2_LINK = new EventType<>(GRAPH_ANY, "CLICK2_LINK");
    public static final EventType<GraphSelectEvent> CLICK2_SENSOR = new EventType<>(GRAPH_ANY, "CLICK2_SENSOR");

    public GraphSelectEvent(EventType<GraphSelectEvent> type, MouseEvent event) {
        super(type);
        this.event = event;
    }

    @Override
    public GraphSelectEvent copyFor(Object newSource, EventTarget newTarget) {
        return (GraphSelectEvent) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends GraphSelectEvent> getEventType() {
        return (EventType<? extends GraphSelectEvent>) super.getEventType();
    }

    public Object getSelected(){
        return event.getSource();
    }

}

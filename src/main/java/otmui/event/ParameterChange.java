package otmui.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import otmui.GlobalParameters;
import otmui.Data;

public class ParameterChange extends Event {

    public static final EventType<ParameterChange> SIMULATION = new EventType<>(Event.ANY, "Simulation");
    public static final EventType<ParameterChange> SET_LINK_WIDTH = new EventType<>(Event.ANY, "Set link width");
    public static final EventType<ParameterChange> SET_LINK_COLOR_SCHEME = new EventType<>(Event.ANY, "Set link colors");
    public static final EventType<ParameterChange> SET_MAX_DENSITY = new EventType<>(Event.ANY, "Set max density");
    public static final EventType<ParameterChange> VIEW_NODES = new EventType<>(Event.ANY, "View nodes");
    public static final EventType<ParameterChange> VIEW_ACTUATORS = new EventType<>(Event.ANY, "View actuators");
    public static final EventType<ParameterChange> SET_NODE_SIZES = new EventType<>(Event.ANY, "Node size");
    public static final EventType<ParameterChange> SET_LINK_OFFSET = new EventType<>(Event.ANY, "Set link offset");

    public ParameterChange(EventType<? extends Event> eventType) {
        super(eventType);
    }

    @Override
    public ParameterChange copyFor(Object newSource, EventTarget newTarget) {
        return (ParameterChange) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends ParameterChange> getEventType() {
        return (EventType<? extends ParameterChange>) super.getEventType();
    }

}

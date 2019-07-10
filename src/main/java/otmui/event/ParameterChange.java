package otmui.event;

import javafx.event.Event;
import javafx.event.EventType;

public class ParameterChange extends Event {

    public static final EventType<ParameterChange> SIMULATION = new EventType<>(Event.ANY, "Simulation");
    public static final EventType<ParameterChange> UPDATEMODELS = new EventType<>(Event.ANY, "Update model data and graph");
    public static final EventType<ParameterChange> DRAWLINKS = new EventType<>(Event.ANY, "Draw links");
    public static final EventType<ParameterChange> DRAWNODES = new EventType<>(Event.ANY, "Draw nodes");
    public static final EventType<ParameterChange> DRAWACTUATORS = new EventType<>(Event.ANY, "Draw actuators");

    public ParameterChange(EventType<? extends Event> eventType) {
        super(eventType);
    }
}

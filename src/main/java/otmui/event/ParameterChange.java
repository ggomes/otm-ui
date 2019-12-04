package otmui.event;

import javafx.event.Event;
import javafx.event.EventType;

public class ParameterChange extends Event {

    public static final EventType<ParameterChange> SIMULATION = new EventType<>(Event.ANY, "Simulation");
    public static final EventType<ParameterChange> UPDATEMODELS = new EventType<>(Event.ANY, "Update model data and graph");
    public static final EventType<ParameterChange> DRAWLINKSHAPES = new EventType<>(Event.ANY, "Draw link shapes");
    public static final EventType<ParameterChange> DRAWLINKCOLORS = new EventType<>(Event.ANY, "Draw link colors");
    public static final EventType<ParameterChange> DRAWNODESHAPES = new EventType<>(Event.ANY, "Draw node shapes");
    public static final EventType<ParameterChange> DRAWNODECOLORS = new EventType<>(Event.ANY, "Draw node colors");
    public static final EventType<ParameterChange> DRAWACTUATORS = new EventType<>(Event.ANY, "Draw actuators");

    public ParameterChange(EventType<? extends Event> eventType) {
        super(eventType);
    }
}

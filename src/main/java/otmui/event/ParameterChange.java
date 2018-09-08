package otmui.event;

import javafx.event.Event;
import javafx.event.EventType;

public class ParameterChange extends Event {

    public static final EventType<ParameterChange> SIMULATION = new EventType<>(Event.ANY, "Simulation");
    public static final EventType<ParameterChange> DISPLAY = new EventType<>(Event.ANY, "Display");

//    public ParameterChange(){
//        super(CHANGED);
//    }

    public ParameterChange(EventType<? extends Event> eventType) {
        super(eventType);
    }

}

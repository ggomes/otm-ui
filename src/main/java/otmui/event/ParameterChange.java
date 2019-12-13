package otmui.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import otmui.GlobalParameters;
import otmui.Data;

public class ParameterChange extends Event {

    public Data itempool;
    public GlobalParameters params;

    public static final EventType<ParameterChange> SIMULATION = new EventType<>(Event.ANY, "Simulation");
    public static final EventType<ParameterChange> UPDATEMODELS = new EventType<>(Event.ANY, "Update model data and graph");
    public static final EventType<ParameterChange> DRAWLINKSHAPES = new EventType<>(Event.ANY, "Draw link shapes");
    public static final EventType<ParameterChange> DRAWLINKCOLORS = new EventType<>(Event.ANY, "Draw link colors");
    public static final EventType<ParameterChange> DRAWNODESHAPES = new EventType<>(Event.ANY, "Draw node shapes");
    public static final EventType<ParameterChange> DRAWNODECOLORS = new EventType<>(Event.ANY, "Draw node colors");
    public static final EventType<ParameterChange> DRAWACTUATORS = new EventType<>(Event.ANY, "Draw actuators");

    public ParameterChange(EventType<? extends Event> eventType, Data itempool, GlobalParameters params) {
        super(eventType);
        this.itempool = itempool;
        this.params = params;
    }


    @Override
    public NewScenarioEvent copyFor(Object newSource, EventTarget newTarget) {
        return (NewScenarioEvent) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends NewScenarioEvent> getEventType() {
        return (EventType<? extends NewScenarioEvent>) super.getEventType();
    }

}

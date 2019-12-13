package otmui.event;

import api.OTMdev;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import otmui.Data;

public class NewScenarioEvent extends Event {

    public OTMdev otm;
    public Data data;

    public static final EventType<NewScenarioEvent> SCENARIO_LOADED = new EventType<>(Event.ANY, "SCENARIO_LOADED");

    public NewScenarioEvent(OTMdev otm, Data data) {
        super(SCENARIO_LOADED);
        this.otm = otm;
        this.data = data;
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

package otmui.event;

import otmui.model.Scenario;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class NewScenarioEvent extends Event {

    public Scenario scenario;

    public static final EventType<NewScenarioEvent> SCENARIO_LOADED = new EventType<>(Event.ANY, "SCENARIO_LOADED");

    public NewScenarioEvent(Scenario scenario) {
        super(SCENARIO_LOADED);
        this.scenario = scenario;
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

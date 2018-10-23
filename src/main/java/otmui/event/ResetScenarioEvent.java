package otmui.event;

import javafx.event.EventTarget;
import javafx.event.EventType;

import javafx.event.Event;

public class ResetScenarioEvent extends Event {

    public static final EventType<ResetScenarioEvent> SCENARIO_RESET = new EventType<ResetScenarioEvent>(javafx.event.Event.ANY, "SCENARIO_RESET");

    public ResetScenarioEvent() {
        super(SCENARIO_RESET);
    }

    @Override
    public ResetScenarioEvent copyFor(Object newSource, EventTarget newTarget) {
        return (ResetScenarioEvent) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends ResetScenarioEvent> getEventType() {
        return (EventType<? extends ResetScenarioEvent>) super.getEventType();
    }

}

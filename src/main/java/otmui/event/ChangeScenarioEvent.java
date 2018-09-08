package otmui.event;

import otmui.model.Scenario;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class ChangeScenarioEvent extends Event {

    public Scenario scenario;

    public static final EventType<ChangeScenarioEvent> LOADED =
            new EventType<>(Event.ANY, "ScenarioLoaded");

    public static final EventType<ChangeScenarioEvent> ANY = LOADED;

    public ChangeScenarioEvent(Scenario scenario) {
        super(LOADED);
        this.scenario = scenario;
    }

    @Override
    public ChangeScenarioEvent copyFor(Object newSource, EventTarget newTarget) {
        return (ChangeScenarioEvent) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends ChangeScenarioEvent> getEventType() {
        return (EventType<? extends ChangeScenarioEvent>) super.getEventType();
    }

}

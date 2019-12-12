package otmui.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class NewElementEvent extends Event {

    public Object item;

    public static final EventType<NewElementEvent> NEW_NODE = new EventType<>(Event.ANY, "NEW_NODE");

    public NewElementEvent(EventType<? extends Event> eventType,Object item) {
        super(eventType);
        this.item = item;
    }

    @Override
    public NewElementEvent copyFor(Object newSource, EventTarget newTarget) {
        return (NewElementEvent) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends NewElementEvent> getEventType() {
        return (EventType<? extends NewElementEvent>) super.getEventType();
    }

}

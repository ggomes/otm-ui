package otmui.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class DeleteElementEvent extends Event {

    public Object item;

    public static final EventType<DeleteElementEvent> REMOVE_LINK = new EventType<>(Event.ANY, "REMOVE_LINK");
    public static final EventType<DeleteElementEvent> REMOVE_NODE = new EventType<>(Event.ANY, "REMOVE_NODE");
    public static final EventType<DeleteElementEvent> REMOVE_ACTUATOR = new EventType<>(Event.ANY, "REMOVE_ACTUATOR");

    public DeleteElementEvent(EventType<? extends Event> eventType,Object item) {
        super(eventType);
        this.item = item;
    }

    @Override
    public DeleteElementEvent copyFor(Object newSource, EventTarget newTarget) {
        return (DeleteElementEvent) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends DeleteElementEvent> getEventType() {
        return (EventType<? extends DeleteElementEvent>) super.getEventType();
    }

}

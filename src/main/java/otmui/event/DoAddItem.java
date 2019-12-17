package otmui.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import otmui.item.AbstractItem;

public class DoAddItem extends Event {

    public AbstractItem item;

    public static final EventType<DoAddItem> ADD_ITEM = new EventType<>(Event.ANY, "ADD_ITEM");

    public DoAddItem(EventType<? extends Event> eventType, AbstractItem item) {
        super(eventType);
        this.item = item;
    }

    @Override
    public DoAddItem copyFor(Object newSource, EventTarget newTarget) {
        return (DoAddItem) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends DoAddItem> getEventType() {
        return (EventType<? extends DoAddItem>) super.getEventType();
    }

}

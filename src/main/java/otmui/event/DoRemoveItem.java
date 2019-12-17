package otmui.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import otmui.item.AbstractItem;

public class DoRemoveItem extends Event {

    public AbstractItem item;

    public static final EventType<DoRemoveItem> REMOVE_ITEM = new EventType<>(Event.ANY, "REMOVE_ITEM");

    public DoRemoveItem(EventType<? extends Event> eventType, AbstractItem item) {
        super(eventType);
        this.item = item;
    }

    @Override
    public DoRemoveItem copyFor(Object newSource, EventTarget newTarget) {
        return (DoRemoveItem) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends DoRemoveItem> getEventType() {
        return (EventType<? extends DoRemoveItem>) super.getEventType();
    }

}

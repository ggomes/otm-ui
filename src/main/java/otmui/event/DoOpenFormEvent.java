package otmui.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import otmui.item.AbstractItem;

public class DoOpenFormEvent extends Event {

    public AbstractItem item;

    public static final EventType<DoOpenFormEvent> OPEN = new EventType<>(Event.ANY, "OPEN");

    public DoOpenFormEvent(EventType<DoOpenFormEvent> type, AbstractItem item) {
        super(type);
        this.item = item;
    }

    @Override
    public DoOpenFormEvent copyFor(Object newSource, EventTarget newTarget) {
        return (DoOpenFormEvent) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends DoOpenFormEvent> getEventType() {
        return (EventType<? extends DoOpenFormEvent>) super.getEventType();
    }


}

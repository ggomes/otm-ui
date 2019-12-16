package otmui.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import otmui.ItemType;
import otmui.item.AbstractItem;

public class FormSelectEvent extends Event {

    public AbstractItem item;
    public ItemType itemType;
    public long itemid;

    public static final EventType<FormSelectEvent> FORM_ANY = new EventType<>(Event.ANY, "FORM_ANY");
    public static final EventType<FormSelectEvent> CLICK1 = new EventType<>(FORM_ANY, "FORM CLICK1");
    public static final EventType<FormSelectEvent> CLICK2 = new EventType<>(FORM_ANY, "FORM CLICK2");

    public FormSelectEvent(EventType<FormSelectEvent> type, ItemType itemType, long itemid) {
        super(type);
        this.itemType = itemType;
        this.itemid = itemid;
    }


    public FormSelectEvent(EventType<FormSelectEvent> type, AbstractItem item) {
        super(type);
        this.item = item;
    }

    @Override
    public FormSelectEvent copyFor(Object newSource, EventTarget newTarget) {
        return (FormSelectEvent) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends FormSelectEvent> getEventType() {
        return (EventType<? extends FormSelectEvent>) super.getEventType();
    }

}

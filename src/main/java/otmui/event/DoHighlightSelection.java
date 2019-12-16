package otmui.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import otmui.ItemType;
import otmui.item.AbstractItem;

import java.util.Map;
import java.util.Set;

public class DoHighlightSelection extends Event {

    public Map<ItemType, Set<AbstractItem>> selection;

    public static final EventType<DoHighlightSelection> HIGHLIGHT_ANY = new EventType<>(Event.ANY, "HIGHLIGHT_ANY");

    public DoHighlightSelection(EventType<DoHighlightSelection> type, Map<ItemType, Set<AbstractItem>> selection) {
        super(type);
        this.selection = selection;
    }

    @Override
    public DoHighlightSelection copyFor(Object newSource, EventTarget newTarget) {
        return (DoHighlightSelection) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends DoHighlightSelection> getEventType() {
        return (EventType<? extends DoHighlightSelection>) super.getEventType();
    }

}

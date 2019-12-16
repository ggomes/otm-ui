package otmui.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;
import otmui.item.AbstractGraphItem;
import otmui.item.AbstractItem;

public class GraphClickEvent extends Event {

    public AbstractGraphItem item;
    public MouseEvent event;

    public static final EventType<GraphClickEvent> GRAPH_ANY = new EventType<>(Event.ANY, "GRAPH_ANY");
    public static final EventType<GraphClickEvent> CLICK1 = new EventType<>(GRAPH_ANY, "CLICK1");
    public static final EventType<GraphClickEvent> CLICK2 = new EventType<>(GRAPH_ANY, "CLICK2");

    public GraphClickEvent(EventType<GraphClickEvent> type, AbstractGraphItem item, MouseEvent event) {
        super(type);
        this.event = event;
        this.item = item;
    }

    @Override
    public GraphClickEvent copyFor(Object newSource, EventTarget newTarget) {
        return (GraphClickEvent) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends GraphClickEvent> getEventType() {
        return (EventType<? extends GraphClickEvent>) super.getEventType();
    }

    public Object getSelected(){
        return event.getSource();
    }

}

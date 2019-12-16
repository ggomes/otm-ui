package otmui.event;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.control.TreeItem;

import javafx.scene.input.MouseEvent;

public class TreeClickEvent extends Event {

    public MouseEvent event;
    public ObservableList<TreeItem> items; // for click1
    public TreeItem item;       // for click2

    public static final EventType<TreeClickEvent> TREE_ANY = new EventType<>(Event.ANY, "TREE_ANY");
    public static final EventType<TreeClickEvent> CLICK1 = new EventType<>(TREE_ANY, "CLICK1");
    public static final EventType<TreeClickEvent> CLICK2 = new EventType<>(TREE_ANY, "CLICK2");

    public TreeClickEvent(EventType<TreeClickEvent> type, ObservableList<TreeItem> items, TreeItem item, MouseEvent event) {
        super(type);
        this.items = items;
        this.item = item;
        this.event = event;
    }

    @Override
    public TreeClickEvent copyFor(Object newSource, EventTarget newTarget) {
        return (TreeClickEvent) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends TreeClickEvent> getEventType() {
        return (EventType<? extends TreeClickEvent>) super.getEventType();
    }

}

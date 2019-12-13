package otmui.event;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.control.TreeItem;

import javafx.scene.input.MouseEvent;

public class TreeSingleClickEvent extends Event {

    public ObservableList<TreeItem> items;
    public MouseEvent event;

    public static final EventType<TreeSingleClickEvent> TREE_SINGLE = new EventType<>(Event.ANY, "TREE_SINGLE");
//    public static final EventType<TreeSingleClickEvent> CLICK1 = new EventType<>(TREE_SINGLE, "CLICK1");


    public TreeSingleClickEvent(EventType<TreeSingleClickEvent> type, ObservableList<TreeItem> items, MouseEvent event) {
        super(type);
        this.items = items;
        this.event = event;
    }


    @Override
    public TreeSingleClickEvent copyFor(Object newSource, EventTarget newTarget) {
        return (TreeSingleClickEvent) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends TreeSingleClickEvent> getEventType() {
        return (EventType<? extends TreeSingleClickEvent>) super.getEventType();
    }


}

package otmui.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.control.TreeItem;

import javafx.scene.input.MouseEvent;

public class TreeSelectEvent extends Event {

    public TreeItem treeitem;
    public MouseEvent event;

    public static final EventType<TreeSelectEvent> TREE_ANY = new EventType<>(Event.ANY, "TREE_ANY");
    public static final EventType<TreeSelectEvent> CLICK1 = new EventType<>(TREE_ANY, "CLICK1");

    public static final EventType<TreeSelectEvent> CLICK2_LINK = new EventType<>(TREE_ANY, "CLICK2_LINK");
    public static final EventType<TreeSelectEvent> CLICK2_COMMODITY = new EventType<>(TREE_ANY, "CLICK2_COMMODITY");
    public static final EventType<TreeSelectEvent> CLICK2_SUBNETWORK = new EventType<>(TREE_ANY, "CLICK2_SUBNETWORK");
    public static final EventType<TreeSelectEvent> CLICK2_DEMAND = new EventType<>(TREE_ANY, "CLICK2_DEMAND");
    public static final EventType<TreeSelectEvent> CLICK2_SPLIT = new EventType<>(TREE_ANY, "CLICK2_SPLIT");
    public static final EventType<TreeSelectEvent> CLICK2_ACTUATOR = new EventType<>(TREE_ANY, "CLICK2_ACTUATOR");
    public static final EventType<TreeSelectEvent> CLICK2_CONTROLLER = new EventType<>(TREE_ANY, "CLICK2_CONTROLLER");
    public static final EventType<TreeSelectEvent> CLICK2_SENSOR = new EventType<>(TREE_ANY, "CLICK2_SENSOR");

    public TreeSelectEvent(EventType<TreeSelectEvent> type, TreeItem treeitem, MouseEvent event) {
        super(type);
        this.treeitem = treeitem;
        this.event = event;
    }

    @Override
    public TreeSelectEvent copyFor(Object newSource, EventTarget newTarget) {
        return (TreeSelectEvent) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends TreeSelectEvent> getEventType() {
        return (EventType<? extends TreeSelectEvent>) super.getEventType();
    }

//    public Object getSelected(){
//        return event.getSource();
//    }

}

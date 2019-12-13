package otmui.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;

public class TreeDoubleClickEvent extends Event {

    public TreeItem item;
    public MouseEvent event;

    public static final EventType<TreeDoubleClickEvent> TREE_DOUBLE = new EventType<>(Event.ANY, "TREE_DOUBLE");
//    public static final EventType<TreeDoubleClickEvent> CLICK = new EventType<>(TREE_DOUBLE, "CLICK");

//    public static final EventType<TreeSingleClickEvent> CLICK2_LINK = new EventType<>(TREE_ANY, "CLICK2_LINK");
//    public static final EventType<TreeSingleClickEvent> CLICK2_COMMODITY = new EventType<>(TREE_ANY, "CLICK2_COMMODITY");
//    public static final EventType<TreeSingleClickEvent> CLICK2_SUBNETWORK = new EventType<>(TREE_ANY, "CLICK2_SUBNETWORK");
//    public static final EventType<TreeSingleClickEvent> CLICK2_DEMAND = new EventType<>(TREE_ANY, "CLICK2_DEMAND");
//    public static final EventType<TreeSingleClickEvent> CLICK2_SPLIT = new EventType<>(TREE_ANY, "CLICK2_SPLIT");
//    public static final EventType<TreeSingleClickEvent> CLICK2_ACTUATOR = new EventType<>(TREE_ANY, "CLICK2_ACTUATOR");
//    public static final EventType<TreeSingleClickEvent> CLICK2_CONTROLLER = new EventType<>(TREE_ANY, "CLICK2_CONTROLLER");
//    public static final EventType<TreeSingleClickEvent> CLICK2_SENSOR = new EventType<>(TREE_ANY, "CLICK2_SENSOR");


    public TreeDoubleClickEvent(EventType<TreeDoubleClickEvent> type, TreeItem item, MouseEvent event) {
        super(type);
        this.item = item;
        this.event = event;
    }


    @Override
    public TreeDoubleClickEvent copyFor(Object newSource, EventTarget newTarget) {
        return (TreeDoubleClickEvent) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends TreeDoubleClickEvent> getEventType() {
        return (EventType<? extends TreeDoubleClickEvent>) super.getEventType();
    }



}

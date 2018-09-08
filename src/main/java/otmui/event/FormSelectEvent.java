package otmui.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class FormSelectEvent extends Event {

    public Long itemId;

    public static final EventType<FormSelectEvent> FORM_ANY = new EventType<>(Event.ANY, "FORM_ANY");

    public static final EventType<FormSelectEvent> CLICK1_LINK = new EventType<>(FORM_ANY, "CLICK1_LINK");
    public static final EventType<FormSelectEvent> CLICK1_NODE= new EventType<>(FORM_ANY, "CLICK1_NODE");
    public static final EventType<FormSelectEvent> CLICK1_SUBNETWORK = new EventType<>(FORM_ANY, "CLICK1_SUBNETWORK");
    public static final EventType<FormSelectEvent> CLICK1_COMMODITY = new EventType<>(FORM_ANY, "CLICK1_COMMODITY");
    public static final EventType<FormSelectEvent> CLICK1_DEMAND = new EventType<>(FORM_ANY, "CLICK1_DEMAND");
    public static final EventType<FormSelectEvent> CLICK1_SPLIT= new EventType<>(FORM_ANY, "CLICK1_SPLIT");
    public static final EventType<FormSelectEvent> CLICK1_ACTUATOR = new EventType<>(FORM_ANY, "CLICK1_ACTUATOR");
    public static final EventType<FormSelectEvent> CLICK1_CONTROLLER = new EventType<>(FORM_ANY, "CLICK1_CONTROLLER");
    public static final EventType<FormSelectEvent> CLICK1_SENSOR = new EventType<>(FORM_ANY, "CLICK1_SENSOR");

    public static final EventType<FormSelectEvent> CLICK2_LINK = new EventType<>(FORM_ANY, "CLICK2_LINK");
    public static final EventType<FormSelectEvent> CLICK2_NODE = new EventType<>(FORM_ANY, "CLICK2_NODE");
    public static final EventType<FormSelectEvent> CLICK2_SUBNETWORK = new EventType<>(FORM_ANY, "CLICK2_SUBNETWORK");
    public static final EventType<FormSelectEvent> CLICK2_COMMODITY = new EventType<>(FORM_ANY, "CLICK2_COMMODITY");
    public static final EventType<FormSelectEvent> CLICK2_DEMAND = new EventType<>(FORM_ANY, "CLICK2_DEMAND");
    public static final EventType<FormSelectEvent> CLICK2_SPLIT = new EventType<>(FORM_ANY, "CLICK2_SPLIT");
    public static final EventType<FormSelectEvent> CLICK2_ACTUATOR = new EventType<>(FORM_ANY, "CLICK2_ACTUATOR");
    public static final EventType<FormSelectEvent> CLICK2_CONTROLLER = new EventType<>(FORM_ANY, "CLICK2_CONTROLLER");
    public static final EventType<FormSelectEvent> CLICK2_SENSOR = new EventType<>(FORM_ANY, "CLICK2_SENSOR");

    public FormSelectEvent(EventType<FormSelectEvent> type, Long itemId) {
        super(type);
        this.itemId = itemId;
    }

    @Override
    public FormSelectEvent copyFor(Object newSource, EventTarget newTarget) {
        return (FormSelectEvent) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends FormSelectEvent> getEventType() {
        return (EventType<? extends FormSelectEvent>) super.getEventType();
    }

    public Long getItemId(){
        return itemId;
    }

}

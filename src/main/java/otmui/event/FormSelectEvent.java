package otmui.event;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class FormSelectEvent extends Event {

    public Object item;

    public static final EventType<FormSelectEvent> FORM_ANY = new EventType<>(Event.ANY, "FORM_ANY");
    public static final EventType<FormSelectEvent> CLICK1 = new EventType<>(FORM_ANY, "FORM CLICK1");
    public static final EventType<FormSelectEvent> CLICK2 = new EventType<>(FORM_ANY, "FORM CLICK2");


//    public static final EventType<FormSelectEvent> CLICK2_LINK = new EventType<>(FORM_ANY, "FORM CLICK2_LINK");
//    public static final EventType<FormSelectEvent> CLICK2_NODE = new EventType<>(FORM_ANY, "FORM CLICK2_NODE");
//    public static final EventType<FormSelectEvent> CLICK2_SUBNETWORK = new EventType<>(FORM_ANY, "FORM CLICK2_SUBNETWORK");
//    public static final EventType<FormSelectEvent> CLICK2_COMMODITY = new EventType<>(FORM_ANY, "FORM CLICK2_COMMODITY");
//    public static final EventType<FormSelectEvent> CLICK2_DEMAND = new EventType<>(FORM_ANY, "FORM CLICK2_DEMAND");
//    public static final EventType<FormSelectEvent> CLICK2_SPLIT = new EventType<>(FORM_ANY, "FORM CLICK2_SPLIT");
//    public static final EventType<FormSelectEvent> CLICK2_ACTUATOR = new EventType<>(FORM_ANY, "FORM CLICK2_ACTUATOR");
//    public static final EventType<FormSelectEvent> CLICK2_CONTROLLER = new EventType<>(FORM_ANY, "FORM CLICK2_CONTROLLER");
//    public static final EventType<FormSelectEvent> CLICK2_SENSOR = new EventType<>(FORM_ANY, "FORM CLICK2_SENSOR");

    public FormSelectEvent(EventType<FormSelectEvent> type, Object item) {
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

    public Object getItem(){
        return item;
    }

}

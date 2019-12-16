package otmui.item;

import control.AbstractController;
import otmui.Data;
import otmui.ItemType;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class Controller extends AbstractItem {

    public control.AbstractController controller;
    public Set<otmui.item.Actuator> actuators;
    public Set<otmui.item.FixedSensor> sensors;

    public Controller(AbstractController controller, Data data) {
        super(controller.getId());
        this.controller = controller;
        this.actuators = controller.actuators.values().stream()
                .map(x->(otmui.item.Actuator) data.items.get(ItemType.actuator).get(x.id))
                .collect(toSet());
        this.sensors = controller.sensors.stream()
                .map(x->(otmui.item.FixedSensor) data.items.get(ItemType.sensor).get(x.id))
                .collect(toSet());
    }
    @Override
    public ItemType getType() {
        return null;
    }
}

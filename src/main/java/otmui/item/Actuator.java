package otmui.item;

import actuator.AbstractActuator;
import javafx.scene.paint.Color;
import otmui.ItemType;

public abstract class Actuator extends AbstractGraphItem {

    public AbstractActuator actuator;

    public Actuator(AbstractActuator act, float xpos, float ypos, Color color1, Color color2) {
        super(act.getId(), xpos, ypos, color1, color2);
        this.actuator = act;
    }

    @Override
    public ItemType getType() {
        return ItemType.actuator;
    }

}

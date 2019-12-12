package otmui.item;

import javafx.scene.paint.Color;

public abstract class BaseActuator extends AbstractPointItem {

    public BaseActuator(long id, float xpos, float ypos) {
        super(id,xpos,ypos);
        color1 = Color.DODGERBLUE;
        color2 = Color.RED;
    }

    @Override
    public String getPrefix() {
        return "actuator";
    }

    @Override
    public String getName() {
        return String.format("actuator %d",id);
    }
}

package otmui.item;

import control.AbstractController;
import otmui.ItemType;

public class Controller extends AbstractItem {

    public control.AbstractController controller;

    public Controller(AbstractController controller) {
        super(controller.getId());
        this.controller = controller;
    }
    @Override
    public ItemType getType() {
        return null;
    }
}

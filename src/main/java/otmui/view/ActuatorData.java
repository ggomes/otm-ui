package otmui.view;

import actuator.AbstractActuator;
import actuator.InterfaceActuatorTarget;
import otmui.Data;
import otmui.ItemType;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import otmui.item.AbstractItem;

public class ActuatorData extends AbstractData {

    public ActuatorData(otmui.item.Actuator uiactuator, Data data) {
        super();

        AbstractActuator actuator = uiactuator.actuator;
        ObservableList<Node> X = vbox.getChildren();

        // id .................
        X.add(UIFactory.createLabelText("id", actuator.getId().toString()).pane);

        // type .................
        X.add(UIFactory.createLabelText("type", actuator.getType().toString()).pane);

        // target .................
        X.add(UIFactory.createLabelButton("target",
                actuator.target.getClass().getSimpleName() + " " + actuator.target.getId(),
                e->showTargetData(actuator.target, data)
        ).pane);

        // controller .................
        if(actuator.myController!=null){
            X.add(UIFactory.createLabelButton("controller",
                    actuator.myController.getId().toString(),
                    e->click2(data.items.get(ItemType.controller).get(actuator.myController.getId()))
            ).pane);
        }

    }

    private void showTargetData(InterfaceActuatorTarget target, Data data){
        AbstractItem item = null;
        if(target instanceof common.Node)
            item = data.items.get(ItemType.node).get(target.getId());

        if(target instanceof common.Link)
            item = data.items.get(ItemType.link).get(target.getId());

        doubleClick(item);
    }

}

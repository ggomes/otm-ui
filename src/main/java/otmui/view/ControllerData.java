package otmui.view;

import control.AbstractController;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.stream.Collectors;

public class ControllerData extends AbstractData {

    public ControllerData(otmui.item.Controller uicontroller) {
        super();

        control.AbstractController controller = uicontroller.controller;
        ObservableList<Node> X = vbox.getChildren();

        // id .................
        X.add(UIFactory.createLabelText(
                "id",
                controller.getId().toString()
        ).pane);

        // Algorithm myType;
        X.add(UIFactory.createLabelText(
                "algorithm",
                controller.type.toString()
        ).pane);

        // Set<AbstractActuator> actuators;
        X.add(UIFactory.createLabelList(
                "actuators",
                controller.actuators.values().stream().map(x->String.format("%d",x.id)).collect(Collectors.toSet())
        ).pane);

    }
}

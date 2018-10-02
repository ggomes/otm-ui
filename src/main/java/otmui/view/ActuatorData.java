/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.view;

import actuator.AbstractActuator;
import actuator.InterfaceActuatorTarget;
import otmui.event.FormSelectEvent;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;

public class ActuatorData extends AbstractData {

    public ActuatorData(AbstractActuator actuator) {
        super();

        ObservableList<Node> X = vbox.getChildren();

        // id .................
        X.add(UIFactory.createLabelText(
                "id",
                actuator.getId().toString()
        ).pane);

        // type .................
        X.add(UIFactory.createLabelText(
                "type",
                actuator.getId().toString()
        ).pane);

        // target .................
//        InterfaceActuatorTarget target = ;
        X.add(UIFactory.createLabelButton(
                "target",
                actuator.target.getClass().getSimpleName() + " " + actuator.target.getId(),
                e->showTargetData(actuator.target)
        ).pane);

        // controller .................
        if(actuator.myController!=null){
            X.add(UIFactory.createLabelButton(
                    "controller",
                    actuator.myController.getId().toString(),
                    e->Event.fireEvent(scrollPane,new FormSelectEvent(FormSelectEvent.CLICK2_CONTROLLER,actuator.myController.getId()))
            ).pane);
        }

    }

    private void showTargetData(InterfaceActuatorTarget target){

        if(target instanceof common.Node)
            Event.fireEvent(scrollPane,new FormSelectEvent(FormSelectEvent.CLICK2_NODE,target.getId()));

        if(target instanceof common.Link)
            Event.fireEvent(scrollPane,new FormSelectEvent(FormSelectEvent.CLICK2_LINK,target.getId()));

    }

}

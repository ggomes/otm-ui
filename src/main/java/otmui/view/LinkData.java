/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.view;

import common.AbstractLaneGroupLongitudinal;
import otmui.event.FormSelectEvent;
import otmui.model.AddLanes;
import otmui.model.Link;
import common.AbstractLaneGroup;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LinkData extends AbstractData {

    public LinkData(Link link){
        super();

        ObservableList<Node> X = vbox.getChildren();

        // link id .................
        X.add(UIFactory.createLabelText("id",link.getId().toString()).pane);

        // start node .................
        X.add(UIFactory.createLabelButton(
                "start node",
                link.getStartNodeId().toString(),
                e->doubleClickNode(link.getStartNodeId())
        ).pane);

        // end node .................
        X.add(UIFactory.createLabelButton(
                "end node",
                link.getEndNodeId().toString(),
                e->doubleClickNode(link.getEndNodeId())
        ).pane);

        // length [LabelText] ................
        X.add(UIFactory.createLabelText(
                "length [km]",
                link.getLength().toString()
        ).pane);

        // full lanes [LabelText] ................
        X.add(UIFactory.createLabelText(
                "full lanes",
                String.format("%d",link.getLanes())
        ).pane);

        // left pocket
        AddLanes left = link.getLeftLanes();
        if(left!=null)
            X.add(UIFactory.createAddLanes("Left pocket:",left));

        // right pocket
        AddLanes right = link.getRightLanes();
        if(right!=null)
            X.add(UIFactory.createAddLanes("Right pocket:",right));

        // hov lanes
        // DO THIS!!!

        // hov gates
        // DO THIS!!!

        // link model type
        X.add(UIFactory.createLabelLabel("link type",link.getModelType().toString()).pane);

        // lanegroups
        Collection<AbstractLaneGroupLongitudinal> lanegroups = link.getLongLanegroups();
        List<String> lanegroupIds = lanegroups.stream().map(x->String.format("%d",x.id)).collect(Collectors.toList());
        X.add(UIFactory.createLabelCombobox("lanegroups",lanegroupIds).pane);

        // is sink
        X.add(UIFactory.createLabelCheckbox("is sink",link.isSink()).pane);

        // is source
        X.add(UIFactory.createLabelCheckbox("is source",link.isSource()).pane);

        // demandsForLink
        if(link.demandsForLink !=null)
            X.add(UIFactory.createLabelButton(
                    "demands",
                    "",
                    e->Event.fireEvent(this.scrollPane,new FormSelectEvent(FormSelectEvent.CLICK2_DEMAND,link.demandsForLink.get_link_id()))
            ).pane);

        // actuator
        if(link.actuator!=null) {
            String actuatorId = String.format("%d",link.actuator.getId());
            X.add(UIFactory.createLabelText("actuator", actuatorId).pane);
        }

    }

    private void doubleClickNode(long id){
        Event.fireEvent(this.scrollPane,new FormSelectEvent(FormSelectEvent.CLICK1_NODE,id));
        Event.fireEvent(this.scrollPane,new FormSelectEvent(FormSelectEvent.CLICK2_NODE,id));
    }
}

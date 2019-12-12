package otmui.view;

import api.OTMdev;
import models.BaseLaneGroup;
import otmui.event.FormSelectEvent;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LinkData extends AbstractData {

    public LinkData(common.Link link, OTMdev otm){
        super();

        ObservableList<Node> X = vbox.getChildren();

        // link id .................
        X.add(UIFactory.createLabelText("id",link.getId().toString()).pane);

        // start node .................
        X.add(UIFactory.createLabelButton(
                "start node",
                link.start_node.getId().toString(),
                e->doubleClickNode(link.start_node)
        ).pane);

        // end node .................
        X.add(UIFactory.createLabelButton(
                "end node",
                link.end_node.getId().toString(),
                e->doubleClickNode(link.end_node)
        ).pane);

        // length [LabelText] ................
        X.add(UIFactory.createLabelText(
                "length [km]",
                String.format("%.0f",link.length)
        ).pane);

        // full lanes [LabelText] ................
        X.add(UIFactory.createLabelText(
                "full lanes",
                String.format("%d",link.full_lanes)
        ).pane);

//        // left pocket
//        AddLanes left = link.getLeftLanes();
//        if(left!=null)
//            X.add(UIFactory.createAddLanes("Left pocket:",left));
//
//        // right pocket
//        AddLanes right = link.getRightLanes();
//        if(right!=null)
//            X.add(UIFactory.createAddLanes("Right pocket:",right));

        // hov lanes
        // DO THIS!!!

        // hov gates
        // DO THIS!!!

        // link model type
        X.add(UIFactory.createLabelLabel("link type",link.model.name).pane);

        // lanegroups
        Collection<BaseLaneGroup> lanegroups = link.lanegroups_flwdn.values();
        List<String> lanegroupIds = lanegroups.stream().map(x->String.format("%d",x.id)).collect(Collectors.toList());
        X.add(UIFactory.createLabelCombobox("lanegroups",lanegroupIds).pane);

        // is sink
        X.add(UIFactory.createLabelCheckbox("is sink",link.is_sink).pane);

        // is source
        X.add(UIFactory.createLabelCheckbox("is source",link.is_source).pane);

//        // demandsForLink
//        if(link.demandsForLink !=null)
//            X.add(UIFactory.createLabelButton(
//                    "demands",
//                    "",
//                    e->Event.fireEvent(this.scrollPane,new FormSelectEvent(FormSelectEvent.CLICK2_DEMAND,link.demandsForLink.get_link_id()))
//            ).pane);
//
//        // actuator
//        if(link.actuator!=null) {
//            String actuatorId = String.format("%d",link.actuator.getId());
//            X.add(UIFactory.createLabelText("actuator", actuatorId).pane);
//        }

    }

    private void doubleClickNode(common.Node node){
        Event.fireEvent(this.scrollPane,new FormSelectEvent(FormSelectEvent.CLICK1,node));
        Event.fireEvent(this.scrollPane,new FormSelectEvent(FormSelectEvent.CLICK2_NODE,node));
    }

}

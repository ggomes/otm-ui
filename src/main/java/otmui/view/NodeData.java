package otmui.view;

import api.OTMdev;
import otmui.event.FormSelectEvent;
import otmui.model.Node;
import otmui.model.Scenario;
import javafx.collections.ObservableList;
import javafx.event.Event;

import java.util.stream.Collectors;

public class NodeData extends AbstractData {

    public NodeData(common.Node node, OTMdev otm){
        super();

        ObservableList<javafx.scene.Node> X = vbox.getChildren();

        // node id ....................
        X.add(UIFactory.createLabelText("id", String.format("%d",node.getId())).pane);

        // input links .................
        X.add(UIFactory.createLabelList(
                "input links",
                node.in_links.keySet().stream()
                        .map(x->x.toString())
                        .collect(Collectors.toList())).pane);

        // output links ................
        X.add(UIFactory.createLabelList(
                "output links",
                node.out_links.keySet().stream()
                        .map(x->x.toString())
                        .collect(Collectors.toList())
        ).pane);

//        // splitsForNode .....................
//        if(node.splitsForNode !=null)
//            X.add(UIFactory.createLabelButton(
//                    "splitsForNode",
//                    "",
//                    e-> Event.fireEvent(scrollPane,new FormSelectEvent(FormSelectEvent.CLICK2_SPLIT,node.splitsForNode.getId()))
//            ).pane);

        // actuator ...................
        if(node.actuator!=null)
            X.add(UIFactory.createLabelButton(
                    "actuator",
                    String.format("%d",node.actuator.getId()),
                    e-> Event.fireEvent(scrollPane,new FormSelectEvent(FormSelectEvent.CLICK2_ACTUATOR,node.actuator.getId()))
            ).pane);

    }

}

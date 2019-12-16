package otmui.view;

import otmui.ItemType;
import otmui.event.FormSelectEvent;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;

public class CommodityData extends AbstractData {

    public CommodityData(otmui.item.Commodity uicommodity) {
        super();

        commodity.Commodity commodity = uicommodity.comm;
        ObservableList<Node> X = vbox.getChildren();

        // id .................
        X.add(UIFactory.createLabelText("id", commodity.getId().toString()).pane);

        // String name;
        X.add(UIFactory.createLabelText("name", commodity.get_name()).pane);

        // subnetwork
//        List<Long> subnets = commodity.get_subnetwork_ids();
//        if(subnets.size()==1) {
//            Subnetwork subnetwork = otm.scenario.subnetworks.get(subnets.get(0));
//            X.add(UIFactory.createLabelButton("subnetwork",
//                    subnetwork.getId().toString(),
//                    e -> DoubleClickSubnetwork(subnetwork.getId())
//            ).pane);
//        } else {
//            System.err.println("WARNING: Loaded a commodity with not a unique subnetwork. Please check UI code.");
//        }

    }

}

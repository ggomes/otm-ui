package otmui.view;

import api.OTMdev;
import otmui.model.Scenario;
import commodity.Subnetwork;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.stream.Collectors;

public class SubnetworkData extends AbstractData {

    public SubnetworkData(Subnetwork subnetwork, OTMdev otm) {
        super();

        ObservableList<Node> X = vbox.getChildren();

        // id .................
        X.add(UIFactory.createLabelText("id",subnetwork.getId().toString()).pane);

        // name .................
        X.add(UIFactory.createLabelText("name",subnetwork.getName()).pane);

        // is_global .................
        X.add(UIFactory.createLabelCheckbox("is global",subnetwork.isGlobal()).pane);

        // Set<Link> links .................
        X.add(UIFactory.createLabelList("link",
                subnetwork.get_link_ids().stream().map(x->x.toString()).collect(Collectors.toSet())
                ).pane);

        // Set<Commodity> used_by_comm .................
        X.add(UIFactory.createLabelList("vehicle types",
                subnetwork.get_commodity_ids().stream().map(x->x.toString()).collect(Collectors.toSet())
        ).pane);


    }
}

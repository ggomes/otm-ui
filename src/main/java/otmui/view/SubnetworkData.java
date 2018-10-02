/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.view;

import otmui.model.Scenario;
import commodity.Subnetwork;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.util.stream.Collectors;

public class SubnetworkData extends AbstractData {

    public SubnetworkData(Subnetwork subnetwork, Scenario scenario) {
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

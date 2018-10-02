/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.view;

import otmui.model.Scenario;
import otmui.model.Sensor;
import javafx.collections.ObservableList;

public class SensorData extends AbstractData  {

    public SensorData(Sensor sensor, Scenario scenario){
        super();

        ObservableList<javafx.scene.Node> X = vbox.getChildren();

        // sensor id ....................
        X.add(UIFactory.createLabelText("id", String.format("%d",sensor.get_id())).pane);

        // link id ....................
        X.add(UIFactory.createLabelText("link id", String.format("%d",sensor.bsensor.link.getId())).pane);

        // position ....................
        X.add(UIFactory.createLabelText("position [m]", String.format("%.1f",sensor.bsensor.position)).pane);

        // lanes
        X.add(UIFactory.createLabelText("lanes", String.format("%d - %d",sensor.bsensor.start_lane,sensor.bsensor.end_lane)).pane);
    }


}

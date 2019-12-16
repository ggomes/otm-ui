package otmui.view;

import javafx.collections.ObservableList;
import sensor.FixedSensor;

public class SensorData extends AbstractData  {

    public SensorData(otmui.item.FixedSensor uisensor){
        super();

        sensor.FixedSensor sensor = uisensor.sensor;
        if(!(sensor instanceof sensor.FixedSensor))
            return;

        FixedSensor fsensor = (FixedSensor) sensor;

        ObservableList<javafx.scene.Node> X = vbox.getChildren();

        // sensor id ....................
        X.add(UIFactory.createLabelText("id", String.format("%d",sensor.id)).pane);

        // link id ....................
        X.add(UIFactory.createLabelText("link id", String.format("%d",fsensor.get_link().getId())).pane);

        // position ....................
        X.add(UIFactory.createLabelText("position [m]", String.format("%.1f",fsensor.get_position())).pane);

        // lanes
        X.add(UIFactory.createLabelText("lanes", String.format("%d - %d",fsensor.start_lane,fsensor.end_lane)).pane);
    }


}

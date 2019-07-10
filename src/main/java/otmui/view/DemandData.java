package otmui.view;

import otmui.event.FormSelectEvent;
import otmui.model.DemandsForLink;
import otmui.model.Scenario;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import keys.KeyCommodityDemandTypeId;
import profiles.Profile1D;

import java.util.Map;

public class DemandData extends AbstractData {

    public DemandData(DemandsForLink demandsForLink, Scenario scenario) {
        super();

        ObservableList<Node> X = vbox.getChildren();

        // id .................
        X.add(UIFactory.createLabelButton(
                "link id",
                String.format("%d", demandsForLink.get_link_id()),
                e->Event.fireEvent(scrollPane,new FormSelectEvent(FormSelectEvent.CLICK2_LINK, demandsForLink.get_link_id()))
        ).pane);

        // data
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setMaxHeight(400);

        X.add(lineChart);

        xAxis.setLabel("time [hr]");
        yAxis.setLabel("flow [vph]");
        for(Map.Entry<KeyCommodityDemandTypeId, Profile1D> e : demandsForLink.demands.entrySet()){
            KeyCommodityDemandTypeId key = e.getKey();
            Long comm_id = key.commodity_id;
            XYChart.Series series = new XYChart.Series();
            series.setName(scenario.getCommodityWithId(comm_id).get_name());
            Profile1D profile = e.getValue();
            double t = profile.start_time;
            double dt = profile.dt==0 ? 3600 : profile.dt;
            for(Double val : profile.values) {
                series.getData().add(new XYChart.Data(t/3600, val*3600));
                t += dt;
                if(profile.values.size()<40)
                    series.getData().add(new XYChart.Data(t/3600, val*3600));
            }
            lineChart.getData().add(series);
        }

    }
}

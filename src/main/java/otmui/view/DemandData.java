package otmui.view;

import api.OTMdev;
import api.info.DemandInfo;
import api.info.Profile1DInfo;
import otmui.event.FormSelectEvent;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.Set;

public class DemandData extends AbstractData {

    public DemandData(long link_id,Set<DemandInfo> demands, OTMdev otm) {
        super();

        ObservableList<Node> X = vbox.getChildren();

        // id .................
        X.add(UIFactory.createLabelButton(
                "link id",
                String.format("%d", link_id),
                e->Event.fireEvent(scrollPane,new FormSelectEvent(FormSelectEvent.CLICK2_LINK, link_id))
        ).pane);

        // data
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setMaxHeight(400);

        X.add(lineChart);

        xAxis.setLabel("time [hr]");
        yAxis.setLabel("flow [vph]");
        for(DemandInfo demand : demands){
//            KeyCommodityDemandTypeId
            Long comm_id = demand.commodity_id;
            XYChart.Series series = new XYChart.Series();
            series.setName(otm.scenario.commodities.get(comm_id).get_name());
            Profile1DInfo profile = demand.profile;
            double t = profile.getStart_time();
            double dt = profile.getDt()==0 ? 3600 : profile.getDt();
            for(Double val : profile.getValues()) {
                series.getData().add(new XYChart.Data(t/3600, val*3600));
                t += dt;
                if(profile.getValues().size()<40)
                    series.getData().add(new XYChart.Data(t/3600, val*3600));
            }
            lineChart.getData().add(series);
        }

    }
}

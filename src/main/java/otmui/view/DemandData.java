package otmui.view;

import api.info.DemandInfo;
import api.info.Profile1DInfo;
import otmui.Data;
import otmui.ItemType;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.Set;

public class DemandData extends AbstractData {

    public DemandData(long link_id,Set<DemandInfo> demands, Data data) {
        super();

        ObservableList<Node> X = vbox.getChildren();

        // id .................
        X.add(UIFactory.createLabelButton(
                "link id",
                String.format("%d", link_id),
                e->click2(data.items.get(ItemType.link).get(link_id))
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
            Long comm_id = demand.commodity_id;
            otmui.item.Commodity comm = (otmui.item.Commodity) data.items.get(ItemType.commodity).get(comm_id);
            XYChart.Series series = new XYChart.Series();
            series.setName(comm.comm.get_name());
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

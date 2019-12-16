package otmui.view;

import api.info.SplitInfo;
import otmui.Data;
import otmui.ItemType;
import otmui.controller.component.LabelCombobox;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import profiles.Profile2D;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SplitData extends AbstractData {

    private LineChart<Number,Number> lineChart;
    private LabelCombobox commodityCtrl;
    private LabelCombobox linkInCtrl;
    private Set<SplitInfo> splits;

    public SplitData(long node_id,Set<SplitInfo> splits, Data data) {
        super();

        this.splits = splits;

        ObservableList<Node> X = vbox.getChildren();

        // id .................
        X.add(UIFactory.createLabelButton(
                "node",
                String.format("%d",node_id),
                e -> click2(data.items.get(ItemType.node).get(node_id))
        ).pane);

        // commodity selector
        PaneCtrl panectrl1 = UIFactory.createLabelCombobox(
                "commodity",splits.stream()
                        .map(s->String.format("%d",s.commodity_id))
                        .collect(Collectors.toSet()));
        commodityCtrl = (LabelCombobox) panectrl1.ctrl;

        X.add(panectrl1.pane);

        // in link selector
        PaneCtrl panectrl2 = UIFactory.createLabelCombobox(
                "link in",
                splits.stream()
                        .map(s->String.format("%d",s.link_in_id))
                        .collect(Collectors.toSet()));
        linkInCtrl = (LabelCombobox) panectrl2.ctrl;
        X.add(panectrl2.pane);

        // draw button
        X.add(UIFactory.createLabelButton(
                "draw",
                "",
                e->drawSplitPlot()
        ).pane);

        // create splitsForNode plot
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("time [hr]");
        yAxis.setLabel("splitsForNode");
        lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setMaxHeight(300);
        X.add(lineChart);

        // draw
        drawSplitPlot();

    }

    private void drawSplitPlot(){

        Object commodity_id_str = commodityCtrl.combobox.getSelectionModel().getSelectedItem();
        Object linkin_id_str = linkInCtrl.combobox.getSelectionModel().getSelectedItem();

        if(commodity_id_str==null || linkin_id_str==null)
            return;

        Long commodity_id = Long.parseLong((String)commodity_id_str);
        Long linkin_id = Long.parseLong((String)linkin_id_str);

        Optional<SplitInfo> splitopt = splits.stream().filter(s->s.commodity_id==commodity_id && s.link_in_id==linkin_id).findFirst();

        if(!splitopt.isPresent())
            return;

        lineChart.getData().clear();
        Profile2D profile = splitopt.get().splits;
        double dt = profile.dt;
        for(Map.Entry<Long,List<Double>> e :  profile.values.entrySet()){
            Long linkOutId = e.getKey();
            List<Double> values = e.getValue();

            XYChart.Series series = new XYChart.Series();
            series.setName(String.format("link %d",linkOutId));
            double time = profile.start_time;
            for(Double val : values) {
                series.getData().add(new XYChart.Data(time/3600, val));
                time += dt;
                if(values.size()<40)
                    series.getData().add(new XYChart.Data(time/3600, val));
            }
            lineChart.getData().add(series);
        }

    }

}

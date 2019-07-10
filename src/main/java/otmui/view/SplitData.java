package otmui.view;

import otmui.controller.component.LabelComboboxController;
import otmui.event.FormSelectEvent;
import otmui.model.SplitsForNode;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import keys.KeyCommodityLink;
import profiles.SplitMatrixProfile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SplitData extends AbstractData {

    private LineChart<Number,Number> lineChart;
    private LabelComboboxController commodityCtrl;
    private LabelComboboxController linkInCtrl;
    private SplitsForNode splitsForNode;

    public SplitData(SplitsForNode splitsForNode) {
        super();

        this.splitsForNode = splitsForNode;

        ObservableList<Node> X = vbox.getChildren();

        // id .................
        X.add(UIFactory.createLabelButton(
                "node",
                splitsForNode.getNodeId().toString(),
                e-> Event.fireEvent(scrollPane,new FormSelectEvent(FormSelectEvent.CLICK2_NODE, splitsForNode.getNodeId()))
        ).pane);

        // commodity selector
        PaneCtrl panectrl1 = UIFactory.createLabelCombobox(
                "commodity",
                splitsForNode.getCommodityIds().stream()
                        .map(x->x.toString())
                        .collect(Collectors.toSet()));
        commodityCtrl = (LabelComboboxController) panectrl1.ctrl;

        X.add(panectrl1.pane);

        // in link selector
        PaneCtrl panectrl2 = UIFactory.createLabelCombobox(
                "link in",
                splitsForNode.getInLinkIds().stream()
                        .map(x->x.toString())
                        .collect(Collectors.toSet()));
        linkInCtrl = (LabelComboboxController) panectrl2.ctrl;
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
        KeyCommodityLink key = new KeyCommodityLink(commodity_id,linkin_id);

        if(!splitsForNode.splits.containsKey(key))
            return;

        SplitMatrixProfile split = splitsForNode.splits.get(key);
        double dt = split.get_dt()==0 ? 3600f : split.get_dt();

        lineChart.getData().clear();
        for(Map.Entry<Long,List<Double>> e :  split.get_outlink_to_profile().entrySet()){
            Long linkOutId = e.getKey();
            List<Double> values = e.getValue();

            XYChart.Series series = new XYChart.Series();
            series.setName(String.format("link %d",linkOutId));
            double time = split.get_start_time();
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

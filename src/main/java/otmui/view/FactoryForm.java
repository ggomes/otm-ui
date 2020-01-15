package otmui.view;

import actuator.AbstractActuator;
import api.info.DemandInfo;
import api.info.Profile1DInfo;
import api.info.SplitInfo;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import models.AbstractLaneGroup;
import otmui.Data;
import otmui.ItemType;
import otmui.controller.component.LabelCombobox;
import otmui.event.FormSelectEvent;
import otmui.item.AbstractItem;
import otmui.item.Commodity;
import profiles.Profile2D;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class FactoryForm {

    public static ScrollPane nodeForm(otmui.item.Node uinode, Data data){

        BaseForm form = new BaseForm();

        common.Node node = uinode.node;
        ObservableList<javafx.scene.Node> X = form.vbox.getChildren();

        // node id ....................
        X.add(FactoryComponent.createLabelText("id", String.format("%d",node.getId())).pane);

        // input links .................
        Set<AbstractItem> in_links = node.in_links.values().stream()
                .map(x->data.items.get(ItemType.link).get(x.getId()))
                .collect(toSet());
        X.add(FactoryComponent.createLabelList("input links", in_links).pane);

        // output links ................
        Set<AbstractItem> out_links = node.out_links.values().stream()
                .map(x->data.items.get(ItemType.link).get(x.getId()))
                .collect(toSet());
        X.add(FactoryComponent.createLabelList("output links",out_links).pane);

//        // splitsForNode .....................
//        if(node.splitsForNode !=null)
//            X.add(UIFactory.createLabelButton(
//                    "splitsForNode",
//                    "",
//                    e-> Event.fireEvent(scrollPane,new FormSelectEvent(FormSelectEvent.CLICK2_SPLIT,node.splitsForNode.getId()))
//            ).pane);

        // actuator ...................
        if(node.actuator!=null)
            X.add(FactoryComponent.createLabelButton(
                    "actuator",
                    String.format("%d",node.actuator.getId()),
                    e->click2(form,data.items.get(ItemType.actuator).get(node.actuator.getId()))
            ).pane);

        return form.scrollPane;
    }

    public static ScrollPane linkForm(otmui.item.Link uilink, Data data){

        BaseForm form = new BaseForm();

        common.Link link = uilink.link;

        ObservableList<Node> X = form.vbox.getChildren();

        // link id .................
        X.add(FactoryComponent.createLabelText("id",link.getId().toString()).pane);

        // start node .................
        X.add(FactoryComponent.createLabelButton("start node",
                link.start_node.getId().toString(),
                e->doubleClick(form,data.items.get(ItemType.node).get(link.start_node.getId()))
        ).pane);

        // end node .................
        X.add(FactoryComponent.createLabelButton("end node",
                link.end_node.getId().toString(),
                e->doubleClick(form,data.items.get(ItemType.node).get(link.end_node.getId()))
        ).pane);

        // length [LabelText] ................
        X.add(FactoryComponent.createLabelText("length [km]", String.format("%.0f",link.length)).pane);

        // full lanes [LabelText] ................
        X.add(FactoryComponent.createLabelText("full lanes", String.format("%d",link.full_lanes)).pane);

//        // left pocket
//        AddLanes left = link.getLeftLanes();
//        if(left!=null)
//            X.add(UIFactory.createAddLanes("Left pocket:",left));
//
//        // right pocket
//        AddLanes right = link.getRightLanes();
//        if(right!=null)
//            X.add(UIFactory.createAddLanes("Right pocket:",right));

        // hov lanes
        // DO THIS!!!

        // hov gates
        // DO THIS!!!

        // link model type
        X.add(FactoryComponent.createLabelLabel("link type",link.model.name).pane);

        // lanegroups
        Collection<AbstractLaneGroup> lanegroups = link.lanegroups_flwdn.values();
        List<String> lanegroupIds = lanegroups.stream().map(x->String.format("%d",x.id)).collect(Collectors.toList());
        X.add(FactoryComponent.createLabelCombobox("lanegroups",lanegroupIds).pane);

        // is sink
        X.add(FactoryComponent.createLabelCheckbox("is sink",link.is_sink).pane);

        // is source
        X.add(FactoryComponent.createLabelCheckbox("is source",link.is_source).pane);

//        // demandsForLink
//        if(link.demandsForLink !=null)
//            X.add(UIFactory.createLabelButton(
//                    "demands",
//                    "",
//                    e->Event.fireEvent(this.scrollPane,new FormSelectEvent(FormSelectEvent.CLICK2_DEMAND,link.demandsForLink.get_link_id()))
//            ).pane);
//
//        // actuator
//        if(link.actuator!=null) {
//            String actuatorId = String.format("%d",link.actuator.getId());
//            X.add(UIFactory.createLabelText("actuator", actuatorId).pane);
//        }

        return form.scrollPane;
    }

    public static ScrollPane sensorForm(otmui.item.FixedSensor uisensor , Data data) {
        BaseForm form = new BaseForm();

        sensor.FixedSensor sensor = uisensor.sensor;

        if(!(sensor instanceof sensor.FixedSensor))
            return null;

        ObservableList<javafx.scene.Node> X = form.vbox.getChildren();

        // sensor id ....................
        X.add(FactoryComponent.createLabelText("id", String.format("%d",sensor.id)).pane);

        // link id ....................
        X.add(FactoryComponent.createLabelText("link id", String.format("%d",sensor.get_link().getId())).pane);

        // position ....................
        X.add(FactoryComponent.createLabelText("position [m]", String.format("%.1f",sensor.get_position())).pane);

        // lanes
        X.add(FactoryComponent.createLabelText("lanes", String.format("%d - %d",sensor.start_lane,sensor.end_lane)).pane);

        return form.scrollPane;
    }

    public static ScrollPane actuatorForm(otmui.item.Actuator uiactuator, Data data) {
        BaseForm form = new BaseForm();

        AbstractActuator actuator = uiactuator.actuator;
        ObservableList<Node> X = form.vbox.getChildren();

        // id .................
        X.add(FactoryComponent.createLabelText("id", actuator.getId().toString()).pane);

        // type .................
        X.add(FactoryComponent.createLabelText("type", actuator.getType().toString()).pane);

        // target .................
        X.add(FactoryComponent.createLabelButton("target",
                actuator.target.getClass().getSimpleName() + " " + actuator.target.getId(),
                e-> {
                    AbstractItem item = null;
                    if(actuator.target instanceof common.Node)
                        item = data.items.get(ItemType.node).get(actuator.target.getId());
                    if(actuator.target instanceof common.Link)
                        item = data.items.get(ItemType.link).get(actuator.target.getId());
                    doubleClick(form,item);
                }
        ).pane);

        // controller .................
        if(actuator.myController!=null){
            X.add(FactoryComponent.createLabelButton("controller",
                    actuator.myController.getId().toString(),
                    e->click2(form,data.items.get(ItemType.controller).get(actuator.myController.getId()))
            ).pane);
        }

        return form.scrollPane;
    }

    public static ScrollPane controllerForm(otmui.item.Controller uicontroller, Data data) {
        BaseForm form = new BaseForm();

        control.AbstractController controller = uicontroller.controller;
        ObservableList<Node> X = form.vbox.getChildren();

        // id .................
        X.add(FactoryComponent.createLabelText("id", controller.getId().toString()).pane);

        // algorithm type;
        X.add(FactoryComponent.createLabelText("algorithm", controller.type.toString()).pane);

        // Set<AbstractActuator> actuators;
        Set<AbstractItem> items = new HashSet<>();
        items.addAll(uicontroller.actuators);
        X.add(FactoryComponent.createLabelList("actuators",items).pane);

        return form.scrollPane;
    }

    public static ScrollPane commodityForm(otmui.item.Commodity uicommodity, Data data) {
        BaseForm form = new BaseForm();

        commodity.Commodity commodity = uicommodity.comm;
        ObservableList<Node> X = form.vbox.getChildren();

        // id .................
        X.add(FactoryComponent.createLabelText("id", commodity.getId().toString()).pane);

        // String name;
        X.add(FactoryComponent.createLabelText("name", commodity.get_name()).pane);

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

        return form.scrollPane;
    }

    public static ScrollPane subnetworkForm(otmui.item.Subnetwork uisubnetwork, Data data) {
        BaseForm form = new BaseForm();

        ObservableList<Node> X = form.vbox.getChildren();

        // id .................
        X.add(FactoryComponent.createLabelText("id",String.format("%d",uisubnetwork.id)).pane);

        // name .................
        X.add(FactoryComponent.createLabelText("name",uisubnetwork.getName()).pane);

        // is_global .................
        X.add(FactoryComponent.createLabelCheckbox("is global",uisubnetwork.subnet.isGlobal()).pane);

        // links .................
        Set<AbstractItem> links = new HashSet<>();
        links.addAll(uisubnetwork.links);
        X.add(FactoryComponent.createLabelList("link",links).pane);

        //  .................
        Set<AbstractItem> comms = data.items.get(ItemType.commodity).values().stream()
                        .filter(x->((Commodity)x).comm.get_subnetwork_ids().contains(uisubnetwork.id))
                        .collect(toSet());
        X.add(FactoryComponent.createLabelList("vehicle types", comms).pane);

        return form.scrollPane;
    }

    public static ScrollPane demandForm(long link_id, Set<DemandInfo> demands, Data data) {

        BaseForm form = new BaseForm();

        ObservableList<Node> X = form.vbox.getChildren();

        // id .................
        X.add(FactoryComponent.createLabelButton(
                "link id",
                String.format("%d", link_id),
                e->click2(form,data.items.get(ItemType.link).get(link_id))
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

        return form.scrollPane;
    }

    public static ScrollPane splitForm(long node_id, Set<SplitInfo> splits, Data data) {

        BaseForm form = new BaseForm();

        ObservableList<Node> X = form.vbox.getChildren();

        // id .................
        X.add(FactoryComponent.createLabelButton(
                "node",
                String.format("%d",node_id),
                e -> click2(form,data.items.get(ItemType.node).get(node_id))
        ).pane);

        // commodity selector
        PaneCtrl panectrl1 = FactoryComponent.createLabelCombobox(
                "commodity",splits.stream()
                        .map(s->String.format("%d",s.commodity_id))
                        .collect(Collectors.toSet()));
        LabelCombobox commodityCtrl = (LabelCombobox) panectrl1.ctrl;

        X.add(panectrl1.pane);

        // in link selector
        PaneCtrl panectrl2 = FactoryComponent.createLabelCombobox(
                "link in",
                splits.stream()
                        .map(s->String.format("%d",s.link_in_id))
                        .collect(Collectors.toSet()));
        LabelCombobox linkInCtrl = (LabelCombobox) panectrl2.ctrl;
        X.add(panectrl2.pane);

        // create splitsForNode plot
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("time [hr]");
        yAxis.setLabel("splitsForNode");
        LineChart<Number,Number> lineChart = new LineChart<>(xAxis,yAxis);
        lineChart.setMaxHeight(300);
        X.add(lineChart);

        // draw button
        X.add(FactoryComponent.createLabelButton("draw", "", e->drawSplitPlot(splits,commodityCtrl,linkInCtrl,lineChart) ).pane);

        // draw
        drawSplitPlot(splits,commodityCtrl,linkInCtrl,lineChart);

        return form.scrollPane;
    }

    ////////////////////////////////////////////////////////////////////////////

    protected static void click1(BaseForm form,AbstractItem item){
        Event.fireEvent(form.scrollPane,new FormSelectEvent(FormSelectEvent.CLICK1, item));
    }

    protected static void click2(BaseForm form,AbstractItem item){
        Event.fireEvent(form.scrollPane,new FormSelectEvent(FormSelectEvent.CLICK2, item));
    }

    protected static void doubleClick(BaseForm form,AbstractItem item){
        click1(form,item);
        click2(form,item);
    }

    protected static void drawSplitPlot(Set<SplitInfo> splits,LabelCombobox commodityCtrl,LabelCombobox linkInCtrl,LineChart<Number,Number> lineChart){

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

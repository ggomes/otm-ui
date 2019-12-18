package otmui;

import api.OTMdev;
import error.OTMException;
import otmui.controller.GraphPaneController;
import otmui.item.*;
import sensor.AbstractSensor;

public class FactoryItem {

    public static otmui.item.Node makeNode(common.Node cnode, GlobalParameters params) {
        otmui.item.Node node = new otmui.item.Node(cnode,params);
        GraphPaneController.attach_mouse_click_handler(node);
        return node;
    }

    public static Link makeLink(common.Link clink,GlobalParameters params) throws OTMException {

        float lane_width_meters = params.lane_width_meters.getValue();
        float link_offset = params.link_offset.getValue();
        GlobalParameters.RoadColorScheme road_color_scheme = (GlobalParameters.RoadColorScheme) params.road_color_scheme.getValue();

        otmui.item.Link link;
        switch(clink.model.getClass().getSimpleName()){

            case "BaseModel":
            case "ModelSpatialQ":
            case "ModelNewell":
                link = new LinkSpaceQ(clink,
                        lane_width_meters,
                        link_offset,
                        road_color_scheme);
                break;

            case "ModelCTM":
                link = new LinkCTM(clink,
                        lane_width_meters,
                        link_offset,
                        road_color_scheme );
                break;

            default:
                throw new OTMException("Link model type not supported.");
        }

        GraphPaneController.attach_mouse_click_handler(link);

        return link;
    }

    public static Actuator makeActuator(OTMdev otmdev, actuator.AbstractActuator cactuator,GlobalParameters params) {

        float node_size = params.node_size.floatValue();

//        if (cactuator==null)
//            return new StopSign(null,0f,0f,size, 0f);

        common.Node node = otmdev.scenario.network.nodes.get(cactuator.target.getId());
        otmui.item.Actuator actuator;
        switch (cactuator.getType()) {
            case signal:
                actuator = new Signal(cactuator, node.xcoord, -node.ycoord, node_size, 4f);
                break;
            case stop:
                actuator = new StopSign(cactuator, node.xcoord, -node.ycoord, node_size, 0f );
                break;
            default:
                actuator = new StopSign(cactuator, node.xcoord, -node.ycoord, node_size, 1f);
                break;
        }

        GraphPaneController.attach_mouse_click_handler(actuator);

        return actuator;
    }

    public static FixedSensor makeSensor(AbstractSensor csensor,GlobalParameters params) throws OTMException {
        float lane_width_meters = params.lane_width_meters.getValue();
        float link_offset = params.link_offset.getValue();

        otmui.item.FixedSensor sensor = new FixedSensor(csensor, lane_width_meters, link_offset);

        GraphPaneController.attach_mouse_click_handler(sensor);
        return sensor;
    }



}

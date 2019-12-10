package otmui.graph.item;

import common.Point;
import geometry.Side;
import javafx.scene.paint.Color;
import otmui.GlobalParameters;
import models.BaseLaneGroup;
import error.OTMException;
import otmui.utils.Arrow;

import java.util.ArrayList;
import java.util.List;

public class DrawLinkCTM extends AbstractDrawLink {

    public DrawLinkCTM(common.Link link, AbstractDrawNode startNode, AbstractDrawNode endNode, float lane_width, float link_offset, GlobalParameters.RoadColorScheme road_color_scheme) throws OTMException {
        super(link, startNode, endNode, lane_width, link_offset, road_color_scheme);
    }

    @Override
    public AbstractDrawLanegroup create_draw_lanegroup(BaseLaneGroup lg, List<Arrow> midline, float lateral_offset, float long_offset, double lane_width, double road2euclid, Color color) throws OTMException {
        return new DrawLaneGroupCTM(lg,midline,lateral_offset,long_offset,lane_width,road2euclid,color);
    }

    @Override
    List<Double> get_additional_midline_points() {

        double shape_length=0d;
        for(int i = 0; i<link.shape.size()-1; i++){
            Point p0 = link.shape.get(i);
            Point p1 = link.shape.get(i+1);
            shape_length += Math.sqrt(Math.pow(p0.x-p1.x,2)+Math.pow(p0.y-p1.y,2));
        }
        double road2euclid = shape_length/link.length;

        // get a full lanegroup
        models.ctm.LaneGroup lg = (models.ctm.LaneGroup) this.link.lanegroups_flwdn.values()
                .stream()
                .filter(x->x.side== Side.middle)
                .findFirst().get();

        // length of a cell
        double road_cell_length_km = lg.length / lg.cells.size() / 1000f;
        double euclid_cell_length_m = road_cell_length_km * road2euclid * 1000f;

        List<Double> additional_points = new ArrayList<>();

        for(int i=0;i<lg.cells.size()-1;i++)
            additional_points.add(euclid_cell_length_m*(i+1));


        return additional_points;
    }

}


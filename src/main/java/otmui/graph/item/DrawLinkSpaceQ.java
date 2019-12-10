package otmui.graph.item;

import otmui.GlobalParameters;
import otmui.graph.color.AbstractColormap;
import otmui.model.Link;
import models.BaseLaneGroup;
import error.OTMException;
import otmui.utils.Arrow;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class DrawLinkSpaceQ extends AbstractDrawLink  {

    public DrawLinkSpaceQ(common.Link link, AbstractDrawNode startNode, AbstractDrawNode endNode, float lane_width, float link_offset, GlobalParameters.RoadColorScheme road_color_scheme) throws OTMException {
        super(link, startNode, endNode, lane_width, link_offset, road_color_scheme);
    }

    @Override
    public AbstractDrawLanegroup create_draw_lanegroup(BaseLaneGroup lg, List<Arrow> midline, float lateral_offset, float long_offset, double lane_width, double road2euclid, Color color) throws OTMException {
        return new DrawLanegroupSpaceQ(lg,midline,lateral_offset,long_offset,lane_width,road2euclid,color);
    }

    @Override
    List<Double> get_additional_midline_points() {
        return new ArrayList<>();
    }

}

package otmui.item;

import otmui.GlobalParameters;
import models.BaseLaneGroup;
import error.OTMException;
import otmui.utils.Arrow;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class LinkSpaceQ extends Link {

    public LinkSpaceQ(common.Link link, float lane_width, float link_offset, GlobalParameters.RoadColorScheme road_color_scheme) throws OTMException {
        super(link, lane_width, link_offset, road_color_scheme);
    }

    @Override
    public LaneGroup create_draw_lanegroup(BaseLaneGroup lg, List<Arrow> midline, float lateral_offset, float long_offset, double lane_width, double road2euclid, Color color) throws OTMException {
        return new LaneGroupSpaceQ(lg,midline,lateral_offset,long_offset,lane_width,road2euclid,color);
    }

    @Override
    List<Double> get_additional_midline_points() {
        return new ArrayList<>();
    }

}

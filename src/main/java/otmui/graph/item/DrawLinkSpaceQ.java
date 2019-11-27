package otmui.graph.item;

import otmui.graph.color.AbstractColormap;
import otmui.model.Link;
import models.BaseLaneGroup;
import error.OTMException;
import otmui.utils.Arrow;

import java.util.ArrayList;
import java.util.List;

public class DrawLinkSpaceQ extends AbstractDrawLink  {

    public DrawLinkSpaceQ(Link link, AbstractDrawNode startNode, AbstractDrawNode endNode, float lane_width, float link_offset, AbstractColormap colormap) throws OTMException {
        super(link, startNode, endNode, lane_width, link_offset, colormap);
    }

    @Override
    public AbstractDrawLanegroup create_draw_lanegroup(BaseLaneGroup lg, List<Arrow> midline, float lateral_offset, float long_offset, double lane_width,double road2euclid,AbstractColormap colormap) throws OTMException {
        return new DrawLanegroupSpaceQ(lg,midline,lateral_offset,long_offset,lane_width,road2euclid,colormap);
    }

    @Override
    List<Double> get_additional_midline_points() {
        return new ArrayList<>();
    }

}

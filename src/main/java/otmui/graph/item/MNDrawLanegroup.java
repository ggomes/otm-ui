package otmui.graph.item;

import otmui.graph.color.AbstractColormap;
import error.OTMException;
import models.ctm.LaneGroup;
import output.animation.AbstractLaneGroupInfo;

import java.util.List;

public class MNDrawLanegroup extends CTMDrawLanegroup {

    public MNDrawLanegroup(LaneGroup macro_lg, List<Segment> segments, float road2euclid) throws OTMException {
        super(macro_lg, segments, road2euclid);
    }

    @Override
    public void draw_state(AbstractLaneGroupInfo laneGroupInfo, AbstractColormap colormap) {
        draw_state_internal(
                laneGroupInfo,
                colormap,
                colormap.max_density_vphpl * road_cell_length_km * lanes);
    }

}

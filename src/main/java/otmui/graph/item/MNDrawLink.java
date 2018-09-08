package otmui.graph.item;

import otmui.graph.color.AbstractColormap;
import otmui.model.Link;
import common.AbstractLaneGroup;
import error.OTMException;

import java.util.List;

public class MNDrawLink extends AbstractDrawLink {

    public MNDrawLink(Link link, AbstractDrawNode startNode, AbstractDrawNode endNode, float lane_width, float link_offset, AbstractColormap colormap) throws OTMException {
        super(link, startNode, endNode, lane_width, link_offset, colormap);
    }

    @Override
    public AbstractDrawLanegroup create_draw_lanegroup(AbstractLaneGroup lg, List<Segment> segments, float road2euclid) throws OTMException {
        return new MNDrawLanegroup((models.ctm.LaneGroup) lg,segments,road2euclid);
    }

}

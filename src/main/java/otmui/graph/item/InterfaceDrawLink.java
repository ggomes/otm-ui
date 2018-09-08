package otmui.graph.item;

import common.AbstractLaneGroup;
import error.OTMException;

import java.util.List;

public interface InterfaceDrawLink {

    AbstractDrawLanegroup create_draw_lanegroup(AbstractLaneGroup lg, List<Segment> segments, float road2euclid) throws OTMException ;

}

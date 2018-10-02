/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.graph.item;

import otmui.graph.color.AbstractColormap;
import otmui.model.Link;
import common.AbstractLaneGroup;
import error.OTMException;

import java.util.List;

public class CTMDrawLink extends AbstractDrawLink {

    public CTMDrawLink(Link link, AbstractDrawNode startNode, AbstractDrawNode endNode, float lane_width, float link_offset,AbstractColormap colormap) throws OTMException {
        super(link, startNode, endNode, lane_width, link_offset, colormap);
    }

    @Override
    public AbstractDrawLanegroup create_draw_lanegroup(AbstractLaneGroup lg, List<Segment> segments, float road2euclid) throws OTMException {
        return new CTMDrawLanegroup((models.ctm.LaneGroup) lg,segments,road2euclid);
    }

}


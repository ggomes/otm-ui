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
import otmui.utils.Arrow;

import java.util.ArrayList;
import java.util.List;

public class DrawLinkPQ extends AbstractDrawLink  {

    public DrawLinkPQ(Link link, AbstractDrawNode startNode, AbstractDrawNode endNode, float lane_width, float link_offset, AbstractColormap colormap) throws OTMException {
        super(link, startNode, endNode, lane_width, link_offset, colormap);
    }

    @Override
    public AbstractDrawLanegroup create_draw_lanegroup(AbstractLaneGroup lg, List<Arrow> midline, double lateral_offset, double long_offset, double lane_width,double road2euclid,AbstractColormap colormap) throws OTMException {
        return new DrawLanegroupPQ(lg,lateral_offset,road2euclid);
    }

    @Override
    List<Double> get_additional_midline_points(double road2euclid) {
        return new ArrayList<>();
    }

}

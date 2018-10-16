/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.graph.item;

import geometry.Side;
import otmui.graph.color.AbstractColormap;
import otmui.model.Link;
import common.AbstractLaneGroup;
import error.OTMException;
import otmui.utils.Arrow;

import java.util.ArrayList;
import java.util.List;

public class DrawLinkCTM extends AbstractDrawLink {

    public DrawLinkCTM(Link link, AbstractDrawNode startNode, AbstractDrawNode endNode, float lane_width, float link_offset, AbstractColormap colormap) throws OTMException {
        super(link, startNode, endNode, lane_width, link_offset, colormap);
    }

    @Override
    public AbstractDrawLanegroup create_draw_lanegroup(AbstractLaneGroup lg, List<Arrow> midline, double lateral_offset, double long_offset,double lane_width, double road2euclid,AbstractColormap colormap) throws OTMException {
        return new DrawLaneGroupCTM(lg,midline,lateral_offset,long_offset,lane_width,road2euclid,colormap);
    }

    @Override
    List<Double> get_additional_midline_points(double road2euclid) {

        // get a full lanegroup
        models.ctm.LaneGroup lg = (models.ctm.LaneGroup) this.link.bLink.lanegroups_flwdn.values()
                .stream()
                .filter(x->x.side== Side.full)
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


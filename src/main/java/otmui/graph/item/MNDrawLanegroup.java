/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.graph.item;

import common.AbstractLaneGroup;
import otmui.graph.color.AbstractColormap;
import error.OTMException;
import output.animation.AbstractLaneGroupInfo;

import java.util.List;

public class MNDrawLanegroup extends CTMDrawLanegroup {

    public MNDrawLanegroup(AbstractLaneGroup macro_lg, List<Segment> segments, float road2euclid) throws OTMException {
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

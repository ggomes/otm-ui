/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.graph.item;

import common.AbstractLaneGroup;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import otmui.graph.color.AbstractColormap;
import otmui.model.Link;
import output.animation.AbstractLaneGroupInfo;

import java.util.List;

public abstract class AbstractDrawLanegroup {

    public Long id;
    public int lanes;

    abstract public void unhighlight();
    abstract public void highlight(Color color);
    abstract public void set_temporary_color(Color color);
    abstract public List<Polygon> make_polygons(Link link, float lane_width, float link_offset, AbstractColormap colormap);
    abstract public void draw_state(AbstractLaneGroupInfo laneGroupInfo, AbstractColormap colormap);

    public AbstractDrawLanegroup(AbstractLaneGroup lg) {
        this.lanes = lg.num_lanes();
        this.id = lg.id;
    }

}

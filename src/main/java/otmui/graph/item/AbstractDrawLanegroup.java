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
import output.animation.AbstractLaneGroupInfo;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractDrawLanegroup {

    public Long id;
    public int lanes;
    public double lateral_offset;
    public List<DrawCell> draw_cells;

    abstract public void unhighlight();
    abstract public void highlight(Color color);
    abstract public void set_temporary_color(Color color);
    abstract public void draw_state(AbstractLaneGroupInfo laneGroupInfo, AbstractColormap colormap);

    public AbstractDrawLanegroup(AbstractLaneGroup lg,double lateral_offset) {
        this.lanes = lg.num_lanes;
        this.id = lg.id;
        this.lateral_offset = lateral_offset;
    }

    public Set<Polygon> get_polygons(){
        return draw_cells.stream().map(x->x.polygon).collect(Collectors.toSet());
    }

}

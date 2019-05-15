/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.graph.item;

import models.AbstractLaneGroup;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import otmui.graph.color.AbstractColormap;
import otmui.utils.Arrow;
import output.animation.AbstractLaneGroupInfo;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Comparator.comparingDouble;
import static java.util.stream.Collectors.toList;

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

    public static int find_closest_arrow_index_to(List<Arrow> midline, double p){
        List<Double> a = midline.stream().map(x->Math.abs(x.position-p)).collect(toList());
        return IntStream.range(0,a.size()).boxed()
                .min(comparingDouble(a::get))
                .get();
    }
}

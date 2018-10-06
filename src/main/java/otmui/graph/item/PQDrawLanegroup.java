/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.graph.item;

import common.AbstractLaneGroup;
import otmui.graph.color.AbstractColormap;
import otmui.model.Link;
import otmui.utils.Point;
import otmui.utils.Vector;
import error.OTMException;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import output.animation.AbstractLaneGroupInfo;

import java.util.ArrayList;
import java.util.List;

public class PQDrawLanegroup extends AbstractDrawLanegroup  {

    public DrawCell cell;

    public PQDrawLanegroup(AbstractLaneGroup meso_lg, List<Segment> segments, float road2euclid) throws OTMException {

        super(meso_lg);

        // length of a cell
        float euclid_cell_length = meso_lg.length * road2euclid;

        // initialize segment
        int seg_ind = 0;
        Vector curr_direction = segments.get(seg_ind).u;
        float segment_remaining = segments.get(seg_ind).euclid_length;
        Point latest_point = segments.get(seg_ind).p0;

        cell = new DrawCell();
        cell.add_point(latest_point);

        float cell_remaining = euclid_cell_length;
        boolean done_with_cell = false;
        while (!done_with_cell) {

            // go to next segment, same cell
            if (segment_remaining <= cell_remaining) {

                float advance = segment_remaining;
                cell_remaining -= advance;
                latest_point = Vector.sum(latest_point, Vector.mult(curr_direction, advance));
                done_with_cell = cell_remaining<0.01;

                if(!done_with_cell) {
                    seg_ind += 1;
                    if (seg_ind >= segments.size())
                        throw new OTMException("Went beyond last segment.");
                    curr_direction = segments.get(seg_ind).u;
                    segment_remaining = segments.get(seg_ind).euclid_length;
                }

            } else { // go to next cell, same segment
                float advance = cell_remaining;
                segment_remaining -= advance;
                latest_point = Vector.sum(latest_point, Vector.mult(curr_direction, advance));
                done_with_cell = true;
            }

            cell.add_point(latest_point);
        }

    }

    @Override
    public void unhighlight() {
        cell.unhighlight();
    }

    @Override
    public void highlight(Color color) {
        cell.highlight(color);
    }

    @Override
    public void set_temporary_color(Color color) {
        cell.set_temporary_color(color);
    }

    @Override
    public List<Polygon> make_polygons(Link link, float lane_width, float link_offset, AbstractColormap colormap) {
        float width = link.getLanes() * lane_width;
        List<Polygon> polygons = new ArrayList<>();
        cell.make_polygon(null, null, link_offset, width,colormap,link.bLink.road_type);
        polygons.add(cell.polygon);
        return polygons;
    }

    @Override
    public void draw_state(AbstractLaneGroupInfo laneGroupInfo, AbstractColormap colormap) {
        System.err.println("IMplement this");
    }

}

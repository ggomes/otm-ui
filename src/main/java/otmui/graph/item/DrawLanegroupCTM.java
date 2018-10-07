/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.graph.item;

import common.AbstractLaneGroup;
import otmui.graph.color.AbstractColormap;
import otmui.graph.color.RGB;
import otmui.model.Link;
import otmui.utils.Point;
import error.OTMException;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import output.animation.AbstractLaneGroupInfo;
import output.animation.macro.CellInfo;

import java.util.ArrayList;
import java.util.List;

public class DrawLanegroupCTM extends AbstractDrawLanegroup {

    public float max_vehicles_per_cell;
    public List<DrawCell> draw_cells;
    public float euclid_cell_length_m;
    public float road_cell_length_km;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public DrawLanegroupCTM(AbstractLaneGroup macro_lg, List<Segment> segments, float road2euclid) throws OTMException {

        super(macro_lg);

        throw new OTMException("UNCOMMENT THIS.");

//        this.max_vehicles_per_cell = macro_lg.max_vehicles / macro_lg.cells.size();
//
//        draw_cells = new ArrayList<>();
//
//        // length of a cell
//        road_cell_length_km = macro_lg.length / macro_lg.cells.size() / 1000f;
//        euclid_cell_length_m = road_cell_length_km * road2euclid * 1000f;
//
//        // initialize segment
//        int seg_ind = 0;
//        Vector curr_direction = segments.get(seg_ind).u;
//        float segment_remaining = segments.get(seg_ind).euclid_length;
//        Point latest_point = segments.get(seg_ind).p0;
//
//        // traverse cells
//        for (int i = 0; i < macro_lg.cells.size(); i++) {
//
//            DrawCell draw_cell = new DrawCell();
//            this.add_drawcell(draw_cell);
//
//            draw_cell.add_point(latest_point);
//
//            // terminate either with cell or segment end
//            float cell_remaining = euclid_cell_length_m;
//            boolean done_with_cell = false;
//            while (!done_with_cell) {
//
//                // go to next segment, same cell
//                if (segment_remaining <= cell_remaining) {
//
//                    float advance = segment_remaining;
//                    cell_remaining -= advance;
//                    latest_point = Vector.sum(latest_point, Vector.mult(curr_direction, advance));
//                    done_with_cell = cell_remaining<0.01;
//
//                    if(!done_with_cell) {
//                        seg_ind += 1;
//                        if (seg_ind >= segments.size())
//                            throw new OTMException("Went beyond last segment.");
//                        curr_direction = segments.get(seg_ind).u;
//                        segment_remaining = segments.get(seg_ind).euclid_length;
//                    }
//
//                } else { // go to next cell, same segment
//
//                    float advance = cell_remaining;
//                    segment_remaining -= advance;
//                    latest_point = Vector.sum(latest_point, Vector.mult(curr_direction, advance));
//
//                    done_with_cell = true;
//                }
//
//                draw_cell.add_point(latest_point);
//            }
//        }

    }

    public void add_drawcell(DrawCell x){
        draw_cells.add(x);
    }

    @Override
    public List<Polygon> make_polygons(Link link, float lane_width, float link_offset, AbstractColormap colormap) {
        float width = link.getLanes() * lane_width;
        List<Polygon> polygons = new ArrayList<>();

        for (int i = 0; i < draw_cells.size(); i++) {

            // get a points upstream and downstream of this cell
            Point pdn = i == 0 ? null :
                    draw_cells.get(i-1).get_second_from_up();
            Point pup = i == draw_cells.size() - 1 ? null :
                    draw_cells.get(i+1).get_second_from_down();

            // make the polygon for the cell
            DrawCell drawcell = draw_cells.get(i);
            drawcell.make_polygon(pup, pdn, link_offset, width,colormap,link.bLink.road_type);
            polygons.add(drawcell.polygon);
        }
        return polygons;
    }

    /////////////////////////////////////////////////
    // highlighting
    /////////////////////////////////////////////////

    @Override
    public void unhighlight() {
        for(DrawCell cell : draw_cells)
            cell.unhighlight();
    }

    @Override
    public void highlight(Color color) {
        for(DrawCell cell : draw_cells)
            cell.highlight(color);
    }

    /////////////////////////////////////////////////
    // coloring
    /////////////////////////////////////////////////

    @Override
    public void set_temporary_color(Color color) {
        for(DrawCell cell : draw_cells)
            cell.set_temporary_color(color);
    }

    @Override
    public void draw_state(AbstractLaneGroupInfo laneGroupInfo, AbstractColormap colormap) {
        draw_state_internal(
                laneGroupInfo,
                colormap,
                max_vehicles_per_cell );
    }

    protected void draw_state_internal(AbstractLaneGroupInfo laneGroupInfo, AbstractColormap colormap,float max_vehicles){
        output.animation.macro.LaneGroupInfo lg_info = (output.animation.macro.LaneGroupInfo) laneGroupInfo;
        for(int i=0;i<lg_info.cell_info.size();i++){
            CellInfo cellinfo = lg_info.cell_info.get(i);
            DrawCell drawCell = draw_cells.get(i);
            double veh = cellinfo.get_total_vehicles();
            RGB rgb = colormap.get_color(veh,max_vehicles);
            Color color = new Color(rgb.red,rgb.green,rgb.blue,1.0);
            drawCell.set_temporary_color(color);
        }

    }

}

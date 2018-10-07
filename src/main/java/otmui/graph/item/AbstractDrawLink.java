/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.graph.item;

import otmui.graph.color.AbstractColormap;
import otmui.model.Link;
import otmui.utils.Point;
import common.AbstractLaneGroup;
import error.OTMException;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDrawLink extends Group {

    protected static Color color_highlight = Color.RED;

    public otmui.model.Link link;
    public Long id;
    public List<AbstractDrawLanegroup> draw_lanegroups;
    public AbstractDrawNode startNode;
    public AbstractDrawNode endNode;
    public float max_vehicles;

    abstract AbstractDrawLanegroup create_draw_lanegroup(AbstractLaneGroup lg, List<Segment> segments, float road2euclid) throws OTMException ;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public AbstractDrawLink(otmui.model.Link link, AbstractDrawNode startNode, AbstractDrawNode endNode, float lane_width, float link_offset, AbstractColormap colormap) throws OTMException {
        this.link = link;
        this.id = link.getId();
        this.startNode = startNode;
        this.endNode = endNode;
        this.max_vehicles = link.bLink.get_max_vehicles();
        this.draw_lanegroups = new ArrayList<>();

        // case for mn, where get_max_vehicles returns infinity
        if(Float.isInfinite(this.max_vehicles))
            this.max_vehicles = (float) (link.bLink.length * link.bLink.full_lanes * (180.0 / 1600.0));

        // Draw the road ....................................................

        // traverse the points from downstream to upstream. Create Segments
        List<Point> points = link.getShape();
        List<Segment> segments = new ArrayList<>();
        for (int i = points.size() - 1; i > 0; i--)
            segments.add(new Segment(points.get(i), points.get(i - 1)));

        // road2euclid
        float euclid_segment_length = segments.stream().map(x -> x.euclid_length).reduce(0f, (i, j) -> i + j);
        float road_segment_length = link.bLink.length;
        float road2euclid = euclid_segment_length / road_segment_length;

        throw new OTMException("UNCOMMENT THSI");
//        // populate draw_lanegroups
//        for (AbstractLaneGroup lg : link.bLink.lanegroups.values())
//            draw_lanegroups.add(create_draw_lanegroup(lg,segments,road2euclid));
//
//        // make the polygons
//        for (AbstractDrawLanegroup draw_lanegroup : draw_lanegroups) {
//            List<Polygon> polygons = draw_lanegroup.make_polygons(link, lane_width, link_offset, colormap);
//            getChildren().addAll(polygons);
//        }

    }

    /////////////////////////////////////////////////
    // highlight
    /////////////////////////////////////////////////

    public void highlight() {
        draw_lanegroups.forEach(x -> x.highlight(color_highlight));
    }

    public void unhighlight() {
        draw_lanegroups.forEach(x -> x.unhighlight());
    }

    /////////////////////////////////////////////////
    // get
    /////////////////////////////////////////////////

    public Double getStartPosX(){
        return startNode.xpos;
    }

    public Double getStartPosY(){
        return startNode.ypos;
    }

    public Double getEndPosX(){
        return endNode.xpos;
    }

    public Double getEndPosY(){
        return endNode.ypos;
    }

}
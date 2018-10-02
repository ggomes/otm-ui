/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.graph.item;

import otmui.graph.color.AbstractColormap;
import otmui.utils.Point;
import otmui.utils.Vector;
import common.Link;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;

public class DrawCell {

    public List<Point> points;
    public Polygon polygon;
    public Color mycolor;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public DrawCell() {
        points = new ArrayList<>();
        polygon = new Polygon();
        mycolor = new Color(0.0,0.0,0.0,1.0);
    }

    public void add_point(Point point){
        points.add(point);
    }

    public void make_polygon(Point pup, Point pdn, float offset, float width, AbstractColormap colormap, Link.RoadType road_type) {

        // add pup and pdwn to points
        List<Point> temppoints = new ArrayList<>();
        temppoints.add(pdn);
        temppoints.addAll(points);
        temppoints.add(pup);

        Vector u_prev, u_next, q_prev, q_next, n;
        Point p_dn, p_this, p_up;
        float correction;
        Point p_inner, p_outer;

        List<Point> inner = new ArrayList<>();
        List<Point> outer = new ArrayList<>();

        for (int i = 1; i < temppoints.size() - 1; i++) {
            p_dn = temppoints.get(i - 1);
            p_this = temppoints.get(i);
            p_up = temppoints.get(i + 1);

            u_prev = new Vector(p_dn, p_this);
            u_next = new Vector(p_this, p_up);

            q_prev = Vector.normalize(Vector.cross_z(u_prev));
            q_next = Vector.normalize(Vector.cross_z(u_next));

            n = Vector.normalize(Vector.sum(q_prev, q_next));

            correction = q_next==null ? 1f/Vector.dot(q_prev,n) : 1f/Vector.dot(q_next,n);

            p_inner = Vector.sum(p_this, Vector.mult(n, offset * correction));
            p_outer = Vector.sum(p_inner, Vector.mult(n, width * correction));

            inner.add(p_inner);
            outer.add(p_outer);
        }

        polygon = new Polygon();
        polygon.setStrokeWidth(0d);

        for (Point p : inner)
            polygon.getPoints().addAll(new Double[]{(double) p.x, (double) -p.y});

        for (int i = outer.size() - 1; i >= 0; i--) {
            Point p = outer.get(i);
            polygon.getPoints().addAll(new Double[]{(double) p.x, (double) -p.y});
        }

        // apply color according to road type
        set_fixed_color(colormap.get_color_for_roadtype(road_type));

    }

    /////////////////////////////////////////////////
    // setText
    /////////////////////////////////////////////////

    public void highlight(Color color){
        polygon.setFill(color);
    }

    public void unhighlight(){
        polygon.setFill(mycolor);
    }

    /////////////////////////////////////////////////
    // color
    /////////////////////////////////////////////////

    public void set_fixed_color(Color color){
        polygon.setFill(color);
        mycolor = color;
    }

    public void set_temporary_color(Color color) {
        polygon.setFill(color);
    }

    /////////////////////////////////////////////////
    // get
    /////////////////////////////////////////////////

    public Point get_downstream_point(){
        return points.get(0);
    }

    public Point get_second_from_down(){
        return points.get(1);
    }

    public Point get_second_from_up(){
        return points.get(points.size()-2);
    }

    public Point get_upstream_point(){
        return points.get(points.size()-1);
    }

}

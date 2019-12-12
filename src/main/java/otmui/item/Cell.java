package otmui.item;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import otmui.utils.Arrow;
import otmui.utils.Vector;

import java.util.ArrayList;
import java.util.List;

public class Cell {

    public List<Arrow> arrows;
    public Polygon polygon;
    public Color mycolor;

    public Cell(List<Arrow> midline, int ind, double lateral_offset, double length, double width, Color color){

        arrows = new ArrayList<>();
        double remaining = length;
        while(ind<midline.size() && remaining>-BaseLink.epsilon) {
            Arrow a = midline.get(ind++);
            arrows.add(a);
            remaining -= a.distance_to_next;
        }

        polygon = new Polygon();
        make_polygon(arrows,(float) lateral_offset,(float) width );
        paintColor(color);
    }

    /////////////////////////////////////////////////
    // color
    /////////////////////////////////////////////////

    public void set_temporary_color(Color color) {
        polygon.setFill(color);
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
    // set
    /////////////////////////////////////////////////

    public void paintShape(float lateral_offset, float width, Color color){
        make_polygon(arrows,lateral_offset,width);
        paintColor(color);
    }

    public void paintColor(Color color){
        mycolor = color;
        polygon.setFill(color);
    }

    /////////////////////////////////////////////////
    // get
    /////////////////////////////////////////////////

    public double get_downstream_offset(){
        return arrows.get(arrows.size()-1).position;
    }

    private void make_polygon(List<Arrow> arrows, float lateral_offset, float width ) {

        List<Vector> inner = new ArrayList<>();
        List<Vector> outer = new ArrayList<>();

        for (Arrow a : arrows) {
            inner.add(Vector.sum(a.start,Vector.mult(a.direction,lateral_offset)));
            outer.add(Vector.sum(a.start,Vector.mult(a.direction,lateral_offset+width)));
        }

        polygon.setStrokeWidth(0d);
        polygon.getPoints().clear();

        for (Vector p : inner)
            polygon.getPoints().addAll(new Double[]{(double) p.x, (double) -p.y});

        for (int i = outer.size() - 1; i >= 0; i--) {
            Vector p = outer.get(i);
            polygon.getPoints().addAll(new Double[]{(double) p.x, (double) -p.y});
        }

//        return polygon;
    }

//    private static Polygon make_polygon(Vector pup, Vector pdn, float offset, float width, AbstractColormap colormap, Link.RoadType road_type) {
//
//        // add pup and pdwn to points
//        List<Vector> temppoints = new ArrayList<>();
//        temppoints.add(pdn);
//        temppoints.addAll(points);
//        temppoints.add(pup);
//
//        Vector u_prev, u_next, q_prev, q_next, n;
//        Vector p_dn, p_this, p_up;
//        float correction;
//        Vector p_inner, p_outer;
//
//        List<Vector> inner = new ArrayList<>();
//        List<Vector> outer = new ArrayList<>();
//
//        for (int i = 1; i < temppoints.size() - 1; i++) {
//            p_dn = temppoints.get(i - 1);
//            p_this = temppoints.get(i);
//            p_up = temppoints.get(i + 1);
//
//            u_prev = new Vector(p_dn, p_this);
//            u_next = new Vector(p_this, p_up);
//
//            q_prev = Vector.normalize(Vector.cross_z(u_prev));
//            q_next = Vector.normalize(Vector.cross_z(u_next));
//
//            n = Vector.normalize(Vector.sum(q_prev, q_next));
//
//            correction = q_next==null ? 1f/Vector.dot(q_prev,n) : 1f/Vector.dot(q_next,n);
//
//            p_inner = Vector.sum(p_this, Vector.mult(n, offset * correction));
//            p_outer = Vector.sum(p_inner, Vector.mult(n, width * correction));
//
//            inner.add(p_inner);
//            outer.add(p_outer);
//        }
//
//        Polygon polygon = new Polygon();
//        polygon.setStrokeWidth(0d);
//
//        for (Vector p : inner)
//            polygon.getPoints().addAll(new Double[]{(double) p.x, (double) -p.y});
//
//        for (int i = outer.size() - 1; i >= 0; i--) {
//            Vector p = outer.get(i);
//            polygon.getPoints().addAll(new Double[]{(double) p.x, (double) -p.y});
//        }
//
//        // apply color according to road type
////        set_fixed_color(colormap.get_color_for_roadtype(road_type));
//
//        return polygon;
//    }

}

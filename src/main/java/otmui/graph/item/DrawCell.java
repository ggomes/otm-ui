package otmui.graph.item;

import common.Link;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import otmui.graph.color.AbstractColormap;
import otmui.utils.Arrow;
import otmui.utils.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Comparator.comparingDouble;
import static java.util.stream.Collectors.toList;

public class DrawCell {

    public List<Arrow> arrows;
    public Polygon polygon;
    public Color mycolor;

    public DrawCell(List<Arrow> midline, double long_offset, double lateral_offset, double length, double width, AbstractColormap colormap){
        arrows = new ArrayList<>();
        double remaining = length;
        int ind = find_closest_arrow_index_to(midline,long_offset);
        while(ind<midline.size() && remaining>-AbstractDrawLink.epsilon) {
            Arrow a = midline.get(ind++);
            arrows.add(a);
            remaining -= a.distance_to_next;
        }

        make_polygon(arrows,(float) lateral_offset,(float) width,colormap,Link.RoadType.freeway);

        System.out.println(arrows);
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
    // setText
    /////////////////////////////////////////////////

    public void highlight(Color color){
        polygon.setFill(color);
    }

    public void unhighlight(){
        polygon.setFill(mycolor);
    }


    /////////////////////////////////////////////////
    // get
    /////////////////////////////////////////////////
//
//    public Vector get_downstream_point(){
//        return arrows.get(0).start;
//    }
//
//    public Vector get_second_from_down(){
//        return arrows.get(1).start;
//    }
//
//    public Vector get_second_from_up(){
//        return arrows.get(arrows.size()-2).start;
//    }
//
//    public Vector get_upstream_point(){
//        return arrows.get(arrows.size()-1).start;
//    }
//

    public static int find_closest_arrow_index_to(List<Arrow> midline,double p){
        List<Double> a = midline.stream().map(x->Math.abs(x.position-p)).collect(toList());
        return IntStream.range(0,a.size()).boxed()
                .min(comparingDouble(a::get))
                .get();
    }

    private void make_polygon(List<Arrow> arrows, float lateral_offset, float width, AbstractColormap colormap, Link.RoadType road_type) {

        List<Vector> inner = new ArrayList<>();
        List<Vector> outer = new ArrayList<>();

        for (Arrow a : arrows) {
            inner.add(Vector.sum(a.start,Vector.mult(a.direction,lateral_offset)));
            outer.add(Vector.sum(a.start,Vector.mult(a.direction,lateral_offset+width)));
        }

        polygon = new Polygon();
        polygon.setStrokeWidth(0d);

        for (Vector p : inner)
            polygon.getPoints().addAll(new Double[]{(double) p.x, (double) -p.y});

        for (int i = outer.size() - 1; i >= 0; i--) {
            Vector p = outer.get(i);
            polygon.getPoints().addAll(new Double[]{(double) p.x, (double) -p.y});
        }

        // apply color according to road type
        set_fixed_color(colormap.get_color_for_roadtype(road_type));

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

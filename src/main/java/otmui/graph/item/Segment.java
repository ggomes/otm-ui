package otmui.graph.item;

import otmui.utils.Point;
import otmui.utils.Vector;

public class Segment {
    public Point p0;
    public Point p1;
    public Vector u;  // points downstream to upstream
    public float euclid_length;

    public Segment(Point p0, Point p1) {
        this.p0 = p0;
        this.p1 = p1;
        Vector p0p1 = new Vector(p0, p1);
        this.euclid_length = Vector.norm(p0p1);
        this.u = Vector.normalize(p0p1);
    }

}
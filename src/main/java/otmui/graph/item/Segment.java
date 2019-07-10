package otmui.graph.item;

import otmui.utils.Arrow;
import otmui.utils.Vector;

public class Segment {
    public Arrow A;
    public Arrow B;
    public Vector u;
    public float euclid_length;

    public Segment(Arrow A, Arrow B) {
        this.A = A;
        this.B = B;
        Vector p0p1 = new Vector(A.start, B.start);
        this.u = Vector.normalize(p0p1);
        this.euclid_length = Vector.length(p0p1);
    }

}
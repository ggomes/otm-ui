package otmui.utils;
public class Arrow implements Comparable<Arrow>{
    public double position;             // distance along the midline
    public Vector start;                // coordinates of the point
    public Vector direction;            // lateral/outward direction
    public double distance_to_next;     // position of next - position of this
    public  Arrow(double position,Vector start,Vector direction){
        this.position = position;
        this.start = start;
        this.direction = direction;
    }

    @Override
    public int compareTo(Arrow that) {
        return Double.compare(this.position,that.position);
    }

}

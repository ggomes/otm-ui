package otmui.utils;

public class Point {
    public float x;
    public float y;
    public Point(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Point(common.Point p){
        this.x = p.x;
        this.y = p.y;
    }

}

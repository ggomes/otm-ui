package otmui.utils;

public class Vector {

    public boolean isnull;
    public float x,y;

    public Vector(Point from, Point to){
        isnull = from==null || to==null;
        if(!isnull) {
            this.x = to.x - from.x;
            this.y = to.y - from.y;
        }
    }

    public Vector(float x, float y){
        this.x = x;
        this.y = y;
    }

    public double get_angle_deg(){
        return Math.toDegrees(Math.atan2(y,x));
    }

    public static float norm(Vector a){
        return (float) Math.sqrt(a.x*a.x + a.y*a.y);
    }

    public static Vector normalize(Vector a){
        if(a==null || a.isnull)
            return null;
        float m = norm(a);
        return new Vector(a.x/m , a.y/m );
    }

    public static Vector sum(Vector a,Vector b){
        if(a==null || a.isnull)
            return b;
        if(b==null || b.isnull)
            return a;
        return new Vector(a.x+b.x,a.y+b.y);
    }

    public static Point sum(Point p, Vector a){
        return new Point(p.x + a.x,p.y + a.y );
    }

    public static Vector mult(Vector a,float m){
        return new Vector(a.x*m,a.y*m);
    }

    public static Vector cross_z(Vector a){
        return a.isnull ? null : new Vector(a.y,-a.x);
    }

    public static float dot(Vector a,Vector b){
        return a.x*b.x + a.y*b.y;
    }


}

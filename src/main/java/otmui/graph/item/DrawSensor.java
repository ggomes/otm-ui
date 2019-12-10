package otmui.graph.item;

import error.OTMException;
import otmui.utils.Arrow;
import otmui.utils.Vector;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import sensor.AbstractSensor;
import sensor.FixedSensor;

public class DrawSensor extends Group {

    public Long id;
    public Arrow geom;
    public Shape shape;

    protected static Color color1 = Color.DODGERBLUE;
    protected static Color color2 = Color.RED;

    public DrawSensor() {
        this.id = null;
    }

    public DrawSensor(AbstractSensor sensor, float lane_width, float link_offset) throws OTMException {

        this.id = sensor.id;

        if(!(sensor instanceof FixedSensor))
            return;

        FixedSensor fsensor = (FixedSensor) sensor;

        this.geom = traverse_distance(fsensor.get_link(),fsensor.get_position());

        if(geom==null)
            return;

        float sensor_length = 2f;

        Vector p = geom.start;
        Vector u = geom.direction;

        int start_lane = fsensor.start_lane;
        int end_lane = fsensor.end_lane;

        Vector n = Vector.cross_z(u);

        Vector inner = Vector.sum(p , Vector.mult( n, (-1f)* (link_offset + (start_lane-1)*lane_width) ) );
        Vector outer = Vector.sum( inner , Vector.mult(n , (-1f)* (end_lane-start_lane+1)*lane_width ) );

        Vector mlu = Vector.mult(u , -sensor_length/2) ;
        Vector plu = Vector.mult(u, sensor_length/2 );

        Vector uinn = Vector.sum(inner , mlu);
        Vector dinn = Vector.sum(inner , plu);
        Vector uout = Vector.sum(outer , mlu);
        Vector dout = Vector.sum(outer , plu);

        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[]{
                (double) uinn.x, (double) -uinn.y ,
                (double) dinn.x, (double) -dinn.y ,
                (double) dout.x, (double) -dout.y ,
                (double) uout.x, (double) -uout.y ,
        });

        polygon.setStrokeWidth(0d);
        polygon.setFill(Color.DODGERBLUE);
        setView(polygon);

        unhighlight();
    }

    public void setView(Shape shape) {
        this.shape = shape;
        getChildren().clear();
        getChildren().add(shape);
    }

    public double getXPos(){
        return geom.start.x;
    }

    public double getYPos(){
        return geom.start.y;
    }

    /** *****************
     * Change color
     ******************** */

    public void highlight() {
        if(shape!=null)
            shape.setFill(color2);
    }

    public void unhighlight() {
        if(shape!=null)
            shape.setFill(color1);
    }

    /** Starting from upstream node, traverse the link a distance x and return the resulting point.
     * Return null if x<0 or x> length.
     */
    private static Arrow traverse_distance(common.Link link, float x) throws OTMException {

        int seg_ind = 0;
        Vector current_point = new Vector( link.shape.get(seg_ind) );
        Vector next_point = new Vector( link.shape.get(seg_ind+1) );
        Vector u = new Vector(current_point,next_point);
        float segment_length = Vector.length(u);
        float remaining_distance = x;

        Arrow P = null;
        while(remaining_distance>0){

            if(remaining_distance>segment_length){
                // point is not in this segment

                remaining_distance -= segment_length;

                seg_ind += 1;

                if( seg_ind >= link.shape.size() )
                    throw new OTMException("Point not within the link");

                current_point = new Vector(link.shape.get(seg_ind) );
                next_point = new Vector(link.shape.get(seg_ind+1) );
                u = new Vector(current_point,next_point);
                segment_length = Vector.length(u);

            } else{   // point is in this segment
                Vector unorm = Vector.normalize(u);
                P = new Arrow( Double.NaN,Vector.sum(current_point,Vector.mult(unorm,remaining_distance)) ,
                        unorm );
                remaining_distance = -1f;
            }
        }

        return P;
    }
}

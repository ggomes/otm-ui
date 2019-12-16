package otmui.item;

import error.OTMException;
import otmui.ItemType;
import otmui.utils.Arrow;
import otmui.utils.Vector;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import sensor.AbstractSensor;

public class FixedSensor extends AbstractGraphItem {

    public sensor.FixedSensor sensor;
    public Arrow geom;

    public FixedSensor(AbstractSensor sensor, float lane_width, float link_offset) throws OTMException {

        super(sensor.getId(), Float.NaN, Float.NaN, Color.DODGERBLUE, Color.RED);

        if(!(sensor instanceof sensor.FixedSensor))
            return;

        this.sensor = (sensor.FixedSensor) sensor;

        this.geom = traverse_distance(this.sensor.get_link(),this.sensor.get_position());

        if(geom==null)
            return;

        float sensor_length = 2f;

        Vector p = geom.start;
        Vector u = geom.direction;

        int start_lane = this.sensor.start_lane;
        int end_lane = this.sensor.end_lane;

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
        setShape(polygon);

        unhighlight();
    }

    @Override
    public ItemType getType() {
        return ItemType.sensor;
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

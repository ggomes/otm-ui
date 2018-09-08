package otmui.model;

import otmui.graph.item.DrawSensor;
import otmui.utils.Point;
import otmui.utils.PointOrientation;
import otmui.utils.Vector;
import error.OTMException;

public class Sensor {

    public sensor.SensorLoopDetector bsensor;
    public DrawSensor drawSensor;
    public PointOrientation geom;

    public Sensor(sensor.SensorLoopDetector bsensor) throws OTMException{
        this.bsensor = bsensor;
        this.geom = traverse_distance(bsensor.link,bsensor.position);
    }

    public Long get_id(){
        return bsensor.id;
    }


    /** Starting from upstream node, traverse the link a distance x and return the resulting point.
     * Return null if x<0 or x> length.
     */
    private static PointOrientation traverse_distance(common.Link link,float x) throws OTMException {

        int seg_ind = 0;
        Point current_point = new Point( link.shape.get(seg_ind) );
        Point next_point = new Point( link.shape.get(seg_ind+1) );
        Vector u = new Vector(current_point,next_point);
        float segment_length = Vector.norm(u);
        float remaining_distance = x;

        PointOrientation P = null;
        while(remaining_distance>0){

            if(remaining_distance>segment_length){
                    // point is not in this segment

                remaining_distance -= segment_length;

                seg_ind += 1;

                if( seg_ind >= link.shape.size() )
                    throw new OTMException("Point not within the link");

                current_point = new Point(link.shape.get(seg_ind) );
                next_point = new Point(link.shape.get(seg_ind+1) );
                u = new Vector(current_point,next_point);
                segment_length = Vector.norm(u);

            } else{   // point is in this segment
                Vector unorm = Vector.normalize(u);
                P = new PointOrientation( Vector.sum(current_point,Vector.mult(unorm,remaining_distance)) ,
                                          unorm );
                remaining_distance = -1f;
            }
        }

        return P;
    }

}

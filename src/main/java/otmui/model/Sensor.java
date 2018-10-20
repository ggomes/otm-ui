/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.model;

import otmui.graph.item.DrawSensor;
import otmui.utils.Arrow;
import otmui.utils.Vector;
import error.OTMException;

public class Sensor {

    public sensor.FixedSensor bsensor;
    public DrawSensor drawSensor;
    public Arrow geom;

    public Sensor(sensor.FixedSensor bsensor) throws OTMException{
        this.bsensor = bsensor;
        this.geom = traverse_distance(bsensor.get_link(),bsensor.get_position());
    }

    /** Starting from upstream node, traverse the link a distance x and return the resulting point.
     * Return null if x<0 or x> length.
     */
    private static Arrow traverse_distance(common.Link link,float x) throws OTMException {

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

    public Long get_id(){
        return bsensor.id;
    }

}

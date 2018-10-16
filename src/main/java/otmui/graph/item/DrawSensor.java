/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.graph.item;

import otmui.utils.Vector;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class DrawSensor extends Group {

    public otmui.model.Sensor sensor;
    public Long id;
    public Shape shape;

    protected static Color color1 = Color.DODGERBLUE;
    protected static Color color2 = Color.RED;

    public DrawSensor() {
        this.sensor = null;
        this.id = null;
    }

    public DrawSensor(otmui.model.Sensor sensor, float lane_width, float link_offset) {
        this.sensor = sensor;
        this.id = sensor.bsensor.id;

        if(sensor.geom==null)
            return;

        float sensor_length = 2f;

        Vector p = sensor.geom.start;
        Vector u = sensor.geom.direction;

        int start_lane = sensor.bsensor.start_lane;
        int end_lane = sensor.bsensor.end_lane;

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
        return (double) sensor.geom.start.x;
    }

    public double getYPos(){
        return (double) sensor.geom.start.y;
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

}

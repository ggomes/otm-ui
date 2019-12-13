package otmui.item;

import actuator.AbstractActuator;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class StopSign extends Actuator {

    public StopSign(AbstractActuator act, Float xpos, Float ypos, float r, double stroke_width) {
        super(act, xpos, ypos, Color.DODGERBLUE, Color.RED);


        this.xpos -= r;
        this.ypos -= r;

        Polygon octagon = new Polygon();

        double rsin22p5 = r*Math.sin(22.5*Math.PI/180d);
        double rcos22p5 = r*Math.cos(22.5*Math.PI/180d);
        double rsin67p5 = r*Math.sin(67.5*Math.PI/180d);
        double rcos67p5 = r*Math.cos(67.5*Math.PI/180d);

        octagon.getPoints().addAll(new Double[]{
                rcos22p5, rsin22p5,
                rcos67p5, rsin67p5,
                -rcos67p5, rsin67p5,
                -rcos22p5, rsin22p5,
                -rcos22p5, -rsin22p5,
                -rcos67p5, -rsin67p5,
                rcos67p5, -rsin67p5,
                rcos22p5, -rsin22p5 });

        octagon.setStrokeWidth(stroke_width);

        System.out.println("pkerb-93");
//        setView(octagon);
        unhighlight();
    }

//    @Override
//    public void set_size(float r) {
//        Polygon octagon = (Polygon) shape;
//
//        double rsin22p5 = r*Math.sin(22.5*Math.PI/180d);
//        double rcos22p5 = r*Math.cos(22.5*Math.PI/180d);
//        double rsin67p5 = r*Math.sin(67.5*Math.PI/180d);
//        double rcos67p5 = r*Math.cos(67.5*Math.PI/180d);
//
//        octagon.getPoints().clear();
//        octagon.getPoints().addAll(new Double[]{
//                rcos22p5, rsin22p5,
//                rcos67p5, rsin67p5,
//                -rcos67p5, rsin67p5,
//                -rcos22p5, rsin22p5,
//                -rcos22p5, -rsin22p5,
//                -rcos67p5, -rsin67p5,
//                rcos67p5, -rsin67p5,
//                rcos22p5, -rsin22p5 });
//    }

}

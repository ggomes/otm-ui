package otmui.item;

import actuator.AbstractActuator;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Signal extends Actuator {

    public Signal(AbstractActuator act, Float xpos, Float ypos, float node_size, double stroke_width) {
        super(act, xpos, ypos, Color.LIGHTGREEN, Color.LAWNGREEN);

        this.xpos -= node_size;
        this.ypos -= node_size;

        Double [] points = getPoints(node_size);
        for(int i=0;i<points.length;i+=2){
            points[i] += this.xpos;
            points[i+1] += this.ypos;
        }

        Polygon octagon = new Polygon();
        octagon.getPoints().addAll(points);
        octagon.setStrokeWidth(stroke_width);

        setShape(octagon);
        unhighlight();
    }

    @Override
    public void set_size(float node_size) {
        Polygon octagon = (Polygon) shapegroup.iterator().next();
        octagon.getPoints().clear();
        octagon.getPoints().addAll(getPoints(node_size));
    }

    @Override
    public void relocate(float nxpos, float nypos) {
        double dx = nxpos - this.xpos;
        double dy = nypos - this.ypos;
        Polygon octagon = (Polygon) shapegroup.iterator().next();
        ObservableList<Double> points = octagon.getPoints();
        for(int i=0;i<points.size();i+=2){
            points.set(i,points.get(i)+dx);
            points.set(i+1,points.get(i+1)+dy);
        }
        this.xpos = nxpos;
        this.ypos = nypos;
    }

    private Double [] getPoints(float node_size){
        double r = 0.7*node_size;
        double rsin22p5 = r*Math.sin(22.5*Math.PI/180d);
        double rcos22p5 = r*Math.cos(22.5*Math.PI/180d);
        double rsin67p5 = r*Math.sin(67.5*Math.PI/180d);
        double rcos67p5 = r*Math.cos(67.5*Math.PI/180d);
        return new Double[]{
                rcos22p5, rsin22p5,
                rcos67p5, rsin67p5,
                -rcos67p5, rsin67p5,
                -rcos22p5, rsin22p5,
                -rcos22p5, -rsin22p5,
                -rcos67p5, -rsin67p5,
                rcos67p5, -rsin67p5,
                rcos22p5, -rsin22p5 };
    }
}

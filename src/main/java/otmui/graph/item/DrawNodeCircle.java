package otmui.graph.item;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class DrawNodeCircle extends AbstractDrawNode {

    public DrawNodeCircle(Long id, Float xpos, Float ypos, float node_size, Paint fill, double stroke_width) {
        super( id,xpos,ypos);

        this.xpos -= node_size;
        this.ypos -= node_size;

        Circle circle = new Circle(node_size);
        circle.setStrokeWidth(stroke_width);
        circle.setFill(fill);
        setView(circle);

    }

    @Override
    public void set_size(float mysize) {
        ((Circle) shape).setRadius(mysize);
    }
}

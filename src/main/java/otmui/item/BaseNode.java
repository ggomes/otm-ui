package otmui.item;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class BaseNode extends AbstractPointItem {

    public BaseNode(long id, float xpos, float ypos, float node_size, Paint fill, double stroke_width) {
        super(id,xpos,ypos);
        color1 = Color.DODGERBLUE;
        color2 = Color.RED;

        this.xpos -= node_size;
        this.ypos -= node_size;

        Circle circle = new Circle(node_size);
        circle.setStrokeWidth(stroke_width);
        circle.setFill(fill);
        setView(circle);

    }

    @Override
    public String getPrefix() {
        return "node";
    }

    @Override
    public String getName() {
        return String.format("node %d",id);
    }

    @Override
    public void set_size(float mysize) {
        ((Circle) shape).setRadius(mysize);
    }

}
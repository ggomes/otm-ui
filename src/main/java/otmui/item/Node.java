package otmui.item;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import otmui.ItemType;

public class Node extends AbstractGraphItem {

    public common.Node node;

    public Node(common.Node node, float xpos, float ypos, float node_size, Paint fill, double stroke_width) {
        super(node.getId(), Float.NaN, Float.NaN, Color.DODGERBLUE, Color.RED);

        this.node = node;
        this.xpos -= node_size;
        this.ypos -= node_size;

        Circle circle = new Circle(node_size);
        circle.setStrokeWidth(stroke_width);
        circle.setFill(fill);
        setShape(circle);

    }

    @Override
    public ItemType getType() {
        return ItemType.node;
    }


    @Override
    public String getName() {
        return String.format("node %d",id);
    }

//    @Override
//    public void set_size(float mysize) {
//        ((Circle) shape).setRadius(mysize);
//    }

}
package otmui.graph.item;

import javafx.scene.shape.Circle;

public class DrawNodeOctagon extends AbstractDrawNode  {

    public DrawNodeOctagon(Long id, Float xpos, Float ypos,float node_size) {
        super( id,xpos,ypos);

        this.xpos -= node_size;
        this.ypos -= node_size;

        Circle circle = new Circle(node_size);
        circle.setStrokeWidth(0d);
        circle.setFill(color1);
        setView(circle);

    }

    @Override
    public void set_size(float mysize) {
        ((Circle) shape).setRadius(mysize);
    }
}

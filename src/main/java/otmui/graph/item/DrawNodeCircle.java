package otmui.graph.item;

import javafx.scene.shape.Circle;

public class DrawNodeCircle extends AbstractDrawNode {

    public DrawNodeCircle(Long id, Float xpos, Float ypos,float node_radius) {
        super( id,xpos,ypos);

        Circle circle = new Circle(node_radius);
        circle.setStrokeWidth(0d);
        circle.setFill(color1);
        setView(circle);

    }

}

package otmui.item;

import javafx.scene.Group;
import javafx.scene.paint.Color;

import javafx.scene.shape.Shape;

import java.util.Set;


public abstract class AbstractGraphItem extends AbstractItem {

    public Group shapegroup;
    public Color color1;
    public Color color2;
    public float xpos;
    public float ypos;

    public AbstractGraphItem(long id, float xpos, float ypos, Color color1, Color color2) {
        super(id);
        this.shapegroup = new Group();
        this.xpos = xpos;
        this.ypos = ypos;
        this.color1 = color1;
        this.color2 = color2;
    }

    public void highlight() {
        if(shapegroup!=null)
            shapegroup.getChildren().forEach(x->((Shape)x).setFill(color2));
    }

    public void unhighlight() {
        if(shapegroup!=null)
            shapegroup.getChildren().forEach(x->((Shape)x).setFill(color1));
    }

    public void set_visible(boolean visible){
        this.shapegroup.setVisible(visible);
    }

    public void setShape(Shape shape){
        shapegroup.getChildren().clear();
        shapegroup.getChildren().add(shape);
        shape.relocate(xpos,ypos);
    }

    public void addShapes(Set<Shape> shapes) {
        shapegroup.getChildren().addAll(shapes);
    }

}

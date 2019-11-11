package otmui.graph.item;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public abstract class AbstractDrawNode extends Group {

    public enum DrawNodeShape { RECTANGLE, CIRCLE, OCTAGON }

    protected static Color color1 = Color.DODGERBLUE;
    protected static Color color2 = Color.RED;

    public Long id;
    public Shape shape;
    public Double xpos;
    public Double ypos;

    public AbstractDrawNode(Long _id, Float _xpos, Float _ypos) {
        this.id = _id==null ? 0 : _id;
        this.xpos = _xpos==null ? 0d : _xpos.doubleValue();
        this.ypos = _ypos==null ? 0d : _ypos.doubleValue();
    }

    public abstract void set_size(float mysize);

    public void set_visible(boolean visible){
        this.shape.setVisible(visible);
    }

    public void setView(Shape shape) {
        this.shape = shape;
        getChildren().add(shape);
        relocate(xpos,ypos);
    }

    public Double getXPos(){
        return xpos;
    }

    public Double getYPos(){
        return ypos;
    }

    public void highlight() {
        shape.setFill(color2);
    }

    public void unhighlight() {
        shape.setFill(color1);
    }

}
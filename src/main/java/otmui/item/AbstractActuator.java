package otmui.item;

import javafx.scene.shape.Shape;

public abstract class AbstractActuator extends AbstractItem {

    public Shape shape;
    public Double xpos;
    public Double ypos;

    public AbstractActuator(Long _id, Float _xpos, Float _ypos) {
        this.id = _id==null ? 0 : _id;
        this.xpos = _xpos==null ? 0d : _xpos.doubleValue();
        this.ypos = _ypos==null ? 0d : _ypos.doubleValue();
    }

    public abstract void set_size(float r);

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


}

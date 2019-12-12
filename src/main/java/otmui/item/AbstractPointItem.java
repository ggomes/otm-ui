package otmui.item;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public abstract class AbstractPointItem extends AbstractItem {

    public Shape shape;
    public float xpos;
    public float ypos;
    public Color color1;
    public Color color2;

    public AbstractPointItem(){};

    public AbstractPointItem(long id,Float _xpos, Float _ypos) {
        super(id);
        this.xpos = _xpos==null ? 0f : _xpos.floatValue();
        this.ypos = _ypos==null ? 0f : _ypos.floatValue();
    }

    /////////////////////////////////////
    // abstract methods
    /////////////////////////////////////

    public abstract void set_size(float mysize);

    /////////////////////////////////////
    // implemented methods
    /////////////////////////////////////

    @Override
    public void highlight() {
        if(shape!=null)
            shape.setFill(color2);
    }

    @Override
    public void unhighlight() {
        if(shape!=null)
            shape.setFill(color1);
    }

    public void set_visible(boolean visible){
        this.shape.setVisible(visible);
    }

    public void setView(Shape shape) {
        this.shape = shape;
        getChildren().add(shape);
        relocate(xpos,ypos);
    }

    // TODO WHAT IS THE DIFFERENCE BETWEEN THIS AND THE ABOVE?
//    public void setView(Shape shape) {
//        this.shape = shape;
//        getChildren().clear();
//        getChildren().add(shape);
//    }

    public float getXPos(){
        return xpos;
    }

    public float getYPos(){
        return ypos;
    }
}

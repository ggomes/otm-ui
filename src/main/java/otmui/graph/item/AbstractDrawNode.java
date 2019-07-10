/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.graph.item;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import otmui.GlobalParameters;

public abstract class AbstractDrawNode extends Group {

    public enum DrawNodeShape { RECTANGLE, CIRCLE }

    protected static Color color1 = Color.DODGERBLUE;
    protected static Color color2 = Color.RED;

    public Long id;
    public Shape shape;
    public Double xpos;
    public Double ypos;

    public AbstractDrawNode(Long _id, Float _xpos, Float _ypos) {
        this.id = _id==null ? 0 : _id;
        this.xpos = _xpos==null ? 0d : new Double(_xpos );
        this.ypos = _ypos==null ? 0d : new Double(_ypos );
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

    public void paint(GlobalParameters params) {
        this.setVisible(params.view_nodes.getValue());
    }

}
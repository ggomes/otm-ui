/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.graph.item;

import javafx.scene.shape.Circle;

public class DrawNodeCircle extends AbstractDrawNode {

    public DrawNodeCircle(Long id, Float xpos, Float ypos,float node_size) {
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

/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.graph.item;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class DrawNodeRectangle extends AbstractDrawNode {

    public DrawNodeRectangle(Long id, Float xpos, Float ypos, float size) {
        super( id,xpos,ypos);
        Rectangle view = new Rectangle( size,size);
        view.setStroke(Color.DODGERBLUE);
        view.setFill(Color.DODGERBLUE);
        setView( view);
    }

    @Override
    public void set_size(float mysize) {
        ((Rectangle)shape).setHeight(mysize);
        ((Rectangle)shape).setWidth(mysize);
    }

}
package otmui.graph.item;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class DrawNodeRectangle extends AbstractDrawNode {

    public DrawNodeRectangle(Long id, Float xpos, Float ypos) {
        super( id,xpos,ypos);
        Rectangle view = new Rectangle( 50,50);
        view.setStroke(Color.DODGERBLUE);
        view.setFill(Color.DODGERBLUE);
        setView( view);
    }

}
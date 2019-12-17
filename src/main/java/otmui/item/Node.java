package otmui.item;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import keys.KeyCommodityLink;
import otmui.GlobalParameters;
import otmui.ItemType;
import profiles.SplitMatrixProfile;

import java.util.Map;

public class Node extends AbstractGraphItem {

    public common.Node node;
    public Map<KeyCommodityLink, SplitMatrixProfile> splits = null;

    public Node(common.Node node, GlobalParameters params) {
        super(node.getId(), node.xcoord,-node.ycoord, Color.GRAY, Color.GOLD);

        float node_size = params.node_size.floatValue();
        this.node = node;
        this.xpos -= node_size;
        this.ypos -= node_size;

        Circle circle = new Circle(node_size);
        circle.setFill(color1);
        setShape(circle);
    }

    @Override
    public ItemType getType() {
        return ItemType.node;
    }

    @Override
    public void set_size(float node_size) {
        this.xpos = node.xcoord - node_size;
        this.ypos = -node.ycoord - node_size;
        Circle circle = (Circle) this.shapegroup.iterator().next();
        circle.setRadius(node_size);
    }

}
package otmui.graph.item;

import otmui.graph.color.AbstractColormap;
import otmui.model.Link;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import output.animation.AbstractLaneGroupInfo;

import java.util.List;

public interface InterfaceDrawLanegroup {

    void unhighlight();
    void highlight(Color color);
    void set_temporary_color(Color color);
    List<Polygon> make_polygons(Link link, float lane_width, float link_offset, AbstractColormap colormap);

    void draw_state(AbstractLaneGroupInfo laneGroupInfo, AbstractColormap colormap);
}

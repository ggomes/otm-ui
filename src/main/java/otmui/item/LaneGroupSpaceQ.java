package otmui.item;

import models.BaseLaneGroup;
import otmui.graph.color.AbstractColormap;
import error.OTMException;
import javafx.scene.paint.Color;
import otmui.utils.Arrow;
import output.animation.AbstractLaneGroupInfo;

import java.util.List;

public class LaneGroupSpaceQ extends LaneGroup {

    public LaneGroupSpaceQ(otmui.item.Link link,BaseLaneGroup alg, List<Arrow> midline, float lateral_offset, float long_offset, double lane_width, double road2euclid, Color color) throws OTMException {
        super(link,alg,lateral_offset);
        cells.add(new Cell(midline,0,lateral_offset,alg.length * road2euclid,alg.num_lanes *lane_width,color));
    }

    @Override
    public void unhighlight() {
        cells.get(0).unhighlight();
    }

    @Override
    public void highlight(Color color) {
        cells.get(0).highlight(color);
    }

    @Override
    public void set_temporary_color(Color color) {
        cells.get(0).set_temporary_color(color);
    }

    @Override
    public void draw_state(AbstractLaneGroupInfo laneGroupInfo, AbstractColormap colormap) {
        System.err.println("IMplement this");
    }

}

package otmui.graph.item;

import models.AbstractLaneGroup;
import otmui.graph.color.AbstractColormap;
import error.OTMException;
import javafx.scene.paint.Color;
import otmui.utils.Arrow;
import output.animation.AbstractLaneGroupInfo;

import java.util.List;

public class DrawLanegroupPQ extends AbstractDrawLanegroup  {


    public DrawLanegroupPQ(AbstractLaneGroup alg, List<Arrow> midline, float lateral_offset, float long_offset, double lane_width, double road2euclid, AbstractColormap colormap) throws OTMException {
        super(alg,lateral_offset);
        draw_cells.add(new DrawCell(midline,0,lateral_offset,alg.length * road2euclid,alg.num_lanes *lane_width,colormap));
    }

    @Override
    public void unhighlight() {
        draw_cells.get(0).unhighlight();
    }

    @Override
    public void highlight(Color color) {
        draw_cells.get(0).highlight(color);
    }

    @Override
    public void set_temporary_color(Color color) {
        draw_cells.get(0).set_temporary_color(color);
    }

    @Override
    public void draw_state(AbstractLaneGroupInfo laneGroupInfo, AbstractColormap colormap) {
        System.err.println("IMplement this");
    }

}

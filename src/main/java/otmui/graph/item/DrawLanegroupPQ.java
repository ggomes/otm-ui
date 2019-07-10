package otmui.graph.item;

import models.AbstractLaneGroup;
import otmui.graph.color.AbstractColormap;
import error.OTMException;
import javafx.scene.paint.Color;
import output.animation.AbstractLaneGroupInfo;

public class DrawLanegroupPQ extends AbstractDrawLanegroup  {

    public DrawCell cell;

    public DrawLanegroupPQ(AbstractLaneGroup meso_lg, float lateral_offset,double road2euclid) throws OTMException {
        super(meso_lg,lateral_offset);
//        cell = create_drawcell(meso_lg.length * road2euclid, segments);
    }

    @Override
    public void unhighlight() {
        cell.unhighlight();
    }

    @Override
    public void highlight(Color color) {
        cell.highlight(color);
    }

    @Override
    public void set_temporary_color(Color color) {
        cell.set_temporary_color(color);
    }

    @Override
    public void draw_state(AbstractLaneGroupInfo laneGroupInfo, AbstractColormap colormap) {
        System.err.println("IMplement this");
    }

}

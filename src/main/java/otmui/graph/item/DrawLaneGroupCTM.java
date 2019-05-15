package otmui.graph.item;

import error.OTMException;
import javafx.scene.paint.Color;
import models.AbstractLaneGroup;
import otmui.graph.color.AbstractColormap;
import otmui.graph.color.RGB;
import otmui.utils.Arrow;
import output.animation.AbstractLaneGroupInfo;
import output.animation.macro.CellInfo;

import java.util.ArrayList;
import java.util.List;

public class DrawLaneGroupCTM extends AbstractDrawLanegroup  {

    public double max_vehicles_per_cell;
    public double euclid_cell_length_m;
    public double road_cell_length_km;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public DrawLaneGroupCTM(AbstractLaneGroup alg, List<Arrow> midline, double lateral_offset, double long_offset, double lane_width, double road2euclid, AbstractColormap colormap) throws OTMException {

        super(alg,lateral_offset);

        models.ctm.LaneGroup lg = (models.ctm.LaneGroup) alg;

        this.max_vehicles_per_cell = lg.max_vehicles / lg.cells.size();

        draw_cells = new ArrayList<>();

        // length of a cell
        road_cell_length_km = lg.length / lg.cells.size() / 1000f;
        euclid_cell_length_m = road_cell_length_km * road2euclid * 1000f;
        double width = alg.num_lanes *lane_width;

        int start_ind = find_closest_arrow_index_to(midline,long_offset);

        // traverse cells
        for (int i = 0; i < lg.cells.size(); i++) {
            DrawCell draw_cell = new DrawCell(midline,start_ind,lateral_offset,euclid_cell_length_m,width,colormap);
            draw_cells.add(draw_cell);
            start_ind += draw_cell.arrows.size()-1;
        }

    }

    /////////////////////////////////////////////////
    // highlighting
    /////////////////////////////////////////////////

    @Override
    public void unhighlight() {
        draw_cells.forEach(x->x.unhighlight());
    }

    @Override
    public void highlight(Color color) {
        draw_cells.forEach(x->x.highlight(color));
    }

    /////////////////////////////////////////////////
    // coloring
    /////////////////////////////////////////////////

    @Override
    public void set_temporary_color(Color color) {
        draw_cells.forEach(x->x.set_temporary_color(color));
    }

    @Override
    public void draw_state(AbstractLaneGroupInfo laneGroupInfo, AbstractColormap colormap) {
        draw_state_internal(
                laneGroupInfo,
                colormap,
                (float) max_vehicles_per_cell );
    }

    protected void draw_state_internal(AbstractLaneGroupInfo laneGroupInfo, AbstractColormap colormap,float max_vehicles){
        output.animation.macro.LaneGroupInfo lg_info = (output.animation.macro.LaneGroupInfo) laneGroupInfo;
        for(int i=0;i<lg_info.cell_info.size();i++){
            CellInfo cellinfo = lg_info.cell_info.get(i);
            DrawCell drawCell = draw_cells.get(i);
            double veh = cellinfo.get_total_vehicles();
            RGB rgb = colormap.get_color(veh,max_vehicles);
            Color color = new Color(rgb.red,rgb.green,rgb.blue,1.0);
            drawCell.set_temporary_color(color);
        }
    }

}

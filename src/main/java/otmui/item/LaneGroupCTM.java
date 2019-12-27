package otmui.item;

import error.OTMException;
import javafx.scene.paint.Color;
import models.BaseLaneGroup;
import otmui.graph.color.AbstractColormap;
import otmui.graph.color.RGB;
import otmui.utils.Arrow;
import output.animation.AbstractLaneGroupInfo;
import output.animation.macro.CellInfo;

import java.util.List;

public class LaneGroupCTM extends LaneGroup {

    public double max_vehicles_per_cell;
    public double euclid_cell_length_m;
    public double road_cell_length_km;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public LaneGroupCTM(otmui.item.Link link,BaseLaneGroup alg, List<Arrow> midline, float lateral_offset, float long_offset, double lane_width, double road2euclid, Color color) throws OTMException {

        super(link,alg,lateral_offset);

        models.ctm.LaneGroup lg = (models.ctm.LaneGroup) alg;

        this.max_vehicles_per_cell = lg.max_vehicles / lg.cells.size();

        // length of a cell
        road_cell_length_km = lg.length / lg.cells.size() / 1000f;
        euclid_cell_length_m = road_cell_length_km * road2euclid * 1000f;
        double width = alg.num_lanes *lane_width;

        int start_ind = find_closest_arrow_index_to(midline,long_offset);

        // traverse cells
        for (int i = 0; i < lg.cells.size(); i++) {
            Cell draw_cell = new Cell(midline,start_ind,lateral_offset,euclid_cell_length_m,width,color);
            cells.add(draw_cell);
            start_ind += draw_cell.arrows.size()-1;
        }

    }

    /////////////////////////////////////////////////
    // highlighting
    /////////////////////////////////////////////////

    @Override
    public void unhighlight() {
        cells.forEach(x->x.unhighlight());
    }

    @Override
    public void highlight(Color color) {
        cells.forEach(x->x.highlight(color));
    }

    /////////////////////////////////////////////////
    // coloring
    /////////////////////////////////////////////////

    @Override
    public void set_temporary_color(Color color) {
        cells.forEach(x->x.set_temporary_color(color));
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
            Cell drawCell = cells.get(i);
            double veh = cellinfo.get_total_vehicles();
            RGB rgb = colormap.get_color(veh,max_vehicles);
            Color color = new Color(rgb.red,rgb.green,rgb.blue,1.0);
            drawCell.set_temporary_color(color);
        }
    }

}

package otmui.item;

import javafx.scene.shape.Shape;
import models.BaseLaneGroup;
import javafx.scene.paint.Color;
import otmui.GlobalParameters;
import otmui.graph.color.AbstractColormap;
import otmui.utils.Arrow;
import output.animation.AbstractLaneGroupInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Comparator.comparingDouble;
import static java.util.stream.Collectors.toList;

public abstract class LaneGroup {

    public Long id;
    public otmui.item.Link link;
    public int lanes;
    public float lateral_offset;
    public List<Cell> cells = new ArrayList<>();

    abstract public void unhighlight();
    abstract public void highlight(Color color);
    abstract public void set_temporary_color(Color color);
    abstract public void draw_state(AbstractLaneGroupInfo laneGroupInfo, AbstractColormap colormap);

    public LaneGroup(otmui.item.Link link,BaseLaneGroup lg, float lateral_offset) {
        this.link = link;
        this.lanes = lg.num_lanes;
        this.id = lg.id;
        this.lateral_offset = lateral_offset;
    }

    public void paintShape(float link_offset, float lane_width, Color color){
        cells.forEach(x->x.paintShape(link_offset+lateral_offset,lanes*lane_width,color));
    }

    public void paintColor(GlobalParameters.RoadColorScheme road_color_scheme){
        if(road_color_scheme.equals(GlobalParameters.RoadColorScheme.Cells)){
            for(Cell cell : cells)
                cell.paintColor(AbstractColormap.get_color(road_color_scheme,link));
        } else {
            Color color = AbstractColormap.get_color(road_color_scheme,link);
            cells.forEach(x->x.paintColor(color));
        }
    }

    public Set<Shape> get_polygons(){
        return cells.stream().map(x->x.polygon).collect(Collectors.toSet());
    }

    public static int find_closest_arrow_index_to(List<Arrow> midline, double p){
        List<Double> a = midline.stream().map(x->Math.abs(x.position-p)).collect(toList());
        return IntStream.range(0,a.size()).boxed()
                .min(comparingDouble(a::get))
                .get();
    }
}

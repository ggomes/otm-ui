package otmui.item;

import geometry.Side;
import javafx.scene.Group;
import otmui.GlobalParameters;
import otmui.ItemType;
import otmui.graph.color.AbstractColormap;
import models.BaseLaneGroup;
import error.OTMException;
import javafx.scene.paint.Color;
import otmui.utils.Arrow;
import otmui.utils.Vector;
import profiles.AbstractDemandProfile;

import java.util.*;

public abstract class Link extends AbstractGraphItem {

    public static float epsilon = 0.5f; // meters
    public common.Link link;
    public Map<Long, AbstractDemandProfile> comm2demand;

    public List<LaneGroup> draw_lanegroups;
    public double max_vehicles;

    abstract List<Double> get_additional_midline_points();

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public Link(common.Link link, float lane_width, float link_offset, GlobalParameters.RoadColorScheme roadColorScheme) throws OTMException {
        super(link.getId(), Float.NaN, Float.NaN, Color.DODGERBLUE, Color.RED);

        this.link = link;
        this.comm2demand = new HashMap<>();
        this.max_vehicles = link.get_max_vehicles();
        this.draw_lanegroups = new ArrayList<>();

        // case for mn, where get_max_vehicles returns infinity
        if(Double.isInfinite(this.max_vehicles))
            this.max_vehicles = (float) (link.length * link.full_lanes * (180.0 / 1600.0));

        // Draw the road ....................................................
        List<Vector> shape = new ArrayList<>();
        for(common.Point p : link.shape)
            shape.add(new Vector(p.x,p.y));

        // traverse the points from downstream to upstream. Create Segments
        if(shape.size()<2)
            throw new OTMException("Not enough points for link " + link.getId());

        // midline with transversal arrows
        List<Arrow> midline = new ArrayList<>();
        double position = 0;
        for (int i=0;i<shape.size();i++) {
            if(i>0)
                position += Vector.length(Vector.diff(shape.get(i) , shape.get(i-1) ));
            midline.add(new Arrow(position,shape.get(i), null));
        }

        // midline perpendicular directions
        Vector u = new Vector(midline.get(0).start,midline.get(1).start);
        midline.get(0).direction = Vector.normalize(Vector.cross_z(u));
        for(int i=1;i<midline.size()-1;i++) {
            Vector u_this = Vector.normalize(Vector.diff(midline.get(i + 1).start, midline.get(i).start));
            Vector u_prev = Vector.normalize(Vector.diff(midline.get(i).start, midline.get(i-1).start));

            Vector du = Vector.diff(u_this,u_prev);
            if(du.x==0d && du.y==0d)
               midline.get(i).direction = Vector.cross_z(u_this);
            else
                midline.get(i).direction = Vector.normalize(du);

            // needed to avoid switching around 0, which makes a strange shape
            if (Vector.dot(midline.get(i).direction,midline.get(i-1).direction)<0)
                midline.get(i).direction = Vector.mult(midline.get(i).direction,-1f);
        }

        u = new Vector(midline.get(midline.size()-2).start,midline.get(midline.size()-1).start);
        midline.get(midline.size()-1).direction = Vector.normalize(Vector.cross_z(u));

        // get additional midline points for this model
        List<Double> add_points = get_additional_midline_points();  // TODO FIX THIS

        // add additional points
        List<Arrow> add_midline = new ArrayList<>();
        for(Double p : add_points) {
            Arrow prev=null;
            Arrow next=null;
            for(int i=1;i<midline.size();i++){
                if(midline.get(i).position>p){
                    prev = midline.get(i-1);
                    next = midline.get(i);
                    break;
                }
            }

            if(prev!=null && next!=null){
                double xi = (p-prev.position)/(next.position-prev.position);
                Vector start = Vector.linear_combination(prev.start,next.start,(float)xi);
                Vector direction = Vector.linear_combination(prev.direction,next.direction,(float)xi);
                add_midline.add(new Arrow(p, start, direction));
            } else {
                throw new OTMException("ppoiwr-8 rjbenrb -8");
            }
        }

        if(!add_midline.isEmpty())
            midline.addAll(add_midline);

        // sort midline
        Collections.sort(midline);

        // distance to next
        for(int i=0;i<midline.size()-1;i++)
            midline.get(i).distance_to_next = midline.get(i+1).position - midline.get(i).position;

        // TODO: remove points that are too close together (within epsilon)

        // road2euclid
        double euclid_segment_length = midline.get(midline.size()-1).position-midline.get(0).position;
        double road_segment_length = link.length;
        double road2euclid = euclid_segment_length / road_segment_length;

        // populate draw_lanegroups
        Color color = AbstractColormap.get_color_for_roadtype(roadColorScheme,link.road_type);
        for (BaseLaneGroup lg : link.lanegroups_flwdn.values()) {

            // offsets of the upstream inner corner
            float lateral_offset = lane_width*(lg.start_lane_dn-1);
            float long_offset = lg.side== Side.middle ? 0 : link.length-lg.length;

            LaneGroup draw_lg = create_draw_lanegroup(lg,midline,lateral_offset,long_offset,lane_width, road2euclid,color);
            draw_lanegroups.add(draw_lg);
            addShapes(draw_lg.get_polygons());
        }

//        if(link.bLink.lanegroup_flwside_in!=null)
//            draw_lanegroups.add(create_draw_lanegroup(link.bLink.lanegroup_flwside_in,segments,road2euclid));
//
//        if(link.bLink.lanegroup_flwside_out!=null)
//            draw_lanegroups.add(create_draw_lanegroup(link.bLink.lanegroup_flwside_out,segments,road2euclid));

//        // make the polygons
//        for (AbstractDrawLanegroup draw_lanegroup : draw_lanegroups) {
//            List<Polygon> polygons = draw_lanegroup.make_polygons(link, lane_width_meters, link_offset, colormap);
//            getChildren().addAll(polygons);
//        }

    }

    abstract LaneGroup create_draw_lanegroup(BaseLaneGroup lg, List<Arrow> midline, float lateral_offset, float long_offset, double lane_width, double road2euclid, Color color) throws OTMException ;

    @Override
    public ItemType getType() {
        return ItemType.link;
    }

    @Override
    public String getName() {
        return String.format("link %d",id);
    }

    public void add_demand(long commodity_id,AbstractDemandProfile profile){
        comm2demand.put(commodity_id,profile);
    }

    /////////////////////////////////////////////////
    // paint
    /////////////////////////////////////////////////

    public final void paintShape(float link_offset, float lane_width, GlobalParameters.RoadColorScheme road_color_scheme){
        Color color = AbstractColormap.get_color_for_roadtype(road_color_scheme,link.road_type);
        draw_lanegroups.forEach(x -> x.paintShape(link_offset,lane_width,color));
    }

    public final void paintColor(GlobalParameters.RoadColorScheme road_color_scheme){
        Color color = AbstractColormap.get_color_for_roadtype(road_color_scheme,link.road_type);
        draw_lanegroups.forEach(x -> x.paintColor(color));
    }

    @Override
    public void highlight() {
        draw_lanegroups.forEach(x -> x.highlight(color2));
    }

    @Override
    public void unhighlight() {
        draw_lanegroups.forEach(x -> x.unhighlight());
    }

}
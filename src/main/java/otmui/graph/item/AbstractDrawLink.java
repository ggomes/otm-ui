package otmui.graph.item;

import geometry.Side;
import otmui.GlobalParameters;
import otmui.graph.color.AbstractColormap;
import models.AbstractLaneGroup;
import error.OTMException;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import otmui.utils.Arrow;
import otmui.utils.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractDrawLink extends Group {

    public static float epsilon = 0.5f; // meters
    protected static Color color_highlight = Color.RED;

    public otmui.model.Link link;
    public Long id;
    public List<AbstractDrawLanegroup> draw_lanegroups;
    public AbstractDrawNode startNode;
    public AbstractDrawNode endNode;
    public double max_vehicles;

    abstract List<Double> get_additional_midline_points(double road2euclid);

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public AbstractDrawLink(otmui.model.Link link, AbstractDrawNode startNode, AbstractDrawNode endNode, float lane_width, float link_offset, AbstractColormap colormap) throws OTMException {
        this.link = link;
        this.id = link.getId();
        this.startNode = startNode;
        this.endNode = endNode;
        this.max_vehicles = link.bLink.get_max_vehicles();
        this.draw_lanegroups = new ArrayList<>();

        // case for mn, where get_max_vehicles returns infinity
        if(Double.isInfinite(this.max_vehicles))
            this.max_vehicles = (float) (link.bLink.length * link.bLink.full_lanes * (180.0 / 1600.0));

        // Draw the road ....................................................
        List<Vector> shape = link.getShape();

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

            midline.get(i).direction = Vector.normalize(Vector.diff(u_this,u_prev));

            // needed to avoid switching around 0, which makes a strange shape
            if (Vector.dot(midline.get(i).direction,midline.get(i-1).direction)<0)
                midline.get(i).direction = Vector.mult(midline.get(i).direction,-1f);

        }

        u = new Vector(midline.get(midline.size()-2).start,midline.get(midline.size()-1).start);
        midline.get(midline.size()-1).direction = Vector.normalize(Vector.cross_z(u));


        // get additional midline points for this model
        List<Double> add_points = get_additional_midline_points(1d);  // TODO FIX THIS

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
        double road_segment_length = link.bLink.length;
        double road2euclid = euclid_segment_length / road_segment_length;

        // populate draw_lanegroups
        for (AbstractLaneGroup lg : link.bLink.lanegroups_flwdn.values()) {

            // offsets of the upstream inner corner
            float lateral_offset = lane_width*(lg.start_lane_dn-1);
            float long_offset = lg.side== Side.middle ? 0 : link.bLink.length-lg.length;

            AbstractDrawLanegroup draw_lg = create_draw_lanegroup(lg,midline,lateral_offset,long_offset,lane_width, road2euclid,colormap);
            draw_lanegroups.add(draw_lg);
            getChildren().addAll(draw_lg.get_polygons());
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

    abstract AbstractDrawLanegroup create_draw_lanegroup(AbstractLaneGroup lg,List<Arrow> midline,float lateral_offset,float long_offset, double lane_width,double road2euclid,AbstractColormap colormap) throws OTMException ;

    /////////////////////////////////////////////////
    // paint
    /////////////////////////////////////////////////

    public void paint(float link_offset, float lane_width, GlobalParameters.ColorScheme colormap){
        draw_lanegroups.forEach(x -> x.paint(link_offset,lane_width,colormap));
    }

    public void highlight() {
        draw_lanegroups.forEach(x -> x.highlight(color_highlight));
    }

    public void unhighlight() {
        draw_lanegroups.forEach(x -> x.unhighlight());
    }

    /////////////////////////////////////////////////
    // get
    /////////////////////////////////////////////////

    public Double getStartPosX(){
        return startNode.xpos;
    }

    public Double getStartPosY(){
        return startNode.ypos;
    }

    public Double getEndPosX(){
        return endNode.xpos;
    }

    public Double getEndPosY(){
        return endNode.ypos;
    }

}
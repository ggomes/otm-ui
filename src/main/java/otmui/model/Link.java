package otmui.model;

import actuator.AbstractActuator;
import otmui.graph.item.AbstractDrawLink;
import otmui.utils.Point;
import common.AbstractLaneGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Link {

    public common.Link bLink;

    public AbstractDrawLink drawLink; //  graph pane object for this link

    public AbstractActuator actuator;
    public DemandsForLink demandsForLink;

    public Link(common.Link bLink){
        this.bLink = bLink;
    }

    public Long getId(){
        return bLink.getId();
    }

    public Long getStartNodeId(){
        return bLink.start_node.getId();
    }

    public Long getEndNodeId(){
        return bLink.end_node.getId();
    }

    public int getLanes(){
        return bLink.full_lanes;
    }

    public Float getLength(){
        return bLink.length;
    }

    public List<Point> getShape(){
        ArrayList<Point> x = new ArrayList<>();
        for(common.Point p : bLink.shape)
            x.add(new Point(p.x,p.y));
        return x;
    }

    public AddLanes getLeftLanes(){
        if( bLink.road_geom==null || bLink.road_geom.dn_left==null || bLink.road_geom.dn_left.lanes==0 )
            return null;
        return new AddLanes(bLink.road_geom.dn_left);
    }

    public AddLanes getRightLanes(){
        if( bLink.road_geom==null || bLink.road_geom.dn_right==null || bLink.road_geom.dn_right.lanes==0 )
            return null;
        return new AddLanes(bLink.road_geom.dn_right);
    }

    public AddLanes getHOVlanes(){

        if( bLink.road_geom.dn_right.lanes==0 )
            return null;
        return new AddLanes(bLink.road_geom.dn_right);
    }

    /** Lanegroups *******************************************/

    public Collection<AbstractLaneGroup> getLanegroups(){
        return bLink.lanegroups.values();
    }

    /** Model *******************************************/

    public common.AbstractLinkModel getModel(){
        return bLink.model;
    }

    public common.Link.ModelType getModelType(){
        return bLink.model_type;
    }

    public boolean isSink(){
        return bLink.is_sink;
    }

    public boolean isSource(){
        return bLink.is_source;
    }

}

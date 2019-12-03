package otmui.model;

import actuator.AbstractActuator;
import models.BaseLaneGroup;
import otmui.graph.item.AbstractDrawLink;
import otmui.utils.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Link implements Comparable {

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

    public List<Vector> getShape(){
        ArrayList<Vector> x = new ArrayList<>();
        for(common.Point p : bLink.shape)
            x.add(new Vector(p.x,p.y));
        return x;
    }

    public AddLanes getLeftLanes(){
        if( bLink.road_geom==null || bLink.road_geom.dn_in==null || bLink.road_geom.dn_in.lanes==0 )
            return null;
        return new AddLanes(bLink.road_geom.dn_in);
    }

    public AddLanes getRightLanes(){
        if( bLink.road_geom==null || bLink.road_geom.dn_out==null || bLink.road_geom.dn_out.lanes==0 )
            return null;
        return new AddLanes(bLink.road_geom.dn_out);
    }

    public AddLanes getHOVlanes(){

        if( bLink.road_geom.dn_out.lanes==0 )
            return null;
        return new AddLanes(bLink.road_geom.dn_out);
    }

    /** Lanegroups *******************************************/

    public Collection<BaseLaneGroup> getDwnLaneGroups(){
        return bLink.lanegroups_flwdn.values();
    }

    /** Model *******************************************/

    public models.BaseModel getModel(){
        return bLink.model;
    }

    public boolean isSink(){
        return bLink.is_sink;
    }

    public boolean isSource(){
        return bLink.is_source;
    }

    @Override
    public int compareTo(Object o) {
        Link that = (Link) o;
        if(this.getId()<that.getId())
            return -1;
        if(this.getId()>that.getId())
            return 1;
        return 0;
    }
}

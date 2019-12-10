package otmui.model;

import actuator.AbstractActuator;
import models.BaseLaneGroup;
import otmui.graph.item.AbstractDrawLink;
import otmui.utils.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Link implements Comparable {

    public common.Link clink;

    public AbstractDrawLink drawLink; //  graph pane object for this link

    public AbstractActuator actuator;
    public DemandsForLink demandsForLink;

//    public Link(common.Link clink){
//        this.clink = clink;
//    }

    public Long getId(){
        return clink.getId();
    }

    public Long getStartNodeId(){
        return clink.start_node.getId();
    }

    public Long getEndNodeId(){
        return clink.end_node.getId();
    }

    public int getLanes(){
        return clink.full_lanes;
    }

    public Float getLength(){
        return clink.length;
    }

    public List<Vector> getShape(){
        ArrayList<Vector> x = new ArrayList<>();
        for(common.Point p : clink.shape)
            x.add(new Vector(p.x,p.y));
        return x;
    }

//    public AddLanes getLeftLanes(){
//        if( clink.road_geom==null || clink.road_geom.dn_in==null || clink.road_geom.dn_in.lanes==0 )
//            return null;
//        return new AddLanes(clink.road_geom.dn_in);
//    }
//
//    public AddLanes getRightLanes(){
//        if( clink.road_geom==null || clink.road_geom.dn_out==null || clink.road_geom.dn_out.lanes==0 )
//            return null;
//        return new AddLanes(clink.road_geom.dn_out);
//    }

//    public AddLanes getHOVlanes(){
//
//        if( clink.road_geom.dn_out.lanes==0 )
//            return null;
//        return new AddLanes(clink.road_geom.dn_out);
//    }

    /** Lanegroups *******************************************/

    public Collection<BaseLaneGroup> getDwnLaneGroups(){
        return clink.lanegroups_flwdn.values();
    }

    /** Model *******************************************/

    public models.BaseModel getModel(){
        return clink.model;
    }

    public boolean isSink(){
        return clink.is_sink;
    }

    public boolean isSource(){
        return clink.is_source;
    }


    /////////////////////////
    // setters
    /////////////////////////

    public void setStartNode(Node node){
        clink.start_node = node.cnode;
        node.cnode.out_links.put(getId(), clink);
    }

    public void setEndNode(Node node){
        clink.end_node = node.cnode;
        node.cnode.in_links.put(getId(), clink);
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

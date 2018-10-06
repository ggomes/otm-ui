/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.model;

import actuator.AbstractActuator;
import common.AbstractLaneGroupLongitudinal;
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

    public Collection<AbstractLaneGroupLongitudinal> getLongLanegroups(){
        return bLink.long_lanegroups.values();
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

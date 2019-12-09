package otmui.model;

import actuator.AbstractActuator;
import otmui.graph.item.AbstractDrawNode;

import java.util.*;
import java.util.stream.Collectors;

public class Node {

    public common.Node cnode;

    public AbstractDrawNode drawNode; // graph pane object for this node

    public Float xcoord;
    public Float ycoord;

    public AbstractActuator actuator;
    public SplitsForNode splitsForNode;

    public Node(common.Node cnode){
        this.cnode = cnode;
        xcoord = cnode.xcoord;
        ycoord = cnode.ycoord;
    }

    public Long getId(){
        return cnode.getId();
    }

    public Float getXcoord() {
        return xcoord;
    }

    public Float getYcoord() {
        return ycoord;
    }

    public Collection<Long> getInLinkIds(){
        Set z = new HashSet<>();
        if(cnode.in_links!=null)
            z.addAll(cnode.in_links.values().stream()
                    .map(x->x.getId())
                    .collect(Collectors.toSet()));
        return z;
    }

    public Collection<Long> getOutLinkIds(){
        Set z = new HashSet<>();
        if(cnode.out_links!=null)
            z.addAll(cnode.out_links.values().stream()
                    .map(x->x.getId())
                    .collect(Collectors.toSet()));
        return z;
    }

}

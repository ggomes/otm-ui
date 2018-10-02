/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.model;

import actuator.AbstractActuator;
import otmui.graph.item.AbstractDrawNode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by gomes on 1/30/2017.
 */
public class Node {

    private common.Node bnode;

    public AbstractDrawNode drawNode; // graph pane object for this node

    private Float xcoord;
    private Float ycoord;

    public AbstractActuator actuator;
    public SplitsForNode splitsForNode;

    public Node(common.Node bbnode){
        bnode = bbnode;
        xcoord = bbnode.xcoord;
        ycoord = bbnode.ycoord;
    }

    public Long getId(){
        return bnode.getId();
    }

    public Float getXcoord() {
        return xcoord;
    }

    public Float getYcoord() {
        return ycoord;
    }

    public Collection<Long> getInLinkIds(){
        Set z = new HashSet<>();
        if(bnode.in_links!=null)
            z.addAll(bnode.in_links.values().stream()
                    .map(x->x.getId())
                    .collect(Collectors.toSet()));
        return z;
    }

    public Collection<Long> getOutLinkIds(){
        Set z = new HashSet<>();
        if(bnode.out_links!=null)
            z.addAll(bnode.out_links.values().stream()
                    .map(x->x.getId())
                    .collect(Collectors.toSet()));
        return z;
    }

}

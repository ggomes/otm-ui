package otmui.model;

import keys.KeyCommodityLink;
import profiles.SplitMatrixProfile;

import java.util.*;
import java.util.stream.Collectors;

public class SplitsForNode {

    public Long node_id;
    public Map<KeyCommodityLink, SplitMatrixProfile> splits;  // commodity/inlink -> splitsForNode matrix

//    public SplitsForNode(common.Node node){
//        this.node_id = node.getId();
//        splits = node.splits;
//    }

    public Long getId(){
        return node_id;
    }

    public Long getNodeId(){
        return node_id;
    }

    public Set<Long> getCommodityIds(){
        return splits.values().stream()
                .map(x->x.commodity_id)
                .collect(Collectors.toSet());
    }

    public Set<Long> getInLinkIds(){
        return splits.values().stream()
                .map(x->x.link_in_id)
                .collect(Collectors.toSet());
    }

}

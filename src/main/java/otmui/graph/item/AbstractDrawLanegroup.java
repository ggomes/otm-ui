package otmui.graph.item;

import common.AbstractLaneGroup;

public abstract class AbstractDrawLanegroup implements InterfaceDrawLanegroup {

    public Long id;
    public int lanes;

    public AbstractDrawLanegroup(AbstractLaneGroup lg) {
        this.lanes = lg.num_lanes();
        this.id = lg.id;
    }

}

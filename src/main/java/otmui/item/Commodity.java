package otmui.item;

import otmui.ItemType;

public class Commodity extends AbstractItem {

    public commodity.Commodity comm;

    public Commodity(commodity.Commodity comm) {
        super(comm.getId());
        this.comm = comm;
    }

    @Override
    public ItemType getType() {
        return ItemType.commodity;
    }
}

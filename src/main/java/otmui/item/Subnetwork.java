package otmui.item;

import otmui.ItemType;

public class Subnetwork extends AbstractItem {

    public commodity.Subnetwork subnet;
    public Subnetwork(commodity.Subnetwork subnet) {
        super(subnet.getId());

        this.subnet = subnet;
    }

    @Override
    public String getName() {
        return "subnetwork";
    }

    @Override
    public ItemType getType() {
        return ItemType.subnetwork;
    }
}

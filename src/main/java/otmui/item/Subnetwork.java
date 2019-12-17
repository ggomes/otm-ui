package otmui.item;

import otmui.Data;
import otmui.ItemType;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class Subnetwork extends AbstractGraphItem {

    public commodity.Subnetwork subnet;
    public Set<otmui.item.Link> links;

    public Subnetwork(commodity.Subnetwork subnet, Data data) {
        super(subnet.getId(), Float.NaN, Float.NaN, null, null);

        this.subnet = subnet;

        this.links = new HashSet<>();
        this.links.addAll(subnet.links.stream()
                .map( x -> (Link) data.items.get(ItemType.link).get(x.getId()) )
                .collect(toSet()));
    }

    @Override
    public ItemType getType() {
        return ItemType.subnetwork;
    }

    @Override
    public void highlight() {
        links.forEach(link->link.highlight());
    }

    @Override
    public void unhighlight() {
        links.forEach(link->link.unhighlight());
    }

    @Override
    public void set_size(float s) {
    }
}

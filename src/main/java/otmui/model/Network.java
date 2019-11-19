package otmui.model;

import otmui.Maps;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Network {

    public Map<Long, Node> nodes;
    public Map<Long, Link> links;

    public Network(common.Network bNetwork, boolean node_position_in_meters) {

        nodes = new HashMap();
        for (common.Node bNode : bNetwork.nodes.values())
            nodes.put(bNode.getId(), new Node(bNode));

        links = new HashMap();
        for (common.Link bLink : bNetwork.links.values()) {
            Link link = new Link(bLink);
            links.put(bLink.getId(), link);
            Maps.name2linkid.put(String.format("link %d", link.getId()), bLink.getId());
        }

        if (!node_position_in_meters)
            convert_to_meters(nodes.values(),links.values());

    }

    private static void convert_to_meters(Collection<Node> nodes, Collection<Link> links) {

        double R = 6378137.0;  // Radius of Earth in meters
        double conv = Math.PI / 180.0;
        double clat = nodes.stream().mapToDouble(n -> n.ycoord).average().getAsDouble()* conv;
        double clon = nodes.stream().mapToDouble(n -> n.xcoord).average().getAsDouble()* conv;
        double cos2 = Math.pow(Math.cos(clat),2);

        for(Node node : nodes){
            double lon = node.xcoord * conv;
            double lat = node.ycoord * conv;
            double dx = Math.acos(1-cos2*(1-Math.cos(lon-clon)))*R;
            node.xcoord = (float) (lon<clon ? -dx : dx);
            node.ycoord = (float) ( (lat - clat) * R );
            node.bnode.xcoord = node.xcoord;
            node.bnode.ycoord = node.ycoord;
        }

        for(Link link : links){
            for(common.Point point : link.bLink.shape){
                double lon = point.x * conv;
                double lat = point.y* conv;
                double dx = Math.acos(1-cos2*(1-Math.cos(lon-clon)))*R;
                point.x = (float) (lon<clon ? -dx : dx);
                point.y = (float) ( (lat - clat) * R );
            }
        }

    }

}

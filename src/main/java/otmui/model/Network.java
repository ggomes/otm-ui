/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.model;

import otmui.Maps;

import java.util.HashMap;
import java.util.Map;

public class Network {

    public Map<Long,Node> nodes;
    public Map<Long,Link> links;

    public Network(common.Network bNetwork){

        nodes = new HashMap();
        for(common.Node bNode : bNetwork.nodes.values())
            nodes.put(bNode.getId(),new Node(bNode));

        links = new HashMap();
        for(common.Link bLink : bNetwork.links.values()) {
            Link link = new Link(bLink);
            links.put(bLink.getId(),link);
            Maps.name2linkid.put(String.format("link %d",link.getId()),bLink.getId());
        }
    }

}

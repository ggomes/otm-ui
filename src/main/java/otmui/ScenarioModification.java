package otmui;

import actuator.AbstractActuator;
import commodity.Path;
import commodity.Subnetwork;
import error.OTMException;
import javafx.event.Event;
import keys.DemandType;
import keys.KeyCommodityDemandTypeId;
import otmui.event.DoAddItem;
import otmui.event.DoRemoveItem;

import java.util.Comparator;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class ScenarioModification {

    public static otmui.item.Node insert_node(MainApp myApp, float xcoord, float ycoord, otmui.item.Actuator actuator){

        runner.Scenario scenario = myApp.otm.scenario;
        long id = scenario.network.nodes.keySet().stream().max(Comparator.naturalOrder()).get() + 1;
        common.Node cnode = new common.Node(scenario.network,id,xcoord,ycoord,false);

        // add to OTM scenario
        scenario.network.nodes.put(id,cnode);

        // create ui item
        otmui.item.Node node = FactoryItem.makeNode(cnode,myApp.params);
        myApp.data.items.get(ItemType.node).put(node.id,node);

        // attach the actuator
        if(actuator!=null){
            node.node.actuator = actuator.actuator;
            actuator.actuator.target = node.node;
        }

        // fire event
        Event.fireEvent(myApp.stage.getScene(),new DoAddItem(DoAddItem.ADD_ITEM, node));

        return node;
    }

    public static void delete_node(MainApp myApp,otmui.item.Node node) throws OTMException {

        System.out.println("Deleting node " + node.id);

        runner.Scenario scenario = myApp.otm.scenario;
        long node_id = node.id;
        common.Node cnode = node.node;

        // CHECKS

        if(!myApp.otm.scenario.network.nodes.containsKey(node_id))
            return;

        if(cnode.actuator!=null)
            throw new OTMException("I don't know how to delete nodes with actuators");

        // remove from the network
        scenario.network.nodes.remove(node_id);

        // adjust links
        for(common.Link link : cnode.in_links.values())
            link.end_node = null;
        for(common.Link link : cnode.out_links.values())
            link.start_node = null;

        // fire event
        Event.fireEvent(myApp.stage.getScene(),new DoRemoveItem(DoRemoveItem.REMOVE_ITEM, node));

    }

    public static void delete_link(MainApp myApp,otmui.item.Link link) throws OTMException {

        long id = link.id;
        common.Link clink = link.link;

        common.Node start_node = clink.start_node;
        common.Node end_node = clink.end_node;

        // no upstream splits
        if(start_node.splits!=null)
            throw new OTMException("I don't know how to delete links with upstream splits.");

        // no downstream splits
        if(end_node.splits!=null)
            throw new OTMException("I don't know how to delete links with downstream splits.");

        // no demands
        Set<Subnetwork> subnets = myApp.otm.scenario.subnetworks.values().stream()
                .filter(s->s instanceof Path)
                .collect(toSet());
        Set<KeyCommodityDemandTypeId> keys = myApp.otm.scenario.data_demands.keySet().stream()
                .filter(k-> (k.demandType==DemandType.pathless && k.link_or_subnetwork_id==id) || (k.demandType==DemandType.pathfull && subnets.contains(k.link_or_subnetwork_id) ) )
                .collect(toSet());
        if(!keys.isEmpty())
            throw new OTMException("I don't know how to delete links with demands.");

        // no actuators
        if(clink.ramp_meter!=null || clink.actuator_fd!=null)
            throw new OTMException("I don't know how to delete links with actuators.");


        // TODO no sensors

        // remove from the network
        end_node.in_links.remove(id);
        start_node.out_links.remove(id);
        myApp.otm.scenario.network.links.remove(id);

        // fire event
        Event.fireEvent(myApp.stage.getScene(),new DoRemoveItem(DoRemoveItem.REMOVE_ITEM, link));

    }

    public static void delete_actuator(MainApp myApp,otmui.item.Actuator actuator) throws OTMException {

        runner.Scenario scenario = myApp.otm.scenario;
        AbstractActuator cactuator = actuator.actuator;

        if(cactuator.target instanceof common.Node){
            common.Node node = (common.Node) cactuator.target;
            node.actuator = null;
        }

        if(cactuator.myController!=null)
            cactuator.myController.actuators.remove(actuator.id);

        scenario.actuators.remove(actuator);

        // fire event
        Event.fireEvent(myApp.stage.getScene(),new DoRemoveItem(DoRemoveItem.REMOVE_ITEM, actuator));
    }

}

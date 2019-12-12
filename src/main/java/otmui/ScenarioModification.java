package otmui;

import actuator.AbstractActuator;
import api.OTM;
import api.OTMdev;
import commodity.Path;
import commodity.Subnetwork;
import error.OTMException;
import javafx.event.Event;
import keys.DemandType;
import keys.KeyCommodityDemandTypeId;
import keys.KeyCommodityLink;
import otmui.event.DeleteElementEvent;
import otmui.event.FormSelectEvent;
import otmui.event.NewElementEvent;

import java.util.Comparator;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class ScenarioModification {

    public static common.Node create_node(MainApp myApp,float xcoord,float ycoord){
        runner.Scenario scenario = myApp.otm.scenario;
        long id = scenario.network.nodes.keySet().stream().max(Comparator.naturalOrder()).get() + 1;
        common.Node node = new common.Node(scenario.network,id,xcoord,ycoord,false);

        // add to OTM scenario
        scenario.network.nodes.put(id,node);

        // fire event
//        Event.fireEvent(myApp.stage.getScene(),new XXX(XXX.ADD_NODE, node));

        return node;
    }

    public static void delete_node(MainApp myApp,common.Node node) throws OTMException {

        runner.Scenario scenario = myApp.otm.scenario;
        long node_id = node.getId();

        // CHECKS

        if(!myApp.otm.scenario.network.nodes.containsKey(node_id))
            return;

        if(node.actuator!=null)
            throw new OTMException("I don't know how to delete nodes with actuators");

        // remove from the network
        scenario.network.nodes.remove(node_id);

        // adjust links
        for(common.Link link : node.in_links.values())
            link.end_node = null;
        for(common.Link link : node.out_links.values())
            link.start_node = null;

        // fire event
        Event.fireEvent(myApp.stage.getScene(),new DeleteElementEvent(DeleteElementEvent.REMOVE_NODE, node));

    }

    public static void delete_link(MainApp myApp,common.Link link) throws OTMException {

        long id = link.getId();

        common.Node start_node = link.start_node;
        common.Node end_node = link.end_node;

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
        if(link.ramp_meter!=null || link.actuator_fd!=null)
            throw new OTMException("I don't know how to delete links with actuators.");


        // TODO no sensors

        // remove from the network
        end_node.in_links.remove(id);
        start_node.out_links.remove(id);
        myApp.otm.scenario.network.links.remove(id);

        // fire event
        Event.fireEvent(myApp.stage.getScene(),new DeleteElementEvent(DeleteElementEvent.REMOVE_LINK, link));

    }

    public static void delete_actuator(MainApp myApp, AbstractActuator actuator) throws OTMException {

        runner.Scenario scenario = myApp.otm.scenario;

        if(actuator.target instanceof common.Node){
            common.Node node = (common.Node) actuator.target;
            node.actuator = null;
        }

        if(actuator.myController!=null)
            actuator.myController.actuators.remove(actuator.id);

        scenario.actuators.remove(actuator);

        // fire event
        Event.fireEvent(myApp.stage.getScene(),new DeleteElementEvent(DeleteElementEvent.REMOVE_ACTUATOR, actuator));
    }

}

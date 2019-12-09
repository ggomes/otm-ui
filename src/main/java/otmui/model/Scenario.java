package otmui.model;

import actuator.AbstractActuator;
import api.OTMdev;
import common.RoadConnection;
import otmui.Maps;
import commodity.Subnetwork;
import control.AbstractController;
import error.OTMException;
import keys.DemandType;
import profiles.AbstractDemandProfile;
import profiles.DemandProfile;
import sensor.AbstractSensor;

import java.util.*;

import static java.util.stream.Collectors.toMap;

public class Scenario {

    private final OTMdev otm;

    private Network network;
    private Map<Long,DemandsForLink> demands_for_links;
    private Map<Long,SplitsForNode> splits;
    private Map<Long, Actuator> actuators;
    private Map<Long, Sensor> sensors;

    public Scenario(OTMdev otm) throws OTMException {

        this.otm = otm;

        runner.Scenario bscenario = this.otm.scenario;

        // network .........................
        this.network = new Network(bscenario.network,bscenario.network.node_positions_in_meters);

        // name map: commodities .........................
        for(commodity.Commodity x : bscenario.commodities.values())
            Maps.name2commodityid.put(x.get_name(),x.getId());

        // name map: subnetworks .........................
        for(commodity.Subnetwork x : bscenario.subnetworks.values())
            Maps.name2subnetworkid.put(x.getName()==null ? "<no name>" : x.getName(), x.getId());

        // demands_for_links .........................
        demands_for_links = new HashMap<>();
        for(AbstractDemandProfile adp : bscenario.data_demands.values()){

            DemandProfile dp = (DemandProfile) adp;

            DemandType type = dp.get_type();
            common.Link origin = dp.get_origin();
            Long origin_id = origin.getId();

            DemandsForLink demandsForLink;
            if(!demands_for_links.containsKey(origin_id)){
                demandsForLink = new DemandsForLink(origin_id);
                demands_for_links.put(origin_id, demandsForLink);
                Link alink = getLinkWithId(origin_id);
                if(alink!=null)
                    alink.demandsForLink = demandsForLink;
                Maps.name2demandid.put(String.format("demands for link %d",origin_id), origin_id);
            } else{
                demandsForLink = demands_for_links.get(origin_id);
            }

            demandsForLink.add_profile(dp);
        }

        // splitsForNode .........................
        splits = new HashMap<>();
        for(common.Node node : bscenario.network.nodes.values())
            if(node.splits!=null && !node.splits.isEmpty()) {
                splits.put(node.getId(), new SplitsForNode(node));
                Maps.name2splitid.put(String.format("splits for node %d",node.getId()),node.getId());
            }

        // controllers .........................
        for(control.AbstractController x : bscenario.controllers.values())
            Maps.name2controllerid.put(String.format("controller %d",x.id),x.id);

        // sensors .........................
        sensors = new HashMap<>();
        for(sensor.AbstractSensor x : bscenario.sensors.values()) {

            if(!(x instanceof sensor.FixedSensor))
                continue;

            Sensor sensor = new Sensor((sensor.FixedSensor)x);
            sensors.put(x.id,sensor);
            Maps.name2sensorid.put(String.format("sensor %d",x.id),x.id);
        }

        // actuators .........................
        actuators = new HashMap<>();
        for(actuator.AbstractActuator x : bscenario.actuators.values()) {
            Actuator actuator = new Actuator(x,network);
            actuators.put(x.id,actuator);
            Maps.name2actuatorid.put(String.format("actuator %d",x.getId()),x.getId());
        }

    }

    /** zeroth order getters **/

    public Collection<Node> getNodes(){
        return network.nodes.values();
    }

    public Collection<Link> getLinks(){
        return network.links.values();
    }

    public Collection<commodity.Commodity> getCommodities(){
        return otm.scenario.commodities.values();
    }

    public Collection<DemandsForLink> getDemandsForLinks(){
        return demands_for_links.values();
    }

    public Collection<Subnetwork> getSubnetworks(){
        return otm.scenario.subnetworks.values();
    }

    public Collection<SplitsForNode> getSplits(){
        return splits.values();
    }

    public Collection<AbstractController> getControllers(){
        return otm.scenario.controllers.values();
    }

    public Collection<Actuator> getActuators(){
        return actuators.values();
    }

    public Collection<Sensor> getSensors(){
        return sensors.values();
    }

    /** first order getters **/

    public Link getLinkWithId(Long id){
        return network.links.get(id);
    }

    public Node getNodeWithId(Long id){
        return network.nodes.get(id);
    }

    public DemandsForLink get_demands_with_link_id(Long id){
        return demands_for_links.get(id);
    }

    public SplitsForNode getSplitWithId(Long id){
        return splits.get(id);
    }

    public commodity.Commodity getCommodityWithId(Long id){
        return otm.scenario.commodities.get(id);
    }

    public AbstractActuator getActuatorWithId(Long id){
        return otm.scenario.actuators.get(id);
    }

    public Subnetwork getSubnetworkWithId(Long id){
        return otm.scenario.subnetworks.get(id);
    }

    public AbstractController getControllerWithId(Long id){
        return otm.scenario.controllers.get(id);
    }

    public Sensor getSensorWithId(Long id){
        return sensors.get(id);
    }

    /** second order getters **/

    public Collection<Long> getInlinkIdsForNodeId(Long nodeId){
        return network.nodes.get(nodeId).getInLinkIds();
    }

    public Collection<Long> getOutlinkIdsForNodeId(Long nodeId){
        return network.nodes.get(nodeId).getOutLinkIds();
    }

    public OTMdev get_otm(){
        return otm;
    }

    ///////////////////////
    // setters
    ///////////////////////

    public void delete_link(Link link){

        long link_id = link.getId();

        if(!network.links.containsKey(link_id))
            return;

        common.Link clink = otm.scenario.network.links.get(link_id);

        // remove road connections
        Set<RoadConnection> rcs = clink.get_roadconnections_entering();
        rcs.addAll(clink.get_roadconnections_leaving());
        for(common.RoadConnection rc : rcs)
            otm.scenario.network.road_connections.remove(rc.getId());

        // remove from the network
        long start_node = link.getStartNodeId();
        long end_node = link.getEndNodeId();

        network.links.remove(link_id);
        network.nodes.get(start_node).getOutLinkIds().remove(link_id);
        network.nodes.get(end_node).getInLinkIds().remove(link_id);

        otm.scenario.network.links.remove(link_id);
        otm.scenario.network.nodes.get(start_node).out_links.remove(link_id);
        otm.scenario.network.nodes.get(end_node).in_links.remove(link_id);

        // remove sensors
        sensors = sensors.entrySet().stream()
                .filter(e->e.getValue().bsensor.get_link().getId()!=link_id)
                .collect(toMap(s->s.getKey(),s->(Sensor) s));

        otm.scenario.sensors = otm.scenario.sensors.entrySet().stream()
                .filter(e->e.getValue().target instanceof common.Link)
                .filter(e->((common.Link)e.getValue().target).getId()!=link_id)
                .collect(toMap(s->s.getKey(),s->(AbstractSensor) s));

        // TODO remove actuators

        // remove demands
        demands_for_links = demands_for_links.entrySet().stream()
                .filter(e->e.getValue().link_id!=link_id)
                .collect(toMap(s->s.getKey(),s->(DemandsForLink) s));

        // TODO Remove demands from the otm scenario

    }

    public void delete_node(Node node){

        long node_id = node.getId();

        if(!network.nodes.containsKey(node_id))
            return;

        // remove from the network
        network.nodes.remove(node_id);

        for(common.Link link : node.cnode.in_links.values())
            link.end_node = null;
        for(common.Link link : node.cnode.out_links.values())
            link.start_node = null;
        otm.scenario.network.nodes.remove(node_id);

        // remove actuators
        if(node.actuator!=null)
            actuators.remove(node.actuator.id);

        // remove splits
        if(splits!=null)
           splits.remove(node_id);

    }

    public Node create_node(float xcoord,float ycoord){
        common.Node new_cnode = otm.otm.scenario().create_node(xcoord,ycoord);
        Node new_node = new Node(new_cnode);
        network.nodes.put(new_cnode.getId(),new_node);
        return new_node;
    }

}

package otmui.model;

import actuator.AbstractActuator;
import api.APIopen;
import otmui.Maps;
import commodity.Subnetwork;
import control.AbstractController;
import error.OTMException;
import keys.DemandType;
import profiles.AbstractDemandProfile;
import profiles.DemandProfile;

import java.util.*;

public class Scenario {

    private final APIopen beats;

    private final Network network;
    private final Map<Long,DemandsForLink> demands_for_links;
    private final Map<Long,SplitsForNode> splits;
    private final Map<Long, Actuator> actuators;
    private final Map<Long, Sensor> sensors;

    public Scenario(APIopen beats) throws OTMException {

        this.beats = beats;

        runner.Scenario bscenario = this.beats.scenario();

        // network .........................
        this.network = new Network(bscenario.network);

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

            if(!(x instanceof sensor.SensorLoopDetector))
                continue;

            Sensor sensor = new Sensor((sensor.SensorLoopDetector)x);
            sensors.put(new Long(x.id),sensor);
            Maps.name2sensorid.put(String.format("sensor %d",x.id),x.id);
        }

        // actuators .........................
        actuators = new HashMap<>();
        for(actuator.AbstractActuator x : bscenario.actuators.values()) {
            Actuator actuator = new Actuator(x);
            actuators.put(new Long(x.id),actuator);
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
        return beats.scenario().commodities.values();
    }

    public Collection<DemandsForLink> getDemandsForLinks(){
        return demands_for_links.values();
    }

    public Collection<Subnetwork> getSubnetworks(){
        return beats.scenario().subnetworks.values();
    }

    public Collection<SplitsForNode> getSplits(){
        return splits.values();
    }

    public Collection<AbstractController> getControllers(){
        return beats.scenario().controllers.values();
    }

    public Collection<AbstractActuator> getActuators(){
        return beats.scenario().actuators.values();
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
        return beats.scenario().commodities.get(id);
    }

    public AbstractActuator getActuatorWithId(Long id){
        return beats.scenario().actuators.get(id);
    }

    public Subnetwork getSubnetworkWithId(Long id){
        return beats.scenario().subnetworks.get(id);
    }

    public AbstractController getControllerWithId(Long id){
        return beats.scenario().controllers.get(id);
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

    public APIopen get_beats(){
        return beats;
    }
}

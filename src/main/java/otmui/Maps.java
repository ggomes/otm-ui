package otmui;

import api.OTMdev;
import otmui.model.Actuator;
import otmui.model.Link;
import otmui.model.Sensor;
import otmui.utils.BijectiveMap;
import profiles.AbstractDemandProfile;

import java.util.HashMap;

public class Maps {

    public static BijectiveMap<ElementType,String> elementName = new BijectiveMap();

    public static BijectiveMap<String,Long> name2linkid = new BijectiveMap();
    public static BijectiveMap<String,Long> name2commodityid = new BijectiveMap();
    public static BijectiveMap<String,Long> name2subnetworkid = new BijectiveMap();
    public static BijectiveMap<String,Long> name2demandid = new BijectiveMap();
    public static BijectiveMap<String,Long> name2splitid = new BijectiveMap();
    public static BijectiveMap<String,Long> name2actuatorid = new BijectiveMap();
    public static BijectiveMap<String,Long> name2controllerid = new BijectiveMap();
    public static BijectiveMap<String,Long> name2sensorid = new BijectiveMap();

    public static void populate(OTMdev otmdev){

        elementName.put(ElementType.LINK,"links");
        elementName.put(ElementType.COMMODITY,"vehicle types");
        elementName.put(ElementType.SUBNETWORK,"subnetworks");
        elementName.put(ElementType.DEMAND,"demands");
        elementName.put(ElementType.SPLIT,"splits");
        elementName.put(ElementType.ACTUATOR,"actuators");
        elementName.put(ElementType.CONTROLLER,"controllers");
        elementName.put(ElementType.SENSOR,"sensors");

        // links
        for (Long id : otmdev.scenario.network.links.keySet())
            Maps.name2linkid.put(String.format("link %d", id),id);

        // commodities
        for(commodity.Commodity x : otmdev.scenario.commodities.values())
            Maps.name2commodityid.put(x.get_name(),x.getId());

        // subnetworks
        for(commodity.Subnetwork x : otmdev.scenario.subnetworks.values())
            Maps.name2subnetworkid.put(x.getName()==null ? "<no name>" : x.getName(), x.getId());

        // demands
        for(AbstractDemandProfile v : otmdev.scenario.data_demands.values())
            Maps.name2demandid.put(String.format("demands for link %d",v.source.link.getId()), v.source.link.getId());

        // TODO splits
//        Maps.name2splitid.put(String.format("splits for node %d",node.getId()),node.getId());

        // controllers
        for(control.AbstractController x : otmdev.scenario.controllers.values())
            Maps.name2controllerid.put(String.format("controller %d",x.id),x.id);

        // sensors .........................
        for(sensor.AbstractSensor x : otmdev.scenario.sensors.values())
            Maps.name2sensorid.put(String.format("sensor %d",x.id),x.id);

        // actuators .........................
        for(actuator.AbstractActuator x : otmdev.scenario.actuators.values())
            Maps.name2actuatorid.put(String.format("actuator %d",x.getId()),x.getId());

    }

}

package otmui;

import otmui.utils.BijectiveMap;

/**
 * Created by gomes on 2/6/2017.
 */
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

    static{
        elementName.put(ElementType.LINK,"links");
        elementName.put(ElementType.COMMODITY,"vehicle types");
        elementName.put(ElementType.SUBNETWORK,"subnetworks");
        elementName.put(ElementType.DEMAND,"demands");
        elementName.put(ElementType.SPLIT,"splits");
        elementName.put(ElementType.ACTUATOR,"actuators");
        elementName.put(ElementType.CONTROLLER,"controllers");
        elementName.put(ElementType.SENSOR,"sensors");
    }

}

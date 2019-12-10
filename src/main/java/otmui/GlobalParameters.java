package otmui;

import javafx.beans.property.SimpleBooleanProperty;
import otmui.event.ParameterChange;
import otmui.graph.color.AbstractColormap;
import otmui.graph.color.MatlabColormap;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.scene.Scene;

public class GlobalParameters {

    public enum RoadColorScheme { Black, Cells, RoadType }

    // simulation run parameters
    public SimpleFloatProperty start_time           = new SimpleFloatProperty(null,"start_time",0f);            // seconds after midnight
    public SimpleFloatProperty sim_dt               = new SimpleFloatProperty(null,"sim_dt",2f);                // seconds after midnight
    public SimpleFloatProperty duration             = new SimpleFloatProperty(null,"duration",4000f);           // seconds
    public SimpleFloatProperty sim_delay            = new SimpleFloatProperty(null,"sim_delay",15f);             // milliseconds

    // display parameters
    public SimpleFloatProperty link_offset          = new SimpleFloatProperty(null,"link_offset",0f);           // [m] painted width of a lane
    public SimpleFloatProperty lane_width_meters    = new SimpleFloatProperty(null,"lane_width_meters",5f);            // [m] painted width of a lane
    public SimpleFloatProperty node_size            = new SimpleFloatProperty(null,"node_size",6f);           // [m] painted radius for circular nodes
    public SimpleObjectProperty road_color_scheme = new SimpleObjectProperty(null,"road color scheme", RoadColorScheme.Black);
    public SimpleBooleanProperty view_nodes         = new SimpleBooleanProperty(null,"view_nodes",true);
    public SimpleBooleanProperty view_actuators     = new SimpleBooleanProperty(null,"view_actuators",false);
    public SimpleFloatProperty max_density_vpkpl    = new SimpleFloatProperty(null,"max_density_vpkpl",100f);   // [vpkpl] used for displaying MN states

    public GlobalParameters(Scene scene){
        start_time.addListener(         e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.SIMULATION)));
        sim_dt.addListener(             e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.SIMULATION)));
        duration.addListener(           e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.SIMULATION)));
        sim_delay.addListener(          e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.SIMULATION)));
        link_offset.addListener(        e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.DRAWLINKSHAPES)));
        lane_width_meters.addListener(  e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.DRAWLINKSHAPES)));
        node_size.addListener(          e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.DRAWNODESHAPES)));
        road_color_scheme.addListener(e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.DRAWLINKCOLORS)));
        max_density_vpkpl.addListener(  e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.DRAWLINKSHAPES)));
        view_nodes.addListener(         e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.DRAWNODESHAPES)));
        view_actuators.addListener(     e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.DRAWACTUATORS)));
    }

    public AbstractColormap get_colormap(){
        return new MatlabColormap(
                this.max_density_vpkpl.floatValue() ,
                (RoadColorScheme) this.road_color_scheme.getValue());
//        return new HSVColormap(
//                this.max_density_vpkpl.floatValue() ,
//                (GlobalParameters.ColorScheme) this.color_map.getValue());
    }

    @Override
    public String toString() {
        String str = "";
        str += "start_time = " + start_time.floatValue() + "\n";
        str += "sim_dt = " + sim_dt.floatValue() + "\n";
        str += "duration = " + duration.floatValue() + "\n";
        str += "sim_delay = " + sim_delay.floatValue() + "\n";
        str += "link_offset = " + link_offset.floatValue() + "\n";
        str += "lane_width_meters = " + lane_width_meters.floatValue() + "\n";
        str += "node_size = " + node_size.floatValue() + "\n";
        str += "road_color_scheme = " + road_color_scheme.toString() + "\n";
        str += "max_density_vpkpl = " + max_density_vpkpl.toString() + "\n";
        return str;
    }
}

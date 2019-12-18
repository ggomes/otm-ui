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

    private float old_node_size;
    private boolean old_view_nodes;
    private float old_link_offset;
    private float old_lane_width_meters;
    private RoadColorScheme old_road_color_scheme;
    private float old_max_density_vpkpl;
    private boolean old_view_actuators;

    public enum RoadColorScheme { Black, Cells, RoadType }

    // simulation run parameters
    public SimpleFloatProperty start_time           = new SimpleFloatProperty(null,"start_time",0f);            // seconds after midnight
    public SimpleFloatProperty sim_dt               = new SimpleFloatProperty(null,"sim_dt",2f);                // seconds after midnight
    public SimpleFloatProperty duration             = new SimpleFloatProperty(null,"duration",4000f);           // seconds
    public SimpleFloatProperty sim_delay            = new SimpleFloatProperty(null,"sim_delay",15f);             // milliseconds

    // display parameters
    public SimpleFloatProperty link_offset          = new SimpleFloatProperty(null,"link_offset",3f);           // [m] painted width of a lane
    public SimpleFloatProperty lane_width_meters    = new SimpleFloatProperty(null,"lane_width_meters",5f);            // [m] painted width of a lane
    public SimpleFloatProperty node_size            = new SimpleFloatProperty(null,"node_size",4f);           // [m] painted radius for circular nodes
    public SimpleObjectProperty road_color_scheme = new SimpleObjectProperty(null,"road color scheme", RoadColorScheme.Black);
    public SimpleBooleanProperty view_nodes         = new SimpleBooleanProperty(null,"view_nodes",true);
    public SimpleBooleanProperty view_actuators     = new SimpleBooleanProperty(null,"view_actuators",true);
    public SimpleFloatProperty max_density_vpkpl    = new SimpleFloatProperty(null,"max_density_vpkpl",100f);   // [vpkpl] used for displaying MN states

    public GlobalParameters(Scene scene){

        old_node_size = node_size();
        old_view_nodes = view_nodes();
        old_link_offset = link_offset();
        old_lane_width_meters = lane_width_meters();
        old_road_color_scheme = road_color_scheme();
        old_max_density_vpkpl = max_density_vpkpl();
        old_view_actuators = view_actuators();

        start_time.addListener(         e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.SIMULATION)));
        sim_dt.addListener(             e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.SIMULATION)));
        duration.addListener(           e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.SIMULATION)));
        sim_delay.addListener(          e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.SIMULATION)));

        link_offset.addListener( e-> {
            if (Math.abs(old_link_offset-link_offset())>0.1f) {
                Event.fireEvent(scene,new ParameterChange(ParameterChange.SET_LINK_OFFSET));
                old_link_offset = link_offset();
            }
        });

        lane_width_meters.addListener(  e-> {
            if (Math.abs(old_lane_width_meters-lane_width_meters())>0.1f) {
                Event.fireEvent(scene,new ParameterChange(ParameterChange.SET_LINK_WIDTH));
                old_lane_width_meters = lane_width_meters();
            }
        });

        node_size.addListener( e-> {
            if (Math.abs(old_node_size-node_size())>0.1f) {
                Event.fireEvent(scene, new ParameterChange(ParameterChange.SET_NODE_SIZES));
                old_node_size = node_size();
            }
        });

        road_color_scheme.addListener( e-> {
            if (!old_road_color_scheme.equals(road_color_scheme())) {
                Event.fireEvent(scene,new ParameterChange(ParameterChange.SET_LINK_COLOR_SCHEME));
                old_road_color_scheme = road_color_scheme();
            }
        });

        max_density_vpkpl.addListener( e-> {
            if (Math.abs(old_max_density_vpkpl-max_density_vpkpl())>0.1f) {
                Event.fireEvent(scene,new ParameterChange(ParameterChange.SET_MAX_DENSITY));
                old_max_density_vpkpl = max_density_vpkpl();
            }
        });

        view_nodes.addListener(e-> {
            if (old_view_nodes!=view_nodes.get()) {
                Event.fireEvent(scene,new ParameterChange(ParameterChange.VIEW_NODES));
                old_view_nodes = view_nodes();
            }
        });

        view_actuators.addListener( e-> {
            if (old_view_actuators!=view_actuators.get()) {
                Event.fireEvent(scene,new ParameterChange(ParameterChange.VIEW_ACTUATORS));
                old_view_actuators = view_actuators();
            }
        });
    }

    public float start_time(){
        return start_time.floatValue();
    }

    public float sim_dt(){
        return sim_dt.floatValue();
    }

    public float duration(){
        return duration.floatValue();
    }

    public float sim_delay(){
        return sim_delay.floatValue();
    }

    public float link_offset(){
        return link_offset.floatValue();
    }

    public float lane_width_meters(){
        return lane_width_meters.floatValue();
    }

    public float node_size(){
        return node_size.floatValue();
    }

    public boolean view_nodes(){
        return view_nodes.getValue();
    }

    public boolean view_actuators(){
        return view_actuators.getValue();
    }

    public float max_density_vpkpl(){
        return max_density_vpkpl.floatValue();
    }


    public RoadColorScheme road_color_scheme(){
        return (RoadColorScheme) road_color_scheme.getValue();
    }

    public AbstractColormap road_colormap(){
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

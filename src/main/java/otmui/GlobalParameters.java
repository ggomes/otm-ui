/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui;

import otmui.event.ParameterChange;
import otmui.graph.color.AbstractColormap;
import otmui.graph.color.HSVColormap;
import otmui.graph.color.MatlabColormap;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.scene.Scene;

public class GlobalParameters {

    public enum ColorScheme { Black, Cells, RoadType}

    // simulation run parameters
    public SimpleFloatProperty start_time           = new SimpleFloatProperty(null,"start_time",0f);            // seconds after midnight
    public SimpleFloatProperty sim_dt               = new SimpleFloatProperty(null,"sim_dt",2f);                // seconds after midnight
    public SimpleFloatProperty duration             = new SimpleFloatProperty(null,"duration",4000f);           // seconds
    public SimpleFloatProperty sim_delay            = new SimpleFloatProperty(null,"sim_delay",15f);             // milliseconds

    // display parameters
    public SimpleFloatProperty link_offset          = new SimpleFloatProperty(null,"link_offset",0f);           // [m] painted width of a lane
    public SimpleFloatProperty lane_width_meters    = new SimpleFloatProperty(null,"lane_width",20f);            // [m] painted width of a lane
    public SimpleFloatProperty node_radius          = new SimpleFloatProperty(null,"node_radius",0f);           // [m] painted radius for circular nodes
    public SimpleObjectProperty color_scheme        = new SimpleObjectProperty(null,"color_scheme",ColorScheme.Black);

    // MN model parameters
    public SimpleFloatProperty max_density_vpkpl    = new SimpleFloatProperty(null,"max_density_vpkpl",100f);   // [vpkpl] used for displaying MN states

    public GlobalParameters(Scene scene){
        start_time.addListener(         e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.SIMULATION)));
        sim_dt.addListener(             e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.SIMULATION)));
        duration.addListener(           e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.SIMULATION)));
        sim_delay.addListener(          e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.SIMULATION)));
        link_offset.addListener(        e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.DISPLAY)));
        lane_width_meters.addListener(  e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.DISPLAY)));
        node_radius.addListener(        e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.DISPLAY)));
        color_scheme.addListener(       e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.DISPLAY)));
        max_density_vpkpl.addListener(  e-> Event.fireEvent(scene,new ParameterChange(ParameterChange.DISPLAY)));
    }

    public AbstractColormap get_colormap(){
        return new MatlabColormap(
                this.max_density_vpkpl.floatValue() ,
                (GlobalParameters.ColorScheme) this.color_scheme.getValue());
//        return new HSVColormap(
//                this.max_density_vpkpl.floatValue() ,
//                (GlobalParameters.ColorScheme) this.color_scheme.getValue());
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
        str += "node_radius = " + node_radius.floatValue() + "\n";
        str += "color_scheme = " + color_scheme.toString() + "\n";
        str += "color_scheme = " + color_scheme.toString() + "\n";
        str += "max_density_vpkpl = " + max_density_vpkpl.toString() + "\n";
        return str;
    }
}

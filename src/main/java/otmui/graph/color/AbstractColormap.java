/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.graph.color;

import otmui.GlobalParameters;
import common.Link;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public abstract class  AbstractColormap {

    public List<RGB> colorlist;
    public double cmin = 0d;
    public float max_density_vphpl;
    public GlobalParameters.ColorScheme colorScheme;

    public AbstractColormap(double [][] colors,float max_density_vphpl,GlobalParameters.ColorScheme colorScheme){
        this.max_density_vphpl = max_density_vphpl;
        this.colorScheme = colorScheme;
        colorlist = new ArrayList<>();
        for(int i=0;i<colors.length;i++)
            colorlist.add(new RGB(colors[i]));
    }

    public RGB get_color(double x,double cmax){

        // normalize
        double xx = (x-cmin)/(cmax-cmin);

        // discretize
        int index = (int) (xx*colorlist.size());

        if(index<0)
            index = 0;
        if(index>=colorlist.size())
            index = colorlist.size()-1;

        return colorlist.get(index);
    }


    public Color get_color_for_roadtype(Link.RoadType road_type){
        Color color;
        switch(colorScheme){
            case Black:
                color = new Color(0d,0d,0d,0.7);
                break;
            case Cells:
                color = new Color(Math.random(),Math.random(),Math.random(),0.7);
                break;
            case RoadType:
                switch(road_type){
                    case none:
                        color = Color.BLACK;
                        break;
                    case onramp:
                        color = Color.BLUE;
                        break;
                    case offramp:
                        color = Color.DARKMAGENTA;
                        break;
                    case freeway:
                        color = Color.BROWN;
                        break;
                    case arterial:
                        color = Color.CHARTREUSE;
                        break;
                    case hov:
                        color = Color.CYAN;
                        break;
                    case interconnect:
                        color = Color.DARKGREEN;
                        break;
                    case source:
                        color = Color.DEEPPINK;
                        break;
                    case sink:
                        color = Color.GREENYELLOW;
                        break;
                    default:
                        color = Color.BLACK;
                        break;
                }
                break;
            default:
                color = new Color(1d,1d,1d,1d);
                break;
        }
        return color;
    }

}

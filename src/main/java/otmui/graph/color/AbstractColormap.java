package otmui.graph.color;

import otmui.GlobalParameters;
import common.Link;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class  AbstractColormap {

    public List<RGB> colorlist;
    public double cmin = 0d;
    public float max_density_vphpl;
    public GlobalParameters.RoadColorScheme roadColorScheme;
    public static Map<String,Color> params2color = new HashMap<>();


    static{
        params2color.put("residential",Color.BLACK);
        params2color.put("tertiary",Color.VIOLET);
        params2color.put("tertiary_link",Color.TEAL);
        params2color.put("unclassified",Color.POWDERBLUE);
        params2color.put("secondary",Color.ORANGERED);
    }

    public AbstractColormap(double [][] colors, float max_density_vphpl, GlobalParameters.RoadColorScheme roadColorScheme){
        this.max_density_vphpl = max_density_vphpl;
        this.roadColorScheme = roadColorScheme;
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

    public static Color get_color(GlobalParameters.RoadColorScheme roadColorScheme,otmui.item.Link link){
        Color color = null;
        switch(roadColorScheme){
            case Black:
                color = new Color(0d,0d,0d,0.7);
                break;
            case Cells:
                break;

            case Params:
                String name = link.link.road_param.getName();
                if(params2color.containsKey(name))
                    color = params2color.get(name);
                else{
                    System.out.println(name);
                    color = new Color(Math.random(),Math.random(),Math.random(),0.7);
                    params2color.put(name,color);
                }
                break;

            case RoadType:
                switch(link.link.road_type){
                    case none:
                        color = Color.BLACK;
                        break;
                    case onramp:
                    case offramp:
                        color = Color.BLUE;
                        break;
                    case freeway:
                        color = Color.DARKMAGENTA;
                        break;
//                        color = Color.BROWN;
//                        color = Color.CHARTREUSE;
//                        color = Color.CYAN;
//                        color = Color.DARKGREEN;
//                        color = Color.DEEPPINK;
//                        color = Color.GREENYELLOW;
                    case bridge:
                        color = Color.TOMATO;
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

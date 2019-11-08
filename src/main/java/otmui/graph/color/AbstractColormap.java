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

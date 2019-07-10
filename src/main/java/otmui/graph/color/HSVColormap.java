package otmui.graph.color;

import otmui.GlobalParameters;

public class HSVColormap extends AbstractColormap {

    public HSVColormap(float max_density_vphpl, GlobalParameters.ColorScheme colorScheme){
        super( new double[][]{
                { 0d , 0d , 0d } ,
                { 1d , 1d , 1d }
//                { 1d , 0d , 0d } ,
//                { 1d , 1d , 0d } ,
//                { 0d , 1d , 0d } ,
//                { 0d , 1d , 1d } ,
//                { 0d , 0d , 1d } ,
//                { 1d , 0d , 1d }
        } , max_density_vphpl,colorScheme);

    }

}

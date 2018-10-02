/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
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

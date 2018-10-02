/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.graph.color;

import otmui.GlobalParameters;

public class MatlabColormap extends AbstractColormap {

    public MatlabColormap(float max_density_vphpl, GlobalParameters.ColorScheme colorScheme){
        super( new double[][]{
            { 0.242 , 0.150 , 0.6603 } ,
            { 0.250 , 0.165 , 0.7076 } ,
            { 0.257 , 0.181 , 0.7511 } ,
            { 0.264 , 0.197 , 0.7952 } ,
            { 0.270 , 0.214 , 0.8364 } ,
            { 0.275 , 0.234 , 0.8710 } ,
            { 0.278 , 0.255 , 0.8991 } ,
            { 0.280 , 0.278 , 0.9221 } ,
            { 0.281 , 0.300 , 0.9414 } ,
            { 0.281 , 0.322 , 0.9579 } ,
            { 0.279 , 0.344 , 0.9717 } ,
            { 0.276 , 0.366 , 0.9829 } ,
            { 0.269 , 0.389 , 0.9906 } ,
            { 0.260 , 0.412 , 0.9952 } ,
            { 0.244 , 0.435 , 0.9988 } ,
            { 0.220 , 0.460 , 0.9973 } ,
            { 0.196 , 0.484 , 0.9892 } ,
            { 0.183 , 0.507 , 0.9798 } ,
            { 0.178 , 0.528 , 0.9682 } ,
            { 0.176 , 0.549 , 0.9520 } ,
            { 0.168 , 0.570 , 0.9359 } ,
            { 0.154 , 0.590 , 0.9218 } ,
            { 0.146 , 0.609 , 0.9079 } ,
            { 0.138 , 0.627 , 0.8973 } ,
            { 0.124 , 0.645 , 0.8883 } ,
            { 0.111 , 0.663 , 0.8763 } ,
            { 0.095 , 0.679 , 0.8598 } ,
            { 0.068 , 0.694 , 0.8394 } ,
            { 0.029 , 0.708 , 0.8163 } ,
            { 0.003 , 0.720 , 0.7917 } ,
            { 0.006 , 0.731 , 0.7660 } ,
            { 0.043 , 0.741 , 0.7394 } ,
            { 0.096 , 0.750 , 0.7120 } ,
            { 0.140 , 0.758 , 0.6842 } ,
            { 0.171 , 0.767 , 0.6554 } ,
            { 0.193 , 0.775 , 0.6251 } ,
            { 0.216 , 0.784 , 0.5923 } ,
            { 0.247 , 0.791 , 0.5567 } ,
            { 0.290 , 0.797 , 0.5188 } ,
            { 0.340 , 0.800 , 0.4789 } ,
            { 0.390 , 0.802 , 0.4354 } ,
            { 0.445 , 0.802 , 0.3909 } ,
            { 0.504 , 0.799 , 0.3480 } ,
            { 0.561 , 0.794 , 0.3045 } ,
            { 0.617 , 0.787 , 0.2612 } ,
            { 0.672 , 0.779 , 0.2227 } ,
            { 0.724 , 0.769 , 0.1910 } ,
            { 0.773 , 0.759 , 0.1646 } ,
            { 0.820 , 0.749 , 0.1535 } ,
            { 0.863 , 0.740 , 0.1596 } ,
            { 0.903 , 0.733 , 0.1774 } ,
            { 0.939 , 0.728 , 0.2100 } ,
            { 0.972 , 0.729 , 0.2394 } ,
            { 0.995 , 0.743 , 0.2371 } ,
            { 0.997 , 0.765 , 0.2199 } ,
            { 0.995 , 0.789 , 0.2028 } ,
            { 0.989 , 0.813 , 0.1885 } ,
            { 0.978 , 0.838 , 0.1766 } ,
            { 0.967 , 0.863 , 0.1643 } ,
            { 0.961 , 0.889 , 0.1537 } ,
            { 0.959 , 0.913 , 0.1423 } ,
            { 0.962 , 0.937 , 0.1265 } ,
            { 0.969 , 0.960 , 0.1064 } ,
            { 0.976 , 0.983 , 0.0805 }
        } , max_density_vphpl,colorScheme);

    }
}

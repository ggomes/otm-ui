/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.graph.color;

public class RGB {
    public double red,green,blue;

    public RGB(double [] c){
        this.red = c[0];
        this.green = c[1];
        this.blue = c[2];
    }
    public RGB(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
}

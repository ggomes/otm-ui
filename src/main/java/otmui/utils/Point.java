/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.utils;

public class Point {
    public float x;
    public float y;
    public Point(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Point(common.Point p){
        this.x = p.x;
        this.y = p.y;
    }

}

/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.model;

public class Actuator {

    private actuator.AbstractActuator bactuator;

    public Actuator(actuator.AbstractActuator bactuator){
        this.bactuator = bactuator;
    }

}

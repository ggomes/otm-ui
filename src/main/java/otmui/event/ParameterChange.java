/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */

package otmui.event;

import javafx.event.Event;
import javafx.event.EventType;

public class ParameterChange extends Event {

    public static final EventType<ParameterChange> SIMULATION = new EventType<>(Event.ANY, "Simulation");
    public static final EventType<ParameterChange> DISPLAY = new EventType<>(Event.ANY, "Display");

    public ParameterChange(EventType<? extends Event> eventType) {
        super(eventType);
    }
}

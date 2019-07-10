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
    public static final EventType<ParameterChange> UPDATEMODELS = new EventType<>(Event.ANY, "Update model data and graph");
    public static final EventType<ParameterChange> DRAWLINKS = new EventType<>(Event.ANY, "Draw links");
    public static final EventType<ParameterChange> DRAWNODES = new EventType<>(Event.ANY, "Draw nodes");

    public ParameterChange(EventType<? extends Event> eventType) {
        super(eventType);
    }
}

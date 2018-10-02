/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
package otmui.simulation;

import api.APIopen;
import otmui.GlobalParameters;
import otmui.MainApp;
import otmui.graph.color.AbstractColormap;
import error.OTMException;
import javafx.concurrent.Task;
import output.animation.AnimationInfo;
import runner.OTM;

public class OTMTask extends Task {

    private MainApp app;
    private APIopen otm;
    private OTMException exception;
    private float start_time;
    private float duration;
    private float sim_dt;
    private long sim_delay;
    private AbstractColormap colormap;

    public OTMTask(APIopen otm, GlobalParameters params, MainApp app){
        this.app = app;
        this.otm = otm;
        this.start_time = params.start_time.floatValue();
        this.duration = params.duration.floatValue();
        this.sim_dt = params.sim_dt.floatValue();
        this.sim_delay = params.sim_delay.longValue();
        this.colormap = params.get_colormap();
    }

    @Override
    protected Object call()  {

        try {
            OTM.initialize(otm.scenario(), start_time);
            final int steps = (int) (duration / sim_dt);
            for (int i=1; i<=steps; i++) {

                if (isCancelled())
                    break;

                // delay simulation for better visualization
                Thread.sleep(sim_delay);

                // advance otm, get back information
                OTM.advance(otm.scenario(), sim_dt);
                AnimationInfo info = otm.api.get_animation_info();

                // send data to graph
                app.graphpaneController.draw_link_state(info,colormap);

                // update status bar
                updateProgress(i, steps);
                updateMessage(String.format("%.0f",info.timestamp));
            }

        } catch (OTMException e) {
            this.exception = e;
            failed();
        } catch (InterruptedException e) {
            this.exception = new OTMException(e);
            failed();
        } finally {
            app.statusbarController.unbind_progress();
            app.statusbarController.unbind_text();
        }
        return null;
    }

    @Override
    protected void failed() {
        super.failed();
        System.err.println(exception);
    }

}

package otmui.simulation;

import api.OTMdev;
import javafx.application.Platform;
import otmui.GlobalParameters;
import otmui.controller.GraphPaneController;
import otmui.controller.MenuController;
import otmui.controller.StatusBarController;
import otmui.graph.color.AbstractColormap;
import error.OTMException;
import javafx.concurrent.Task;
import output.animation.AnimationInfo;
import runner.OTM;

public class OTMTask extends Task {

    private OTMdev otm;
    private OTMException exception;
    private float start_time;
    private float duration;
    private float sim_dt;
    private long sim_delay;
    private AbstractColormap colormap;
    private MenuController menuController;
    private GraphPaneController graphPaneController;
    private StatusBarController statusBarController;

    public OTMTask(OTMdev otm, GlobalParameters params, MenuController menuController,GraphPaneController graphPaneController, StatusBarController statusBarController){
        this.otm = otm;
        this.start_time = params.start_time.floatValue();
        this.duration = params.duration.floatValue();
        this.sim_dt = params.sim_dt.floatValue();
        this.sim_delay = params.sim_delay.longValue();
        this.colormap = params.get_colormap();
        this.menuController = menuController;
        this.graphPaneController = graphPaneController;
        this.statusBarController = statusBarController;
    }

    @Override
    protected Object call()  {
        try {

            menuController.disableRun();
            menuController.enablePause();
            menuController.enableRewind();
            menuController.disableParameters();
            menuController.disablePlots();

            otm.otm.initialize(start_time);
            final int steps = (int) (duration / sim_dt);
            for (int i=1; i<=steps; i++) {

                if (isCancelled())
                    break;

                // delay simulation for better visualization
                Thread.sleep(sim_delay);

                // advance otm, get back information
                otm.otm.advance(sim_dt);
                final int ii = i;

                // TODO : PUT THIS BACK
//                final AnimationInfo info = otm.otm.get_animation_info();
//
//                Platform.runLater(new Runnable() {
//                    @Override public void run() {
//                        graphPaneController.draw_link_state(info,colormap);
//                        updateProgress(ii, steps);
//                        updateMessage(String.format("%.0f",info.timestamp));
//                    }
//                });

            }

        } catch (OTMException e) {
            this.exception = e;
            failed();
        } catch (InterruptedException e) {
            this.exception = new OTMException(e);
            failed();
        } finally {
            statusBarController.unbind_progress();
            statusBarController.unbind_text();
            menuController.disablePause();
            menuController.enableRewind();
            menuController.enableParameters();
            menuController.enablePlots();
        }
        return null;
    }

    @Override
    protected void failed() {
        super.failed();
        System.err.println(exception);
    }

}

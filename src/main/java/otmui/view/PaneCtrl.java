package otmui.view;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 * Created by gomes on 2/6/2017.
 */
public class PaneCtrl {
    public AnchorPane pane;
    public Initializable ctrl;

    public PaneCtrl(AnchorPane pane, Initializable ctrl) {
        this.pane = pane;
        this.ctrl = ctrl;
    }
}

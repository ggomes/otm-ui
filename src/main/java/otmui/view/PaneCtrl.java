package otmui.view;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class PaneCtrl {
    public AnchorPane pane;
    public Initializable ctrl;

    public PaneCtrl(AnchorPane pane, Initializable ctrl) {
        this.pane = pane;
        this.ctrl = ctrl;
    }
}

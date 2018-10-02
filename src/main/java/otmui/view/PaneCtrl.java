/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */
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

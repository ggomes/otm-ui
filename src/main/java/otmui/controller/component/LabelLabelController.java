/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */

package otmui.controller.component;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LabelLabelController extends LabelItemController {

    @FXML
    private Label label2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setLabel2(String x) {
        this.label2.setText(x);
    }

}

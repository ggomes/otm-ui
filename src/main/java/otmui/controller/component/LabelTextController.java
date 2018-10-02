/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */

package otmui.controller.component;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LabelTextController extends LabelItemController  {

    @FXML
    private TextField text;

    public void setText(String x) {
        this.text.setText(x);
    }

}

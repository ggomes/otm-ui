/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */

package otmui.controller.component;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

public class LabelCheckboxController extends LabelItemController {

    @FXML
    private CheckBox checkbox;

    public void setSelected(boolean selected){
        checkbox.setSelected(selected);
    }

}

/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */

package otmui.controller.component;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class LabelButtonController extends LabelItemController {

    @FXML
    private Button button;

    public void setButtonLabel(String buttonText){
        button.setText(buttonText);
    }

    public void setButtonAction(EventHandler<MouseEvent> handler){
        if(handler!=null)
            button.setOnMouseClicked(handler);
    }

}

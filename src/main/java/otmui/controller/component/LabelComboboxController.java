/**
 * Copyright (c) 2018, Gabriel Gomes
 * All rights reserved.
 * This source code is licensed under the standard 3-clause BSD license found
 * in the LICENSE file in the root directory of this source tree.
 */

package otmui.controller.component;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.util.Collection;

public class LabelComboboxController extends LabelItemController {

    @FXML
    public ComboBox combobox;

    public void setComboboxItems(Collection<String> items){
        combobox.setItems(FXCollections.observableArrayList(items));
    }

    public void selectFirst(){
        combobox.getSelectionModel().selectFirst();
    }

    public void comboSelectAction(ActionEvent e){

    }

}

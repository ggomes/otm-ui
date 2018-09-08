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

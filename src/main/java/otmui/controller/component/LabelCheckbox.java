package otmui.controller.component;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;

public class LabelCheckbox extends LabelItem {

    @FXML
    private CheckBox checkbox;

    public void setSelected(boolean selected){
        checkbox.setSelected(selected);
    }

}

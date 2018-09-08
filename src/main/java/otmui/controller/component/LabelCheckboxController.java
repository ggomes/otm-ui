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

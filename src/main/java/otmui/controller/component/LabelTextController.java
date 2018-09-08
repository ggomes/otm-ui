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

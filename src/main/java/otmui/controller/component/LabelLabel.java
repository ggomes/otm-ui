package otmui.controller.component;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LabelLabel extends LabelItem {

    @FXML
    private Label label2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setLabel2(String x) {
        this.label2.setText(x);
    }

}

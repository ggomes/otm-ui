package otmui.controller.component;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LabelItem implements Initializable {

    @FXML
    private Label label;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setLabel(String x) {
        this.label.setText(x);
    }

    public String getLabel(){
        return label.getText();
    }


}



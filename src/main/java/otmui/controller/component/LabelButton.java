package otmui.controller.component;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class LabelButton extends LabelItem {

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

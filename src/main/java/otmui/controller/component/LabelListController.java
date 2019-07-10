package otmui.controller.component;

import otmui.event.FormSelectEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.Collection;

public class LabelListController extends LabelItemController {

    @FXML
    private ListView list;

    public void setListItems(Collection<String> setlist){
        ObservableList<String> items = FXCollections.observableArrayList(setlist);
        list.setItems(items);
    }

    public void mouseClick(MouseEvent mouseEvent){

        // only register left click
        if (!mouseEvent.getButton().equals(MouseButton.PRIMARY))
            return;

        int clickcount = mouseEvent.getClickCount();
        Object item = list.getSelectionModel().getSelectedItem();

        if(item==null)
            return;

        Long itemId = Long.parseLong((String)item);
        EventTarget target = mouseEvent.getTarget();

        switch(getLabel()){
            case "output links":
            case "input links":
            case "link":
                if(clickcount==1)
                    Event.fireEvent(target,new FormSelectEvent(FormSelectEvent.CLICK1_LINK,itemId));
                if(clickcount==2)
                    Event.fireEvent(target,new FormSelectEvent(FormSelectEvent.CLICK2_LINK,itemId));
                break;
            case "node":
            case "nodes":
                if(clickcount==2)
                    Event.fireEvent(target,new FormSelectEvent(FormSelectEvent.CLICK2_NODE,itemId));
                break;

            case "splitsForNode":
                if(clickcount==2)
                    Event.fireEvent(target,new FormSelectEvent(FormSelectEvent.CLICK2_SPLIT,itemId));
                break;
            case "vehicle type":
            case "vehicle types":
                if(clickcount==1)
                    Event.fireEvent(target,new FormSelectEvent(FormSelectEvent.CLICK1_COMMODITY,itemId));
                if(clickcount==2)
                    Event.fireEvent(target,new FormSelectEvent(FormSelectEvent.CLICK2_COMMODITY,itemId));
                break;
            case "actuator":
            case "actuators":
                if(clickcount==1)
                    Event.fireEvent(target,new FormSelectEvent(FormSelectEvent.CLICK1_ACTUATOR,itemId));
                if(clickcount==2)
                    Event.fireEvent(target,new FormSelectEvent(FormSelectEvent.CLICK2_ACTUATOR,itemId));
                break;
            case "controller":
                if(clickcount==2)
                    Event.fireEvent(target,new FormSelectEvent(FormSelectEvent.CLICK2_CONTROLLER,itemId));
                break;
            case "demandsForLink":
            case "demands":
                if(clickcount==2)
                    Event.fireEvent(target,new FormSelectEvent(FormSelectEvent.CLICK2_DEMAND,itemId));
                break;
            case "subnetwork":
            case "subnetworks":
            case "route":
            case "routes":
                if(clickcount==2)
                    Event.fireEvent(target,new FormSelectEvent(FormSelectEvent.CLICK2_SUBNETWORK,itemId));
                break;
            default:
                System.err.println("Unrecognized label: " + getLabel());
                break;
        }

    }

}

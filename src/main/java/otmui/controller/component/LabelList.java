package otmui.controller.component;

import otmui.Data;
import otmui.ItemType;
import otmui.event.FormSelectEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import otmui.item.AbstractItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LabelList extends LabelItem {

    @FXML
    private ListView list;

    private Map<String,AbstractItem> items;

    public void setListItems(Collection<AbstractItem> itemslist){
        this.items = new HashMap<>();
        for(AbstractItem item : itemslist)
            items.put(item.getName(),item);
        List<String> names = itemslist.stream().map(x->x.getName()).collect(Collectors.toList());
        list.setItems(FXCollections.observableArrayList(names));
    }

    public void mouseClick(MouseEvent mouseEvent){

        // only register left click
        if (!mouseEvent.getButton().equals(MouseButton.PRIMARY))
            return;

        int clickcount = mouseEvent.getClickCount();
        String name = (String) list.getSelectionModel().getSelectedItem();

        if(name==null)
            return;

        AbstractItem item = items.get(name);
//        Long itemId = Long.parseLong((String)name);
//        ItemType itemType = null;
//        EventTarget target = mouseEvent.getTarget();
//
//        switch(getLabel()){
//            case "output links":
//            case "input links":
//            case "link":
//                itemType = ItemType.link;
//                break;
//            case "node":
//            case "nodes":
//                itemType = ItemType.node;
//                break;
//            case "splitsForNode":
//                System.out.println("WRIUG948t0");
////                itemType = ItemType.split;
//                break;
//            case "vehicle type":
//            case "vehicle types":
//                itemType = ItemType.commodity;
//                break;
//            case "actuator":
//            case "actuators":
//                itemType = ItemType.actuator;
//                break;
//            case "controller":
//                itemType = ItemType.controller;
//                break;
//            case "demandsForLink":
//            case "demands":
//                System.out.println("609hje05ju");
////                itemType = ItemType.demand;
//                break;
//            case "subnetwork":
//            case "subnetworks":
//            case "route":
//            case "routes":
//                itemType = ItemType.subnetwork;
//                break;
//            default:
//                System.err.println("Unrecognized label: " + getLabel());
//                break;
//        }

        if(clickcount==1)
            Event.fireEvent(mouseEvent.getTarget(),new FormSelectEvent(FormSelectEvent.CLICK1,item));
        if(clickcount==2)
            Event.fireEvent(mouseEvent.getTarget(),new FormSelectEvent(FormSelectEvent.CLICK2,item));


    }

}

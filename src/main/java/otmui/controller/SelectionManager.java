package otmui.controller;

import error.OTMException;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import otmui.ItemType;
import otmui.MainApp;
import otmui.ScenarioModification;
import otmui.TypeId;
import otmui.event.*;
import otmui.item.*;

import java.util.*;

import static java.util.stream.Collectors.toSet;

public class SelectionManager {

    public MainApp myApp;
    public Map<ItemType,Set<AbstractItem>> selection;

    public SelectionManager(MainApp myApp) {

        this.myApp = myApp;

        selection = new HashMap<>();
        for(ItemType type : myApp.data.items.keySet())
            selection.put(type, new HashSet<>());
        // event listeners
        Scene scene = myApp.stage.getScene();
        scene.addEventFilter(GraphClickEvent.CLICK1, e-> graphClick1(e));
        scene.addEventFilter(GraphClickEvent.CLICK2, e-> graphClick2(e));
        scene.addEventFilter(TreeClickEvent.CLICK1, e-> treeClick1(e));
        scene.addEventFilter(TreeClickEvent.CLICK2, e-> treeClick2(e));
        scene.addEventFilter(FormSelectEvent.CLICK1, e-> formClick1(e));
        scene.addEventFilter(FormSelectEvent.CLICK2, e-> formClick2(e));
    }

    public void graphClick1(GraphClickEvent e){

        if(e.item==null)
            return;

        boolean selected = selection.get(e.item.getType()).contains(e.item);
        boolean shift_pressed = e.event.isShiftDown();
        if(selected)
            if(shift_pressed) {
                selection.get(e.item.getType()).remove(e.item);
            }
            else {
                clear_highlight();
                clear_selection();
            }
        else
            if(shift_pressed)
                selection.get(e.item.getType()).add(e.item);
            else{
                clear_highlight();
                clear_selection();
                selection.get(e.item.getType()).add(e.item);
            }

        e.consume();
        Event.fireEvent(myApp.stage.getScene(),new DoHighlightSelection(DoHighlightSelection.HIGHLIGHT_ANY,selection));
    }

    public void treeClick1(TreeClickEvent e){

        if(e.items==null)
            return;

        clear_selection();

        // add items to selection
        ObservableList<TreeItem> treeitems = e.items;
        for(TreeItem treeitem : treeitems){
            if(!treeitem.isLeaf())
                continue;
            TypeId typeId = myApp.data.getTypeId((String) treeitem.getValue());
            if(typeId.type==null)
                continue;
            AbstractItem item = myApp.data.getItem(typeId);
            selection.get(typeId.type).add(item);
        }

        e.consume();
        Event.fireEvent(myApp.stage.getScene(),new DoHighlightSelection(DoHighlightSelection.HIGHLIGHT_ANY,selection));
    }

    public void formClick1(FormSelectEvent e){

        if(e.item==null)
            return;

        clear_selection();

        // add items to selection
        selection.get(e.item.getType()).add(e.item);

        e.consume();
        Event.fireEvent(myApp.stage.getScene(),new DoHighlightSelection(DoHighlightSelection.HIGHLIGHT_ANY,selection));
    }

    public void treeClick2(TreeClickEvent e) {

        if(!e.item.isLeaf())
            return;

        clear_selection();
        clear_highlight();

        TypeId typeId = myApp.data.getTypeId((String) e.item.getValue());
        AbstractItem item = myApp.data.getItem(typeId);
        selection.get(typeId.type).add(item);

        e.consume();
        Event.fireEvent(myApp.stage.getScene(),new DoHighlightSelection(DoHighlightSelection.HIGHLIGHT_ANY,selection));
        Event.fireEvent(myApp.stage.getScene(),new DoOpenFormEvent(DoOpenFormEvent.OPEN,item));
    }

    public void graphClick2(GraphClickEvent e){

        if(e.item==null)
            return;

        clear_selection();
        clear_highlight();

        AbstractItem item = e.item;
        selection.get(item.getType()).add(item);

        e.consume();
        Event.fireEvent(myApp.stage.getScene(),new DoHighlightSelection(DoHighlightSelection.HIGHLIGHT_ANY,selection));
        Event.fireEvent(myApp.stage.getScene(),new DoOpenFormEvent(DoOpenFormEvent.OPEN,item));
    }

    public void formClick2(FormSelectEvent e){

        if(e.item==null)
            return;

        clear_selection();
        clear_highlight();

        selection.get(e.item.getType()).add(e.item);

        e.consume();
        Event.fireEvent(myApp.stage.getScene(),new DoHighlightSelection(DoHighlightSelection.HIGHLIGHT_ANY,selection));
        Event.fireEvent(myApp.stage.getScene(),new DoOpenFormEvent(DoOpenFormEvent.OPEN,e.item));
    }

    /////////////////////////////////////////////////
    // context menu
    /////////////////////////////////////////////////

    public void delete_selected_items(){
        for(Set<AbstractItem> X : selection.values())
            for (AbstractItem item : X) {
                myApp.data.delete_item(item);
                Event.fireEvent(myApp.stage.getScene(), new DoRemoveItem(DoRemoveItem.REMOVE_ITEM, item));
            }
    }

    public void merge_selected_nodes() {

        try {

            Set<otmui.item.Node> nodes = selection.get(ItemType.node).stream()
                    .map(n->(otmui.item.Node)n)
                    .collect(toSet());
            Set<Long> node_ids = nodes.stream().map(x->x.id).collect(toSet());

            // Collect actuators
            Set<otmui.item.Actuator> actuators = nodes.stream()
                    .filter(n->n.node.actuator!=null)
                    .map(n->(otmui.item.Actuator)myApp.data.items.get(ItemType.actuator).get(n.node.actuator.id))
                    .collect(toSet());

            if(actuators.size()>1){
                Alert alert = new Alert(Alert.AlertType.WARNING,"");
                Label label = new Label("The nodes have multiple actuators. Please remove some first.");
                label.setWrapText(true);
                alert.getDialogPane().setContent(label);
                alert.show();
                return;
            }

            otmui.item.Actuator actuator = actuators.size()==1 ? actuators.iterator().next() : null;

            // Remove the actuator from the node
            if(actuator!=null){
                otmui.item.Node node = nodes.stream().filter(n->n.node.getId().equals(actuator.actuator.target.getId())).findFirst().get();
                node.node.actuator = null;
                actuator.actuator.target = null;
            }

            float xcoord = (float) nodes.stream().mapToDouble(n->n.node.xcoord).average().getAsDouble();
            float ycoord = (float) nodes.stream().mapToDouble(n->n.node.ycoord).average().getAsDouble();

            // create new node
            otmui.item.Node new_node = ScenarioModification.insert_node(myApp,xcoord,ycoord,actuator);

            // reposition the actuator
            if(actuator!=null)
                actuator.relocate(new_node.xpos,new_node.ypos);

            // set of adjacent links
            Set<Long> link_ids = nodes.stream().flatMap(n->n.node.in_links.keySet().stream()).collect(toSet());
            link_ids.addAll(nodes.stream().flatMap(n->n.node.out_links.keySet().stream()).collect(toSet()));

            // process internal and external links
            for(Long link_id : link_ids){
                otmui.item.Link link = (otmui.item.Link) myApp.data.items.get(ItemType.link).get(link_id);
                common.Link clink = link.link;
                boolean starts_at = node_ids.contains(clink.start_node.getId());
                boolean ends_at = node_ids.contains(clink.end_node.getId());
                if(starts_at && ends_at)
                    ScenarioModification.delete_link(myApp,link);
                else if(starts_at) {
                    clink.start_node = new_node.node;
                    new_node.node.out_links.put(link_id,clink);
                }
                else if(ends_at) {
                    clink.end_node = new_node.node;
                    new_node.node.in_links.put(link_id,clink);
                }
            }

            // delete nodes from scenario
            for(otmui.item.Node node : nodes)
                ScenarioModification.delete_node(myApp,node);

        } catch (OTMException e) {
            e.printStackTrace();
        }
    }

    public void merge_selected_links(){
//        System.out.println("merge_links");
//        Set<Long> link_ids = selectedLinks.stream().map(x->x.id).collect(toSet());
//        System.out.println(link_ids);
    }

    /////////////////////////////////////////////////
    // private
    /////////////////////////////////////////////////

    private void clear_selection(){
        selection = new HashMap<>();
        for(ItemType type : myApp.data.items.keySet())
            selection.put(type, new HashSet<>());

    }

    private void clear_highlight(){
        myApp.treeController.clearSelection();
        myApp.graphController.unhighlight(selection);
        myApp.statusbarController.setText("");
    }

}

package otmui.controller;

import actuator.InterfaceActuatorTarget;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.shape.Shape;
import otmui.ItemType;
import otmui.MainApp;
import otmui.TypeId;
import otmui.event.*;
import otmui.item.*;
import otmui.view.FactoryComponent;

import java.util.*;

import static java.util.stream.Collectors.toSet;

public class SelectionManager {

    public MainApp myApp;
    public Map<ItemType,Set<AbstractItem>> selection;

    public SelectionManager(MainApp myApp) {

        this.myApp = myApp;

        selection = new HashMap<>();
        for(ItemType type : ItemType.values())
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

    public void delete_selected_items()  {
        for(Set<AbstractItem> X : selection.values())
            for (AbstractItem item : X)
                myApp.data.delete_item(item);
    }

    public void merge_selected_nodes() {

        Set<Node> nodes = selection.get(ItemType.node).stream()
                .map(n->(Node)n)
                .collect(toSet());
        Set<Long> node_ids = nodes.stream().map(x->x.id).collect(toSet());

        // Collect actuators
        Set<Actuator> actuators = nodes.stream()
                .filter(n->n.node.actuator!=null)
                .map(n->(Actuator)myApp.data.items.get(ItemType.actuator).get(n.node.actuator.id))
                .collect(toSet());

        if(actuators.size()>1){
            FactoryComponent.warning_dialog("The nodes have multiple actuators. Please remove some first.");
            return;
        }

        Actuator actuator = actuators.size()==1 ? actuators.iterator().next() : null;

        // Remove the actuator from the node
        if(actuator!=null){
            InterfaceActuatorTarget target = actuator.actuator.target;
            if(target instanceof common.Node) {
                common.Node node = (common.Node) target;
                node.actuator = null;
                actuator.actuator.target = null;
            }
        }

        float xcoord = (float) nodes.stream().mapToDouble(n->n.node.xcoord).average().getAsDouble();
        float ycoord = (float) nodes.stream().mapToDouble(n->n.node.ycoord).average().getAsDouble();

        // create new node
        Node new_node = myApp.data.insert_node(xcoord,ycoord,actuator);

        // steal the old node's shape (this is so that we don't have to re-sort the pane shapes
        myApp.graphController.graphContainer.pane.getChildren().removeAll(new_node.shapegroup);
        Node first_node = nodes.iterator().next();
        Shape shape = first_node.shapegroup.iterator().next();
        first_node.shapegroup.clear();
        new_node.shapegroup.clear();
        new_node.shapegroup.add(shape);

        // reset mouse click handler for the new node
        GraphPaneController.attach_mouse_click_handler(new_node);

        // new node position is not out of syn with its shape. Fix that.
        new_node.relocate(new_node.xpos,new_node.ypos);

        // reposition the actuator
        if(actuator!=null)
            actuator.relocate(new_node.xpos,new_node.ypos);

        // set of adjacent links
        Set<Long> link_ids = nodes.stream().flatMap(n->n.node.in_links.keySet().stream()).collect(toSet());
        link_ids.addAll( nodes.stream()
                .flatMap(n->n.node.out_links.stream().map(l->l.getId()) )
                .collect(toSet())
        );

        // process internal and external links
        for(Long link_id : link_ids){
            Link link = (Link) myApp.data.items.get(ItemType.link).get(link_id);
            common.Link clink = link.link;
            boolean starts_at = clink.start_node!=null && node_ids.contains(clink.start_node.getId());
            boolean ends_at = clink.end_node!=null && node_ids.contains(clink.end_node.getId());
            if(starts_at && ends_at)
                myApp.data.delete_item(link);
            else if(starts_at) {
                clink.start_node = new_node.node;
                new_node.node.out_links.add(clink);
            }
            else if(ends_at) {
                clink.end_node = new_node.node;
                new_node.node.in_links.put(link_id,clink);
            }
        }

        // delete nodes from scenario
        for(Node node : nodes) {
            node.node.in_links.clear();
            node.node.out_links.clear();
            myApp.data.delete_item(node);
        }

    }

    public void merge_selected_links() {

//        selection.get(ItemType.link).add(myApp.data.items.get(ItemType.link).get(742663956L));
        selection.get(ItemType.link).add(myApp.data.items.get(ItemType.link).get(742663888L));
        selection.get(ItemType.link).add(myApp.data.items.get(ItemType.link).get(22649938L));

        Set<Link> links = selection.get(ItemType.link).stream()
                .map(n -> (Link) n)
                .collect(toSet());
        Set<Long> link_ids = links.stream().map(x -> x.id).collect(toSet());

        // Collect actuators
//        Set<Actuator> ramp_meters = links.stream()
//                .filter(n -> n.link.ramp_meter != null)
//                .map(n -> (Actuator) myApp.data.items.get(ItemType.actuator).get(n.link.ramp_meter.id))
//                .collect(toSet());

//        if (ramp_meters.size() > 1) {
//            FactoryComponent.warning_dialog("The link has multiple ramp meters. Please remove some first.");
//            return;
//        }

//        Set<Actuator> actuator_fds = links.stream()
//                .filter(n -> n.link.actuator_fd != null)
//                .map(n -> (Actuator) myApp.data.items.get(ItemType.actuator).get(n.link.actuator_fd.id))
//                .collect(toSet());
//
//        if (actuator_fds.size() > 1) {
//            FactoryComponent.warning_dialog("The link has multiple fd actuators. Please remove some first.");
//            return;
//        }

//        Actuator ramp_meter = ramp_meters.size() == 1 ? ramp_meters.iterator().next() : null;
//        Actuator actuator_fd = actuator_fds.size() == 1 ? actuator_fds.iterator().next() : null;

        //////////////////////////
//        if (ramp_meter != null) {
//            FactoryComponent.warning_dialog("Link merging with actuators is not implemented.");
//            return;
//        }
        //////////////////////////

        // collect internal and boundary nodes
        Set<Node> incident_nodes = new HashSet<>();
        incident_nodes.addAll(links.stream()
                .map(x -> (Node) myApp.data.items.get(ItemType.node).get(x.link.start_node.getId()))
                .collect(toSet()));
        incident_nodes.addAll(links.stream()
                .map(x -> (Node) myApp.data.items.get(ItemType.node).get(x.link.end_node.getId()))
                .collect(toSet()));

        Set<Node> internal_nodes = incident_nodes.stream()
                .filter(node -> node.node.out_links.size()==1 && node.node.in_links.size()==1)
                .filter(node -> link_ids.containsAll(node.node.out_links.stream().map(x->x.getId()).collect(toSet())))
                .filter(node -> link_ids.containsAll(node.node.in_links.keySet()))
                .collect(toSet());

        Set<Node> boundary_nodes = new HashSet<>();
        boundary_nodes.addAll(incident_nodes);
        boundary_nodes.removeAll(internal_nodes);

        // path = all but two nodes are internal, one is a relative source and one is a relative sink
        if( boundary_nodes.size()!= 2 ){
            FactoryComponent.warning_dialog("Not a linear path.");
            return;
        }

        Iterator<Node> it = boundary_nodes.iterator();
        Node bnode1 = it.next();
        Node bnode2 = it.next();

        boolean relsource1 = bnode1.node.in_links.keySet().stream().noneMatch(x->link_ids.contains(x));
        boolean relsink1 = bnode1.node.out_links.stream().noneMatch(x->link_ids.contains(x.getId()));
        boolean relsource2 = bnode2.node.in_links.keySet().stream().noneMatch(x->link_ids.contains(x));
        boolean relsink2 = bnode2.node.out_links.stream().noneMatch(x->link_ids.contains(x.getId()));

        // boundary node should be either relative source or relative sink
        if( !(relsource1 ^ relsink1) || !(relsource2 ^ relsink2) ){
            FactoryComponent.warning_dialog("These links cannot be merged.");
            return;
        }

        // one should be a relative source, the other a relative sink
        if( relsource1==relsource2 ){
            FactoryComponent.warning_dialog("These links cannot be merged.");
            return;
        }

        // these are the relative soure and sink nodes
        Node node_source = relsource1 ? bnode1 : bnode2;
        Node node_sink = relsink1 ? bnode1 : bnode2;

//        // collect the properties of the links
//        Set<LinkProperties> link_properties = links.stream().map(x->x.get_properties()).collect(toSet());
//
//        // choose which properties to use
//        LinkProperties link_prop = null
//        if(link_properties.size()>1){
//
//            // TODo ask the user to select which one
//            link_prop = link_properties.iterator().next();
//        }
//
//        // create a new link
//        common.Link clink =
//        Link new_link = myApp.data.insert_link(...)

        // reset the mouse click handlers

        // delete merged links
        for(Link link : links)
            myApp.data.delete_item(link);

        // delete internal nodes
        for(Node node : internal_nodes)
            myApp.data.delete_item(node);

    }

    public void add_stoplight(){
        System.out.println("WRH=5um33");
    }

    public void add_signal(){
        System.out.println("9834gn24v0");
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

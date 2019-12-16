package otmui.controller;

import java.util.*;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import otmui.Data;
import otmui.ItemType;
import otmui.MainApp;
import otmui.event.*;
import otmui.item.AbstractItem;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class TreeController {

    private MainApp myApp;

    @FXML
    private TreeView scenariotree;

    private Map<ItemType,Map<Long,TreeItem<String>>> tree;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public void initialize(MainApp myApp){
        this.myApp = myApp;

        scenariotree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Scene scene = myApp.stage.getScene();
        scene.addEventFilter(NewScenarioEvent.SCENARIO_LOADED, e->loadScenario(e.data) );
        scene.addEventFilter(DoHighlightSelection.HIGHLIGHT_ANY, e->highlight(e.selection) );


//        scene.addEventFilter(NewElementEvent.NEW_NODE, e->add_node(e.item));
//        scene.addEventFilter(DeleteElementEvent.REMOVE_LINK, e->remove_link((common.Link)e.item));
    }

    /////////////////////////////////////////////////
    // event handlers
    /////////////////////////////////////////////////

    private void loadScenario(Data data){
        tree = new HashMap<>();

        // populate the tree
        TreeItem<String> rootItem = new TreeItem<>("scenario");
        rootItem.setExpanded(false);

        for(ItemType type : ItemType.values()){

            if(!data.items.containsKey(type))
                continue;

            // dont put nodes in the tree
            if(type.equals(ItemType.node))
                continue;

            TreeItem<String> treebranch = new TreeItem<>(type.toString());
            Map<Long,TreeItem<String>> leaves = new HashMap<>();
            tree.put(type,leaves);
            rootItem.getChildren().add(treebranch);

            for (AbstractItem x : data.items.get(type).values()) {
                TreeItem item = new TreeItem<>(x.getName());
                leaves.put(x.id, item);
                treebranch.getChildren().add(item);
            }
        }

        scenariotree.setRoot(rootItem);
    }

    public void mouseClick(MouseEvent mouseEvent){

        // only register left click
        if (!mouseEvent.getButton().equals(MouseButton.PRIMARY))
            return;

        int clickcount = mouseEvent.getClickCount();
        ObservableList<TreeItem> items = (ObservableList<TreeItem>) getTreeView().getSelectionModel().getSelectedItems();

        // fire event for first click
        if(clickcount==1) {
            Event.fireEvent(mouseEvent.getTarget(), new TreeClickEvent(TreeClickEvent.CLICK1, items, null, mouseEvent));
        }

        if(clickcount==2){
            TreeItem item = (TreeItem) getTreeView().getSelectionModel().getSelectedItem();
            if(item==null)
                return;
            Event.fireEvent(mouseEvent.getTarget(),new TreeClickEvent(TreeClickEvent.CLICK2,null, item,mouseEvent));
        }

        mouseEvent.consume();
    }

    private void reset(){
        clearSelection();
    }

    public void add_node(Object o){
        System.out.println("HELLO!!!");
//        links_tree.getChildren().add(new TreeItem<>(Maps.name2linkid.getFromSecond(link.getId())));
    }


//    public void add_link(common.Link link){
////        links_tree.getChildren().add(new TreeItem<>(ItemPool.getName(link)));
//    }

//    public void remove_link(common.Link link){
//        TreeItem item = new TreeItem<>(link.get);
////        links_tree.getChildren().remove(item);
//    }

    /////////////////////////////////////////////////
    // focusing and hihglighting
    /////////////////////////////////////////////////

    public void focusGraph(ActionEvent e){
        myApp.graphController.focusGraphOnSelection();
    }

    public void highlight(Map<ItemType,Set<AbstractItem>> selection){
        MultipleSelectionModel<TreeItem<String>> model = scenariotree.getSelectionModel();
        for(Map.Entry<ItemType,Set<AbstractItem>> e : selection.entrySet()) {
            if(!tree.containsKey(e.getKey()))
                continue;
            e.getValue().forEach(item -> model.select(tree.get(item.getType()).get(item.id)));
        }
    }

    /////////////////////////////////////////////////
    // get / set
    /////////////////////////////////////////////////

    public TreeView getTreeView(){
        return scenariotree;
    }

    public void clearSelection(){
        scenariotree.getSelectionModel().clearSelection();
    }

}

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

//    private Map<ItemType,Map<Long,TreeItem<String>>> tree;

    /////////////////////////////////////////////////
    // construction
    /////////////////////////////////////////////////

    public void initialize(MainApp myApp){
        this.myApp = myApp;

        scenariotree.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Scene scene = myApp.stage.getScene();
        scene.addEventFilter(NewScenarioEvent.SCENARIO_LOADED, e->loadScenario(e.data) );
        scene.addEventFilter(DoHighlightSelection.HIGHLIGHT_ANY, e->highlight(e.selection) );
        scene.addEventFilter(DoRemoveItem.REMOVE_ITEM, e->remove_item(e.item));
        scene.addEventFilter(DoAddItem.ADD_ITEM, e->add_item(e.item));

    }

    /////////////////////////////////////////////////
    // event handlers
    /////////////////////////////////////////////////

    private void loadScenario(Data data){
//        tree = new HashMap<>();

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
//            Map<Long,TreeItem<String>> leaves = new HashMap<>();
//            tree.put(type,leaves);
            rootItem.getChildren().add(treebranch);

            for (AbstractItem item : data.items.get(type).values()) {
                TreeItem treeitem = new TreeItem<>(item.getName());
                item.treeitem = treeitem;
//                leaves.put(item.id, treeitem);
                treebranch.getChildren().add(treeitem);
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

    private void remove_item(AbstractItem item){
        if(item.treeitem!=null && item.treeitem.getParent()!=null)
            item.treeitem.getParent().getChildren().remove(item.treeitem);
    }


    private void add_item(AbstractItem item){
        if(item.treeitem==null)
            item.treeitem = new TreeItem<>(item.getName());

        TreeItem<String> root = scenariotree.getRoot();
        Optional<TreeItem<String>> parent = root.getChildren().stream()
                .filter(x->x.getValue().equals(item.getType().toString()))
                .findFirst();
        if(parent.isPresent())
            parent.get().getChildren().add(item.treeitem);
    }


    private void reset(){
        clearSelection();
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

    public void delete_item(ActionEvent e){
        myApp.selectionManager.delete_selected_items();
    }

    public void focusGraph(ActionEvent e){
        myApp.graphController.focusGraphOnSelection();
    }

    public void highlight(Map<ItemType,Set<AbstractItem>> selection){
        MultipleSelectionModel<TreeItem<String>> model = scenariotree.getSelectionModel();
        for(Map.Entry<ItemType,Set<AbstractItem>> e : selection.entrySet()) {
//            if(!tree.containsKey(e.getKey()))
//                continue;
            if(e.getValue()!=null && !e.getValue().isEmpty())
                e.getValue().forEach(item -> model.select(item.treeitem));
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

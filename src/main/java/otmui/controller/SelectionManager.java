package otmui.controller;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import otmui.ItemType;
import otmui.MainApp;
import otmui.TypeId;
import otmui.event.*;
import otmui.item.*;

import java.util.*;

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

        Set<AbstractItem> selectionPool = selection.get(e.item.getType());

        boolean selected = selectionPool.contains(e.item);
        boolean shift_pressed = e.event.isShiftDown();
        if(selected)
            if(shift_pressed)
                selectionPool.remove(e.item);
            else
                clear_selection();
        else
        if(shift_pressed)
            selectionPool.add(e.item);
        else
            clear_selection();
        selectionPool.add(e.item);
        selection.put(e.item.getType(),selectionPool);

        e.consume();

        System.out.println(selection.get(ItemType.link).size());

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

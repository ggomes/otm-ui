package otmui.item;

import javafx.scene.control.TreeItem;
import otmui.ItemType;

public abstract class AbstractItem {

    public final long id;
    public TreeItem<String> treeitem;

    public AbstractItem(){
        id = Long.MAX_VALUE;
    }

    public AbstractItem(long id) {
        this.id = id;
    }

    public final String getName() {
        return String.format("%s %d",getType(),id);
    }

    public abstract ItemType getType();

}

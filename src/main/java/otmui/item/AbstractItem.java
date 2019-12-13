package otmui.item;

import otmui.ItemType;

public abstract class AbstractItem {

    public final long id;

    public AbstractItem(){
        id = Long.MAX_VALUE;
    }

    public AbstractItem(long id) {
        this.id = id;
    }

    public abstract String getName();
    public abstract ItemType getType();

}

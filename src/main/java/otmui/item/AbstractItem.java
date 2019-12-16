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

    public final String getName() {
        return String.format("%s %d",getType(),id);
    }

    public abstract ItemType getType();

}

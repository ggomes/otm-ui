package otmui.item;

import javafx.scene.Group;

public abstract class AbstractItem extends Group {

    public final long id;

    public AbstractItem(){
        id = Long.MAX_VALUE;
    }

    public AbstractItem(long id) {
        this.id = id;
    }

    public abstract String getPrefix();
    public abstract String getName();
    public abstract void highlight();
    public abstract void unhighlight();

}

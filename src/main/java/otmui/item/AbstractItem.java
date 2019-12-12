package otmui.item;

import javafx.scene.Group;

public abstract class AbstractItem extends Group {
    public Long id;

    public abstract  void highlight();
    public abstract void unhighlight();
}

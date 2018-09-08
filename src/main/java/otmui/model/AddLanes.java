package otmui.model;

/**
 * Created by gomes on 2/4/2017.
 */
public class AddLanes {
    public int lanes;
    public float length;

    public AddLanes(geometry.AddLanes x){
        this.lanes = x.lanes;
        this.length = x.length;
    }

}

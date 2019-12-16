package otmui;

public class TypeId {
    public ItemType type;
    public long id;
    public TypeId(String itemName){
        String [] strs = itemName.split(" ");
        if(strs.length<2)
            return;
        type = ItemType.valueOf(strs[0]);
        id = Long.parseLong(strs[1]);
    }
}

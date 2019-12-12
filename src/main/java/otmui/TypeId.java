package otmui;

public class TypeId {
    public ItemType type;
    public long id;
    public TypeId(ItemType type, long id) {
        this.type = type;
        this.id = id;
    }
    public TypeId(String itemName){
        String [] strs = itemName.split(" ");
        type = ItemType.valueOf(strs[0]);
        id = Long.parseLong(strs[1]);
    }
}

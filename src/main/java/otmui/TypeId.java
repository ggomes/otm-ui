package otmui;

public class TypeId {
    public ItemType type;
    public long id;
    public TypeId(String itemName){
        String [] strs = itemName.split(" ");
        type = Data.itemNames.BtoA(strs[0]);
        id = Long.parseLong(strs[1]);
    }
}

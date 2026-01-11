import java.util.ArrayList;

public class OwnerCollection {
    ArrayList<Owner> currentOwners = new ArrayList<Owner>();
    public boolean addOwner(Owner newOwner){
        if(newOwner != null){
        if(!containsOwner(newOwner)){
        currentOwners.add(newOwner);
        return true;
        }
        }
        return false;
    }
    public boolean removeOwner(Owner owner){
        if(owner != null && containsOwner(owner)){
            
            currentOwners.set(currentOwners.indexOf(owner), null);
            return true;
        }
        return false;
    }
    
    public boolean containsOwner(String ownerName){
        for(Owner o : currentOwners){
            if(o.getName().equalsIgnoreCase(ownerName)){
                return true;
            }
        }
        return false;
    }
    public boolean containsOwner(Owner owner){
        if(containsOwner(owner.getName())) return true;
        return false;
    }
}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Order;

public class OwnerCollection {
    ArrayList<Owner> currentOwners = new ArrayList<Owner>();

    public boolean addOwner(Owner newOwner) {
        if (newOwner != null) {
            if (!containsOwner(newOwner)) {
                currentOwners.add(newOwner);
                return true;
            }
        }
        return false;
    }

    public boolean removeOwner(Owner owner) {
        if (owner != null && containsOwner(owner) &&removeOwner(owner.getName())) {
            return true;

        }
        return false;
    }

    public boolean removeOwner(String ownerName) {
        if (ownerName != null && containsOwner(ownerName)) {
            for (Owner o : currentOwners) {
                if (o.getName().equalsIgnoreCase(ownerName)) {
                    currentOwners.set(currentOwners.indexOf(o), null);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsOwner(String ownerName) {
        if (ownerName != null) {
            for (Owner o : currentOwners) {
                if (o.getName().equalsIgnoreCase(ownerName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsOwner(Owner owner) {
        if (owner != null) {
            if (containsOwner(owner.getName()))
                return true;
        }
        return false;
    }

    public Owner getOwner(String ownerName) {
        if (ownerName != null && containsOwner(ownerName)) {
            for (Owner o : currentOwners) {
                if (o.getName().equalsIgnoreCase(ownerName)) {
                    return o;
                }
            }
        }
        return null;
    }

    public ArrayList<Owner> getAllOwners() {
        ArrayList<Owner> tempArray = new ArrayList<Owner>();
        tempArray.addAll(currentOwners);
        return tempArray;
    }

    public int size() {
        return currentOwners.size();
    }
}

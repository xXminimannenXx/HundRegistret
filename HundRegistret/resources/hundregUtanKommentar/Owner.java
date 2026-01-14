
import java.util.Comparator;

public class Owner {

    private String name;
    private Dog[] currentDogs = new Dog[7];

    public Owner(String name) {
        if (name == null)
            throw new IllegalArgumentException("Name cant be null");
        name = name.trim();
        this.name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    public Owner(String name, Dog... dogs) {
        // gammal kod kvar från tidigare försök av lösningar
        this(name);


        for (int i = 0; i < dogs.length; i++) {
            if (dogs[i] == null) {
                throw new IllegalArgumentException("Dog cant be null");
            }
        }
        for (Dog dog : dogs) {
            boolean isDuplicant = false;
            for (Dog curDog : currentDogs) {
                if (curDog != null && curDog.getName().equals(dog.getName())) {
                    isDuplicant = true;
                    break;
                }
                if (!isDuplicant) {
                    boolean addedDog = false;
                    for (int i = 0; i < currentDogs.length; i++) {
                        if (currentDogs[i] == null) {
                            addDog(dog);
                            addedDog = true;
                            break;
                        }
                    }
                    if (!addedDog)
                        break;
                }
            }

        }

       
    }

    public Dog[] getDogs() {

      
        int newLen = 0; // hittar nya längden på arrayen
        for (Dog dog : currentDogs) {
            if (dog != null) {
                newLen++;
            }
        }
        int index = 0;
        Dog[] tempDogArray = new Dog[newLen]; // gör en ny array med rätt längd
        for (Dog dog : currentDogs) { // lägger in varje hund på ny plats

            if (dog != null) {
                tempDogArray[index] = dog;
                index++;
            }
        }
        sortDogs(tempDogArray);
        return tempDogArray;
    }

    public String getName() {
        return name;
    }

   
    public boolean addDog(Dog dog) {
        if (dog == null || ownsMaxDogs() || ownsDog(dog)) {
            return false;
        }
        for (int i = 0; i < currentDogs.length; i++) { // kolla att den inte är dubblet

            if (currentDogs[i] != null && currentDogs[i].getName().equals(dog.getName())) {

                return false;
            }
        }
        // Lägg till hunden på första lediga plats
        for (int i = 0; i < currentDogs.length; i++) {
            if (currentDogs[i] == null) {
                currentDogs[i] = dog;
                if (dog.getOwner() != this) {
                    dog.setOwner(this);
                }
                return true;
            }
        }
        return false; 
    }

    public boolean ownsAnyDog() {
        for (int i = 0; i < currentDogs.length; i++) {
            if (currentDogs[i] != null) {
                return true;
            }

        }
        return false;
    }

    public boolean ownsMaxDogs() {
        for (int i = 0; i < currentDogs.length; i++) {
            if (currentDogs[i] == null) {
                return false;
            }
        }
        return true;
    }

    public boolean removeDog(Dog dog) {
        if (dog == null)
            return false;

        for (int i = 0; i < currentDogs.length; i++) {
            if (currentDogs[i] != null && currentDogs[i].equals(dog)) {
                currentDogs[i] = null;
                if (dog.getOwner() == this) {
                    dog.setOwner(null); // nollställ ägare i hunden
                }
                return true;
            }
        }
        return false;
    }

    public boolean removeDog(String dogName) {
        if (dogName == null)
            return false;

        for (int i = 0; i < currentDogs.length; i++) {
            if (currentDogs[i] != null && currentDogs[i].getName().equalsIgnoreCase(dogName)) {
                Dog temp = currentDogs[i];
                currentDogs[i] = null;
                if (temp.getOwner() == this) {
                    temp.setOwner(null); // nollställ ägare i hunden
                }
                return true;
            }
        }
        return false;
    }

    public boolean ownsDog(String dogName) {
        if (!ownsAnyDog()) {
            return false;
        }
        for (int i = 0; i < currentDogs.length; i++) {
            if (currentDogs[i] != null && currentDogs[i].getName().equalsIgnoreCase(dogName)) {
                return true;
            }
        }
        return false;

    }

    public boolean ownsDog(Dog dog) {
        if (!ownsAnyDog()) {
            return false;
        }
        for (int i = 0; i < currentDogs.length; i++) {
            if (currentDogs[i] != null && currentDogs[i].equals(dog)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String dogNamesTemp = "";

        for (Dog d : currentDogs) {
            if (d != null) {
                dogNamesTemp += d.toString() + " ";

            }
        }

        return name + " " + dogNamesTemp;
    }

    private void sortDogs(Dog[] dogs) {
        DogSorter.sort(
                SortingAlgorithm.SELECTION_SORT,
                Comparator.comparing(Dog::getName),
                dogs);
    }

}

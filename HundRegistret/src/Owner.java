//TODO: metod som kollar ägare för hund samt om ägaren äger en hund vid de namnet om de inte matchar ska nuvarande hundens ägare(i hunden) tas bort och ägaren som kollar bli nya ägare

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

        /*
         * if (dogs.length > 7) {
         * 
         * throw new IllegalArgumentException("Cant have more than 7 dogs"); // kollar
         * så att inte de är mer än 7
         * // hundar i skapningen av ägar klassen
         * }
         */

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
       
        
        /*
         * for (int i = 0; i < dogs.length; i++) {
         * for (int j = 0; j < i; j++) {
         * if(currentDogs[j] != null){
         * if (currentDogs[j].equals(dogs[i])) {
         * throw new IllegalArgumentException("Cant have duplicant dogs");
         * }
         * }
         * currentDogs[i] = dogs[i];
         * }
         * }
         */
        /*
         * for(int i = 0; i < dogs.length; i++){
         * Owner tempOwner = dogs[i].getOwner().orElse(null);
         * if ( tempOwner != null && tempOwner.equals(this)) {
         * throw new IllegalArgumentException("Cant have duplicant dogs");
         * }
         * }
         */
        /*
         * for(int i = 0; i < dogs.length; i++){
         * dogs[i].getName().equals(getDogs().toString());
         * }
         */

        /*
         * for (int i = 0; i < dogs.length; i++) {
         * for (int j = 0; j < i; j++) {
         * if (!dogs[i].getName().equals(dogs[j].getName())) {
         * 
         * 
         * 
         * }
         * }
         * 
         * addDog(dogs[i]);
         * }
         */

    }

    public Dog[] getDogs() {
        
        // gammal lösning som inte var helt bra
        /*
         * int newLen = 0; // samma som under fast de va hund, hund, hund, null ,
         * null...
         * Dog[] tempDogArray = new Dog[0]; // fick fel på testet eftersom att den
         * returnerar null,null,null... istället
         * // för att vara tom så gjorde detta
         * if (!ownsAnyDog()) {
         * return tempDogArray;
         * }
         * for (int i = 0; i < currentDogs.length; i++) {
         * if (currentDogs[i] != null) {
         * newLen++;
         * tempDogArray[newLen] = currentDogs[i];
         * }
         * }
         * return Arrays.copyOf(currentDogs, newLen);
         */
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
        if (dog == null) {
            return false;
        }
        for (int i = 0; i < currentDogs.length; i++) { // kolla att den inte är dubblet
            if (currentDogs[i] != null && currentDogs[i].getName().equals(dog.getName())) {

                return false;
            }
        }
        for (int i = 0; i < currentDogs.length; i++) {// hitta första tomma plats och lägg in hunden där
            if (currentDogs[i] == null) {
                currentDogs[i] = dog;
              
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

    public boolean removeDog(String dogName) {
        if (dogName == null) {
            return false;
        }
        for (int i = 0; i < currentDogs.length; i++) { // kolla om hunden finns i arrayen och då ta bort
            if (currentDogs[i] != null && currentDogs[i].getName().equalsIgnoreCase(dogName)) {
                currentDogs[i] = null;
                return true;
            }
        }
        return false;
    }

    public boolean removeDog(Dog dog) {
        if (dog == null) {
            return false;
        }
        for (int i = 0; i < currentDogs.length; i++) {
            if (currentDogs[i] != null && currentDogs[i].equals(dog)) {
                currentDogs[i] = null;
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
        dogs
    );
}

}


import java.util.Arrays;


public class Owner {
    private String name;
    private Dog[] currentDogs = new Dog[7];

    public Owner(String name, Dog... dogs) {
        if (name == null) {
            throw new IllegalArgumentException("Name cant be null");
        }
        name = name.trim();
        this.name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();

        if (dogs.length > 7) {
            throw new IllegalArgumentException("Cant have more than 7 dogs"); // kollar så att inte de är mer än 7
                                                                              // hundar i skapningen av ägar klassen
        }
        for(int i = 0; i <dogs.length; i++){
            if(dogs[i] == null){
                throw new IllegalArgumentException("Dog cant be null");
            }
        }
        for (int i = 0; i < dogs.length; i++) {
            for (int j = 0; j < i; j++) {
                if (currentDogs[j] != null &&currentDogs[j].equals(dogs[i])) {
                    throw new IllegalArgumentException("Cant have duplicant dogs");
                }
                currentDogs[i] = dogs[i];
            }
        }

    }

   
    public Dog[] getDogs(){
        int newLen = 0; //samma som under fast de va hund, hund, hund, null , null... 
        Dog[] tempDogArray = new Dog[0]; //fick fel på testet eftersom att den returnerar null,null,null... istället för att vara tom så gjorde detta
        if(!ownsAnyDog()){
            return tempDogArray;
        }
        for(int i = 0; i < currentDogs.length; i++){
            if (currentDogs[i] != null) {
                newLen++;        
            }
        }
       return Arrays.copyOf(currentDogs,newLen );
        
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

    public boolean removeDog(String dogName) {
        if (dogName == null) {
            return false;
        }
        for (int i = 0; i < currentDogs.length; i++) { // kolla om hunden finns i arrayen och då ta bort
            if (currentDogs[i] != null || currentDogs[i].getName().equals(dogName)) {
                currentDogs[i] = null;
                return true;
            }
        }
        return false;
    }

    public boolean ownsAnyDog() {
        for (int i = 0; i < currentDogs.length; i++) {
            if (currentDogs[i] != null || currentDogs[i] != null) {
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
        if (dog == null) {
            return false;
        }
        for (int i = 0; i < currentDogs.length; i++) {
            if (currentDogs[i] != null || currentDogs[i].equals(dog)) {
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
            if (currentDogs[i] != null && currentDogs[i].getName().equals(dogName)) {
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
            if ( currentDogs[i] != null && currentDogs[i].equals(dog)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Dog)) {
            return false;
        }
        Dog other = (Dog) obj;
        return name.equals(other.getName());

    }

}

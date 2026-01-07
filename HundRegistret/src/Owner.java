import java.util.Arrays;
import java.util.Optional;

public class Owner {
    private String name;
    private Dog[] currentDogs = new Dog[7];

    public Owner(String name) {

        this.name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();

    }

    public Owner(String name, Dog... dogs) {
        this(name);
        if (dogs.length > 7) {
            throw new IllegalArgumentException("Cant have more than 7 dogs"); // kollar så att inte de är mer än 7
                                                                              // hundar i skapningen av ägar klassen
        }
        for (int i = 0; i < dogs.length; i++) {
            for (int j = 0; j < i; j++) {
                if (currentDogs[j].equals(dogs[i])) {
                    throw new IllegalArgumentException("Cant have duplicant dogs");
                }
                currentDogs[i] = dogs[i];
            }
        }

    }

    public Optional<Dog[]> getDogs() {
        if (currentDogs == null) {
            return Optional.empty();
        }
        Dog[] tempArray = Arrays.copyOf(currentDogs, currentDogs.length);

        return Optional.of(tempArray);
    }

    public String getName() {
        return name;
    }

    public boolean addDog(Dog dog) {
        if (dog == null) {
            return false;
        }
        for (int i = 0; i < currentDogs.length; i++) {
            if (currentDogs[i] != null && currentDogs[i].equals(dog)) {
                return false;
            }
        }
        for (int i = 0; i < currentDogs.length; i++) {
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
        for (int i = 0; i < currentDogs.length; i++) {
            if (currentDogs[i] != null || currentDogs[i].getName().equals(dogName)) {
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
            if (currentDogs[i] != null || currentDogs[i].equals(dog)) {
                currentDogs[i] = null;
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

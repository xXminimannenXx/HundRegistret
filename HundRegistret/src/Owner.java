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
        } else {
            for (int i = 0; i < dogs.length; i++) {
                if (currentDogs[i].equals(dogs[i])) {
                    System.out.print("Cant have duplicant dogs");
                } else {
                    currentDogs[i] = dogs[i];
                }
            }
        }
    }

    public Optional<Dog[]> getDogs() {
        return Optional.ofNullable(currentDogs);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}

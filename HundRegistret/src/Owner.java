public class Owner {
    private String name;

    public Owner(String name) {

        this.name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();

    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
